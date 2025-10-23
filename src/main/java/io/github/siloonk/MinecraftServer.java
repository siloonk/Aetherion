package io.github.siloonk;

import io.github.siloonk.protocol.ClientHandler;
import io.github.siloonk.protocol.GameState;
import io.github.siloonk.protocol.PacketDirection;
import io.github.siloonk.protocol.PacketRegistry;
import io.github.siloonk.protocol.packets.HandshakingPacket;
import io.github.siloonk.protocol.packets.status.PingRequestPacket;
import io.github.siloonk.protocol.packets.status.PingResponsePacket;
import io.github.siloonk.protocol.packets.status.StatusRequestPacket;
import io.github.siloonk.protocol.packets.status.StatusResponsePacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MinecraftServer {

    private ServerSocket serverSocket;

    private ArrayList<ClientHandler> handlers = new ArrayList<>();

    private PacketRegistry registry = new PacketRegistry();

    public MinecraftServer() {
        try {
            this.serverSocket = new ServerSocket(25565);
            registerPackets();
            Logger.info("Server has started!");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Something went wrong while starting the server!");
            throw new RuntimeException(e);
        }
    }

    public void start() {
        Logger.info("Listening for connections on port %d".formatted(this.serverSocket.getLocalPort()));
        try {
            while (!this.isClosed()) {
                Socket socket = serverSocket.accept();

                ClientHandler handler = new ClientHandler(socket, registry);
                handlers.add(handler);
                handler.start();
            }
        } catch (IOException e) {
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return this.serverSocket.isClosed() || !this.serverSocket.isBound();
    }

    private void registerPackets() {

        /**
         * handshaking
         */
        this.registry.register(PacketDirection.SERVERBOUND, GameState.HANDSHAKING, 0x00, HandshakingPacket.class);

        /**
         * Status
         */
        this.registry.register(PacketDirection.SERVERBOUND, GameState.STATUS, 0x00, StatusRequestPacket.class);
        this.registry.register(PacketDirection.SERVERBOUND, GameState.STATUS, 0x01, PingRequestPacket.class);

        this.registry.register(PacketDirection.CLIENTBOUND, GameState.STATUS, 0x00, StatusResponsePacket.class);
        this.registry.register(PacketDirection.CLIENTBOUND, GameState.STATUS, 0x01, PingResponsePacket.class);
    }
}
