package fr.izy.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlatformDAO {

    protected Connection dataSourceConnection;

    public PlatformDAO(Connection dataSourceConnection) {
        this.dataSourceConnection = dataSourceConnection;
    }
}
