package io.github.siloonk.protocol.listeners;

import io.github.siloonk.Logger;
import io.github.siloonk.protocol.ClientHandler;
import io.github.siloonk.protocol.data.GameState;
import io.github.siloonk.protocol.packets.HandshakingPacket;
import io.github.siloonk.protocol.packets.PacketListener;

public class HandshakeListener implements PacketListener<HandshakingPacket> {

    @Override
    public void handle(HandshakingPacket packet, ClientHandler client) {
        switch (packet.getIntent()) {
            case 1 -> client.setState(GameState.STATUS);
            case 2 -> client.setState(GameState.LOGIN);
            case 3 -> client.setState(GameState.TRANSFER);
            default -> Logger.error("Found an invalid intent with value: " + packet.getIntent());
        }
    }
}
