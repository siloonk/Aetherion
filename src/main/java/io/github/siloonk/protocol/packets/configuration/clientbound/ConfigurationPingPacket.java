package io.github.siloonk.protocol.packets.configuration.clientbound;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class ConfigurationPingPacket extends Packet {

    private int pingId;

    public ConfigurationPingPacket() {
        super(0x05, PacketDirection.CLIENTBOUND);
    }

    public ConfigurationPingPacket(int pingId) {
        this();
        this.pingId = pingId;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.pingId = in.readInt();
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeInt(pingId);
    }
}
