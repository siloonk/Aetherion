package io.github.siloonk.protocol.packets;

import io.github.siloonk.protocol.ClientHandler;
import io.github.siloonk.protocol.Packet;

import java.io.IOException;

public interface PacketListener<T extends Packet> {

    void handle (T packet, ClientHandler client) throws IOException;
}
