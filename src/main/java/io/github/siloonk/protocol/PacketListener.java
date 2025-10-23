package io.github.siloonk.protocol;

import io.github.siloonk.Logger;
import io.github.siloonk.annotations.PacketHandler;
import io.github.siloonk.protocol.packets.HandshakingPacket;
import io.github.siloonk.protocol.packets.status.PingRequestPacket;
import io.github.siloonk.protocol.packets.status.PingResponsePacket;
import io.github.siloonk.protocol.packets.status.StatusRequestPacket;
import io.github.siloonk.protocol.packets.status.StatusResponsePacket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketListener {

    private ClientHandler client;

    public PacketListener(ClientHandler handler) {
        this.client = handler;
    }

    @PacketHandler
    public void onHandshake(HandshakingPacket packet) {
        if (packet.getIntent() == 1) client.setState(GameState.STATUS);
        else if (packet.getIntent() == 2) client.setState(GameState.LOGIN);
        else if (packet.getIntent() == 3) client.setState(GameState.TRANSFER);
        else {
            Logger.error(String.format("Found an invalid intent with value: %d", packet.getIntent()));
        }
    }

    @PacketHandler
    public void onStatusRequest(StatusRequestPacket packet) throws IOException {
        ServerListJson json = new ServerListJson()
                .setVersionName("1.21.10")
                .setProtocol(773)
                .setMaxPlayers(50)
                .setOnlinePlayers(0)
                .setDescription("Welcome to the Java Minecraft Server");


        client.getOut().writePacket(new StatusResponsePacket(json.toJsonString()));
    }

    @PacketHandler
    public void onPingRequest(PingRequestPacket packet) throws IOException {
        client.getOut().writePacket(new PingResponsePacket(packet.getPayload()));
    }


    public void handle(Packet packet) throws InvocationTargetException, IllegalAccessException {
        for (Method method : getClass().getMethods()) {
            if (method.isAnnotationPresent(PacketHandler.class)) {
                Class<?>[] params = method.getParameterTypes();

                if (params.length == 1 && params[0] == packet.getClass()) {
                    method.invoke(this, packet);
                }
            }
        }
    }

}
