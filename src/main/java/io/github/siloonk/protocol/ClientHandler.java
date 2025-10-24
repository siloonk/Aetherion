package io.github.siloonk.protocol;

import io.github.siloonk.Logger;
import io.github.siloonk.MinecraftServer;
import io.github.siloonk.protocol.data.GameState;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private final Socket socket;
    private final PacketInputStream in;
    private final PacketOutputStream out;

    private final MinecraftServer server;
    private final PacketListener listener;

    private GameState state = GameState.HANDSHAKING;

    public ClientHandler(Socket socket, MinecraftServer server) {
        try {
            this.socket = socket;
            this.in = new PacketInputStream(socket.getInputStream());
            this.out = new PacketOutputStream(socket.getOutputStream());
            this.server = server;

            this.listener = new PacketListener(this);


            Logger.info("Clienthandler initialized correctly!");
        } catch (IOException e) {
            Logger.error("Something went wrong trying to initialize the client!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        start();
    }

    @Override
    public void start() {
        try {
            while (!isClosed()) {
                // Get the packet data
                Packet packet = in.readPacket(server.getPacketRegistry(), PacketDirection.SERVERBOUND, state);
                if (packet != null) {
                    this.listener.handle(packet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Something went wrong trying to communicate with the client!");
            try {
                this.socket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public boolean isClosed() {
        return socket.isClosed() || !socket.isConnected();
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public PacketOutputStream getOut() {
        return out;
    }
}
