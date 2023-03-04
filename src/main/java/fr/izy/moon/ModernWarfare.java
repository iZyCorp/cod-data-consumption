package fr.izy.moon;

import io.github.izycorp.moonapi.abstraction.Title;
import io.github.izycorp.moonapi.components.*;
import io.github.izycorp.moonapi.exceptions.MoonViolationException;
import io.github.izycorp.moonapi.query.RequestManager;
import org.json.JSONObject;

import java.util.Objects;

public class ModernWarfare extends Title {

    private final Opus opus;
    /**
     * Initialize the Title Object with a RequestManager object
     *
     * @param request - A valid RequestManager Object
     */
    public ModernWarfare(RequestManager request, Opus opus) {
        super(request);
        this.opus = opus;
    }

    public JSONObject getLeaderboards(Platform platform, TimeFrame timeFrame, Gamemode mode, GameType gameType, int page) throws MoonViolationException {
        JSONObject leaderboard = super.getLeaderboards(this.opus, platform, timeFrame, mode, gameType, page);
        if(leaderboard != null) return Objects.equals(leaderboard.getString("status"), "error") ? null : leaderboard;
        return null;
    }
}
