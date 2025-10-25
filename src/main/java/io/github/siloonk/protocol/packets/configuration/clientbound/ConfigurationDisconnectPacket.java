package io.github.siloonk.protocol.packets.configuration.clientbound;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class ConfigurationDisconnectPacket extends Packet {

    private String reason;

    public ConfigurationDisconnectPacket() {
        super(0x02, PacketDirection.CLIENTBOUND);
    }

    public ConfigurationDisconnectPacket(String reason) {
        this();
        this.reason = reason;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.reason = in.readString();
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeString(this.reason);
    }
}
