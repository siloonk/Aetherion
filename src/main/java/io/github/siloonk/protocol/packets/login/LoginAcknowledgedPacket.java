package io.github.siloonk.protocol.packets.login;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class LoginAcknowledgedPacket extends Packet {

    public LoginAcknowledgedPacket() {
        super(0x03, PacketDirection.SERVERBOUND);
    }

    @Override
    public void read(PacketInputStream in) throws IOException {}

    @Override
    public void write(PacketOutputStream out) throws IOException {}
}
