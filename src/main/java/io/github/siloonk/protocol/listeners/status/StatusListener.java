package io.github.siloonk.protocol.listeners.status;


import io.github.siloonk.MinecraftServer;
import io.github.siloonk.protocol.ClientHandler;
import io.github.siloonk.protocol.data.ServerListJson;
import io.github.siloonk.protocol.packets.PacketListener;
import io.github.siloonk.protocol.packets.status.StatusRequestPacket;
import io.github.siloonk.protocol.packets.status.StatusResponsePacket;

import java.io.IOException;

public class StatusListener implements PacketListener<StatusRequestPacket> {

    @Override
    public void handle(StatusRequestPacket packet, ClientHandler client) throws IOException {
        ServerListJson json = new ServerListJson()
                .setVersionName(MinecraftServer.VERSION_NAME)
                .setProtocol(MinecraftServer.PROTOCOL_VERSION)
                .setMaxPlayers(MinecraftServer.MAX_PLAYERS)
                .setOnlinePlayers(client.getServer().getPlayers().size())
                .setDescription(MinecraftServer.MOTD);

        client.getOut().writePacket(new StatusResponsePacket(json.toJsonString()));
    }
}