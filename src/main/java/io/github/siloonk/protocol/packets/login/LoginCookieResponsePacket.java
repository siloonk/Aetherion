package io.github.siloonk.protocol.packets.login;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class LoginCookieResponsePacket extends Packet {

    private String key;
    private byte[] payload;

    public LoginCookieResponsePacket() {
        super(0x04, PacketDirection.SERVERBOUND);
    }

    public LoginCookieResponsePacket(String key, byte[] payload) {
        this();
        this.key = key;
        this.payload = payload;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.key = in.readString();
        if (in.readBoolean()) {
            this.payload = in.readAllBytes();
        }
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeString(key);
        out.writeByte(payload != null ? 1 : 0);
        if (payload != null) {
            out.write(payload);
        }
    }

    public String getKey() {
        return key;
    }

    public byte[] getPayload() {
        return payload;
    }
}
