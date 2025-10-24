package io.github.siloonk.protocol.packets.login;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;
import java.util.UUID;

public class LoginStartPacket extends Packet {

    private String username;
    private UUID uuid;

    public LoginStartPacket() {
        super(0x00, PacketDirection.SERVERBOUND);
    }

    public LoginStartPacket(String username, UUID uuid) {
        this();
        this.username = username;
        this.uuid = uuid;
    }



    @Override
    public void read(PacketInputStream in) throws IOException {
        this.username = in.readString();
        this.uuid = in.readUUID();
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeString(this.username);
        out.writeUUID(this.uuid);
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return uuid;
    }
}
