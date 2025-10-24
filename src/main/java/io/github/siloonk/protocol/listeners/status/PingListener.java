package io.github.siloonk.protocol.listeners.status;


import io.github.siloonk.protocol.ClientHandler;
import io.github.siloonk.protocol.packets.PacketListener;
import io.github.siloonk.protocol.packets.status.PingRequestPacket;
import io.github.siloonk.protocol.packets.status.PingResponsePacket;

import java.io.IOException;

public class PingListener implements PacketListener<PingRequestPacket> {


    @Override
    public void handle(PingRequestPacket packet, ClientHandler client) throws IOException {
        client.getOut().writePacket(new PingResponsePacket(packet.getPayload()));
    }
}