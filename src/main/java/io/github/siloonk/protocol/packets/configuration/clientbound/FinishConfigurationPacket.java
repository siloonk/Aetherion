package io.github.siloonk.protocol.packets.configuration.clientbound;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class FinishConfigurationPacket extends Packet {

    public FinishConfigurationPacket() {
        super(0x03, PacketDirection.CLIENTBOUND);
    }

    @Override
    public void read(PacketInputStream in) throws IOException {}

    @Override
    public void write(PacketOutputStream out) throws IOException {}
}
