package io.github.siloonk.protocol.packets;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;
import java.io.NotActiveException;

public class HandshakingPacket extends Packet {

    private int protcolVersion;
    private String serverAddress;
    private short serverPort;
    private int intent;

    public HandshakingPacket() {
        super(0x00, PacketDirection.SERVERBOUND);
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.protcolVersion = in.readVarInt();       // protocol version
        this.serverAddress = in.readString();        // server address
        this.serverPort = in.readShort();            // server port
        this.intent = in.readVarInt();               // next state / intent
    }

    public int getProtcolVersion() {
        return protcolVersion;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public short getServerPort() {
        return serverPort;
    }

    public int getIntent() {
        return intent;
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        throw new NotActiveException();
    }
}
