package fr.izy.database.dto;

import java.sql.Connection;

public class PlatformDTO {

    protected Connection dataSourceConnection;

    public PlatformDTO(Connection dataSourceConnection) {
        this.dataSourceConnection = dataSourceConnection;
    }
}
