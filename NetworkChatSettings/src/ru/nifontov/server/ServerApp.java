package ru.nifontov.server;

import ru.nifontov.server.chat.MyServer;

public class ServerApp {
    public static final int DEFAULT_PORT=8189;

    public static void main(String[] args) {
        int port= DEFAULT_PORT;
        if(args.length != 0){
            port= Integer.parseInt(args[0]);
        }
        new MyServer().start(port);
    }
}
