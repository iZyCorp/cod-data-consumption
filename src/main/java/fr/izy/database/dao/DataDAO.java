package fr.izy.database.dao;

import fr.izy.database.dto.DataDTO;
import io.github.izycorp.moonapi.components.Platform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataDAO {

    protected Connection dataSourceConnection;

    public DataDAO(Connection dataSourceConnection) {
        this.dataSourceConnection = dataSourceConnection;
    }

    public void createTableData() throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("CREATE TABLE IF NOT EXISTS public.data\n" +
                "(\n" +
                "    id_data serial,\n" +
                "    kda numeric(15,2) NOT NULL,\n" +
                "    username character varying(90) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    id_platform integer,\n" +
                "    CONSTRAINT data_pkey PRIMARY KEY (id_data),\n" +
                "    CONSTRAINT data_username_key UNIQUE (username),\n" +
                "    CONSTRAINT data_platform_id_platform_fk FOREIGN KEY (id_platform)\n" +
                "        REFERENCES public.platform (id_platform) MATCH SIMPLE\n" +
                "        ON UPDATE NO ACTION\n" +
                "        ON DELETE NO ACTION\n" +
                ")");
        preparedStatement.execute();
    }

    public void replaceProcedure() {
        String procedure = "create or replace procedure insert_verify(numeric, character varying(90), integer)\n" +
                "    language plpgsql\n" +
                "as $$\n" +
                "BEGIN\n" +
                "   INSERT INTO data (kda, username, id_platform) VALUES ($1, $2, $3) ON CONFLICT DO NOTHING;\n" +
                "END;\n" +
                "$$;" +
                "CREATE OR REPLACE FUNCTION get_fetched_pages(platform int)\n" +
                "    RETURNS integer\n" +
                "    LANGUAGE plpgsql\n" +
                "AS $$\n" +
                "DECLARE\n" +
                "    v_count integer;\n" +
                "BEGIN\n" +
                "    SELECT COUNT(*) INTO v_count FROM data where id_platform = platform;\n" +
                "    RETURN v_count / 20;\n" +
                "END;\n" +
                "$$;";
        try {
            PreparedStatement preparedStatement = dataSourceConnection.prepareStatement(procedure);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLatestPage(int platform) throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("SELECT get_fetched_pages(?)");
        preparedStatement.setInt(1, platform - 1);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 1;
    }

    public void insertData(DataDTO data) throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("CALL insert_verify(?::numeric, ?::character varying(90), ?::integer)");
        preparedStatement.setBigDecimal(1, data.getKda());
        preparedStatement.setString(2, data.getUsername());
        preparedStatement.setInt(3, data.getPlatform());
        preparedStatement.executeUpdate();
    }

    public int countNumberOfUser(Platform platform) throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("SELECT COUNT(data.id_data) FROM Data WHERE id_platform = ?");
        int idPlatform = platform == Platform.PLAYSTATION ? 1 : platform == Platform.XBOX ? 2 : platform == Platform.BATTLE_NET ? 3 : 4;
        preparedStatement.setInt(1, idPlatform);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet().next() ? preparedStatement.getResultSet() : null;
        return resultSet.getInt(1);
    }
}
