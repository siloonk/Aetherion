package io.github.siloonk.protocol.packets.login;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class LoginCookieRequestPacket extends Packet {

    private String key;

    public LoginCookieRequestPacket() {
        super(0x05, PacketDirection.CLIENTBOUND);
    }

    public LoginCookieRequestPacket(String key) {
        this();
        this.key = key;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.key = in.readString();
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeString(key);
    }

    public String getKey() {
        return key;
    }
}
