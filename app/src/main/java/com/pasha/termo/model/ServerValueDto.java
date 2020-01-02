package com.pasha.termo.model;

public class ServerValueDto {
    private ServerType serverType;
    private boolean isActual;
    private Integer temp;

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    public boolean isActual() {
        return isActual;
    }

    public void setActual(boolean actual) {
        isActual = actual;
    }

    public Integer getTemp() {
        return temp;
//        return -288;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }
}
