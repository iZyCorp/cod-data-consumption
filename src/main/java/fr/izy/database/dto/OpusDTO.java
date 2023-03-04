package fr.izy.database.dto;

public class OpusDTO {

    private int idOpus;

    private String name;

    public OpusDTO(int idOpus, String name) {
        this.idOpus = idOpus;
        this.name = name;
    }

    public int getIdOpus() {
        return idOpus;
    }

    public void setIdOpus(int idOpus) {
        this.idOpus = idOpus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
