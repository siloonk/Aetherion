package io.github.siloonk.protocol.packets.status;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class PingResponsePacket extends Packet {

    private long payload;

    public PingResponsePacket() {
        super(0x01, PacketDirection.CLIENTBOUND);
    }

    public PingResponsePacket(long payload) {
        this();
        this.payload = payload;
    }


    @Override
    public void read(PacketInputStream in) throws IOException {
        this.payload = in.readLong();
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeLong(payload);
    }
}
