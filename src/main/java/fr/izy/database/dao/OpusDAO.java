package fr.izy.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OpusDAO {

    protected Connection dataSourceConnection;

    public OpusDAO(Connection dataSourceConnection) {
        this.dataSourceConnection = dataSourceConnection;
    }

    public void createTableOpus() throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("CREATE TABLE IF NOT EXISTS public.opus\n" +
                "(\n" +
                "    id_opus serial,\n" +
                "    name character varying(255) COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    CONSTRAINT opus_pkey PRIMARY KEY (id_opus)\n" +
                ")");
        preparedStatement.execute();

        populateDefault();
    }

    private void populateDefault() throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("INSERT INTO opus (name) VALUES ('bo3'),('iw'),('wwii'),('bo4'),('mw'),('cw'),('vg') ON CONFLICT DO NOTHING");
        preparedStatement.execute();
    }

}
