package fr.izy.database.dto;

import io.github.izycorp.moonapi.components.Platform;

import java.math.BigDecimal;

public class DataDTO {

    final BigDecimal kda;

    final String username;

    final Platform platform;

    public DataDTO(BigDecimal kda, String username, Platform platform) {
        this.kda = kda;
        this.username = username;
        this.platform = platform;
    }

    public BigDecimal getKda() {
        return kda;
    }

    public String getUsername() {
        return username;
    }

    public int getPlatform() {
        switch (platform) {
            case XBOX: return 2;
            case BATTLE_NET: return 3;
            default: return 1;
        }
    }
}
