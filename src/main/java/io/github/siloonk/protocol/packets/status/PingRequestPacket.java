package io.github.siloonk.protocol.packets.status;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class PingRequestPacket extends Packet {

    private long payload;

    public PingRequestPacket() {
        super(0x01, PacketDirection.SERVERBOUND);
    }

    public PingRequestPacket(long payload) {
        this();
        this.payload = payload;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.payload = in.readLong();
    }

    public long getPayload() {
        return payload;
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeLong(this.payload);
    }
}
