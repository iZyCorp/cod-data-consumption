package fr.izy.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlatformDAO {

    protected Connection dataSourceConnection;

    public PlatformDAO(Connection dataSourceConnection) {
        this.dataSourceConnection = dataSourceConnection;
    }

    public void createTablePlatform() throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("CREATE TABLE IF NOT EXISTS public.platform\n" +
                "(\n" +
                "    id_platform serial,\n" +
                "    label character varying COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    CONSTRAINT platform_pk PRIMARY KEY (id_platform)\n" +
                ")");
        preparedStatement.execute();

        PreparedStatement insert = dataSourceConnection.prepareStatement("INSERT INTO public.platform (id_platform, label) VALUES (1, 'Playstation'), (2, 'Xbox'), (3, 'Battle.net'), (4, 'Steam') ON CONFLICT DO NOTHING");
        insert.execute();
    }
}
