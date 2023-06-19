package fr.izy.database;

import org.postgresql.ds.PGSimpleDataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Database {

    private final Connection connection;

    public Database(String host, int port, String database, String user, String password) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{host});
        dataSource.setPortNumbers(new int[]{port});
        dataSource.setDatabaseName(database);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        this.connection = dataSource.getConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Fetch all SQL files in gradle resources folder
     * @return SQL files
     */
    public File[] fetchSQLFiles() {
        File folder = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("sql")).getFile());
        return folder.listFiles();

    }

    /**
     * Execute all SQL files in gradle resources folder
     * @param file The targeted file
     * @throws IOException If the file doesn't exist
     * @throws SQLException If the SQL query is invalid
     */
    public void executeSQLFile(File file) throws IOException, SQLException {

        // Grab all SQL queries in a String array
        List<String> queries = new ArrayList<>();

        // Read file
        BufferedReader reader = Files.newBufferedReader(file.toPath());

        // Create a StringBuilder to store each query
        StringBuilder queryBuilder = new StringBuilder();

        AtomicBoolean isFunction = new AtomicBoolean(false);

        // For each line in the file
        reader.lines().forEach( currentLine -> {

            // If the line is not a comment
            if (currentLine.startsWith("--")) return;

            // If the line is a trigger
            if(currentLine.contains("FUNCTION")) {
            	isFunction.set(true);
            }

            // Append the line to the query
            queryBuilder.append(currentLine).append(" ");

            // If the line ends with a semicolon, it's the end of the query
            if(isFunction.get()) {
                if(currentLine.contains("$$;")) {
                    isFunction.set(false);
                    queries.add(queryBuilder.toString());
                    queryBuilder.setLength(0);
                }
            } else {
                if (currentLine.endsWith(";")) {
                    queries.add(queryBuilder.toString());
                    queryBuilder.setLength(0);
                }
            }
        });

        // Execute each query
        for (String query : queries) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            // Release resources
            preparedStatement.close();
        }
    }
}
