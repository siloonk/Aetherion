package io.github.siloonk.protocol.packets.login;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class LoginDisconnectPacket extends Packet {

    private String reason;

    public LoginDisconnectPacket() {
        super(0x00, PacketDirection.CLIENTBOUND);
    }

    public LoginDisconnectPacket(String reason) {
        this();
        this.reason = reason;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.reason = in.readString();
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeString(reason);
    }

    public String getReason() {
        return reason;
    }
}
