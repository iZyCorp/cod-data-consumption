package fr.izy.database.dao;

import java.sql.Connection;

public class OpusDAO {

    protected Connection dataSourceConnection;

    public OpusDAO(Connection dataSourceConnection) {
        this.dataSourceConnection = dataSourceConnection;
    }

}
