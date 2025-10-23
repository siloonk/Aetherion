package io.github.siloonk;

public class Main {
    public static void main(String[] args) {
        Logger.info("Starting the server...");
        MinecraftServer server = new MinecraftServer();
        server.start();
    }
}