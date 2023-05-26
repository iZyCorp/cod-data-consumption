package fr.izy.database.dao;

import io.github.izycorp.moonapi.components.Platform;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsDAO {

    protected Connection dataSourceConnection;

    public StatsDAO(Connection dataSourceConnection) {
        this.dataSourceConnection = dataSourceConnection;
    }

    public void createTableStats() throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("CREATE TABLE IF NOT EXISTS public.stats\n" +
                "(\n" +
                "    id_stats serial,\n" +
                "    kda numeric(15,2) NOT NULL,\n" +
                "    amount integer NOT NULL,\n" +
                "    id_platform integer,\n" +
                "    CONSTRAINT stats_pkey PRIMARY KEY (id_stats),\n" +
                "    CONSTRAINT stats_platform_id_platform_fk FOREIGN KEY (id_platform)\n" +
                "        REFERENCES public.platform (id_platform) MATCH SIMPLE\n" +
                "        ON UPDATE NO ACTION\n" +
                "        ON DELETE NO ACTION\n" +
                ")");
        preparedStatement.execute();
    }

    public void insertProcedureChecker() throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("CREATE OR REPLACE FUNCTION insert_checker(k numeric, p integer)\n" +
                "    RETURNS void\n" +
                "    LANGUAGE plpgsql\n" +
                "AS $$\n" +
                "DECLARE\n" +
                "    v_ integer;\n" +
                "BEGIN\n" +
                "    SELECT amount INTO v_ FROM stats WHERE kda = k AND id_platform = p;\n" +
                "    IF v_ IS NULL THEN\n" +
                "        INSERT INTO stats (kda, amount, id_platform) VALUES (k, 1, p);\n" +
                "    ELSE\n" +
                "        UPDATE stats SET amount = v_ + 1 WHERE kda = k AND id_platform = p;\n" +
                "    END IF;\n" +
                "END;\n" +
                "$$;");
        preparedStatement.execute();
    }

    public void insertStats(double kda, Platform platform) throws SQLException {
        int idPlatform = platform == Platform.PLAYSTATION ? 1 : platform == Platform.XBOX ? 2 : platform == Platform.BATTLE_NET ? 3 : 4;
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("SELECT insert_checker(?::numeric, ?::integer)");
        preparedStatement.setDouble(1, kda);
        preparedStatement.setInt(2, idPlatform);
        preparedStatement.execute();
    }

    public int countNumberOfStats() throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("SELECT COUNT(*) FROM stats");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }


}
