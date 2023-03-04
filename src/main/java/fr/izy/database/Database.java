package fr.izy.database;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

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
}
