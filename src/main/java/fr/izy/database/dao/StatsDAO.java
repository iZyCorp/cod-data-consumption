package fr.izy.database.dao;

import io.github.izycorp.moonapi.components.Opus;
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

    public void insertStats(double kda, Platform platform, Opus opus) throws SQLException {

        final int idPlatform = platform == Platform.PLAYSTATION ? 1 : platform == Platform.XBOX ? 2 : platform == Platform.BATTLE_NET ? 3 : 4;
        final int opusId = opus == Opus.BO3 ? 1 : opus == Opus.INFINITE_WARFARE ? 2 : opus == Opus.WWII ? 3 : opus == Opus.BO4 ? 4 : opus == Opus.MW2019 ? 5 : opus == Opus.COLD_WAR ? 6 : 7;

        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("SELECT insert_checker(?::numeric, ?::integer, ?::integer)");
        preparedStatement.setDouble(1, kda);
        preparedStatement.setInt(2, idPlatform);
        preparedStatement.setInt(3, opusId);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public int countNumberOfStats() throws SQLException {
        PreparedStatement preparedStatement = dataSourceConnection.prepareStatement("SELECT COUNT(*) FROM stats");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }


}
