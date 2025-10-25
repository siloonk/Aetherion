package io.github.siloonk.protocol.packets.configuration.clientbound;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class ConfigurationKeepAlivePacket extends Packet {

    private long keepAliveID;

    public ConfigurationKeepAlivePacket() {
        super(0x04, PacketDirection.CLIENTBOUND);
    }

    public ConfigurationKeepAlivePacket(long keepAliveID) {
        this();
        this.keepAliveID = keepAliveID;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.keepAliveID = in.readLong();
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeLong(this.keepAliveID);
    }
}
