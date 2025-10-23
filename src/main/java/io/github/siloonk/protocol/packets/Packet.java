package io.github.siloonk.protocol.packets;

import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public abstract class Packet {
    private final int id;
    private final PacketDirection direction;

    protected Packet(int id, PacketDirection direction) {
        this.id = id;
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public PacketDirection getDirection() {
        return direction;
    }

    public abstract void read(PacketInputStream in) throws IOException;
    public abstract void write(PacketOutputStream out) throws IOException;
}

