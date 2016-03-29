package com.pasha.termo.model;

public enum ServerType {
    Termo(0),
    Iao(1);

    private final int value;

    ServerType(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public static ServerType fromInt(int value) {
        ServerType type = Termo;
        switch (value) {
            case 0:
                type = Termo;
                break;
            case 1:
                type = Iao;
                break;
        }
        return type;
    }

    public int getInt() {
        return toInt();
    }
}
