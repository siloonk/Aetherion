package io.github.siloonk.protocol.packets.status;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class StatusResponsePacket extends Packet {

    private String response;

    public StatusResponsePacket() {
        super(0x00, PacketDirection.CLIENTBOUND);
    }

    public StatusResponsePacket(String response) {
        this();
        this.response = response;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.response = in.readString();
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeString(response);
    }
}
