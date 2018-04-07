package com.pasha.termo.model;

import java.util.ArrayList;

public class WeatherDto {
    private JsonDateTime updated = new JsonDateTime();
    private ServerValueDto serverTermo = new ServerValueDto();
    private ServerValueDto serverIao = new ServerValueDto();
    private ArrayList<Integer> oldValues;
    private ArrayList<Integer> oldValuesIao;

    public JsonDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(JsonDateTime updated) {
        this.updated = updated;
    }

    public ServerValueDto getServerTermo() {
        return serverTermo;
    }

    public void setServerTermo(ServerValueDto serverTermo) {
        this.serverTermo = serverTermo;
    }

    public ServerValueDto getServerIao() {
        return serverIao;
    }

    public void setServerIao(ServerValueDto serverIao) {
        this.serverIao = serverIao;
    }

    public ArrayList<Integer> getOldValues() {
        return oldValues;
    }

    public void setOldValues(ArrayList<Integer> oldValues) {
        this.oldValues = oldValues;
    }

    public ArrayList<Integer> getOldValuesIao() {
        return oldValuesIao;
    }

    public void setOldValuesIao(ArrayList<Integer> oldValuesIao) {
        this.oldValuesIao = oldValuesIao;
    }
}
