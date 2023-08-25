package fr.izy.utils;

import io.github.izycorp.codapi.abstraction.Page;
import io.github.izycorp.codapi.abstraction.TitleEndpoint;
import io.github.izycorp.codapi.components.*;
import io.github.izycorp.codapi.query.RequestManager;
import io.github.izycorp.codapi.title.*;

public class TitleAdapter {
    private final TitleEndpoint title;

    public TitleAdapter(Opus opus, RequestManager requestManager) {
        switch (opus) {
            case BO3:
                title = new BlackOps3(requestManager);
                break;
            case BO4:
                title = new BlackOps4(requestManager);
                break;
            case INFINITE_WARFARE:
                title = new InfiniteWarfare(requestManager);
                break;
            case MW2019:
                title = new ModernWarfare2019(requestManager);
                break;
            default:
                throw new RuntimeException("This opus is not supported");
        }
    }

    public Page getLeaderboard(Platform platform, TimeFrame timeFrame, Gamemode gamemode, GameType gameType, int pageIndex) {

        try {
            if (title instanceof BlackOps3)
                return ((BlackOps3) title).getLeaderboard(platform, timeFrame, gamemode, gameType, pageIndex);

            if (title instanceof BlackOps4)
                return ((BlackOps4) title).getLeaderboard(platform, timeFrame, gamemode, gameType, pageIndex);

            if (title instanceof InfiniteWarfare)
                return ((InfiniteWarfare) title).getLeaderboard(platform, timeFrame, gamemode, gameType, pageIndex);

            if (title instanceof ModernWarfare2019)
                return ((ModernWarfare2019) title).getLeaderboard(platform, timeFrame, gamemode, gameType, pageIndex);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
