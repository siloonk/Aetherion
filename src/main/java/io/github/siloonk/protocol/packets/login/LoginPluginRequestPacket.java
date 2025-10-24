package io.github.siloonk.protocol.packets.login;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class LoginPluginRequestPacket extends Packet {

    private String messageID;
    private String channel;
    private byte[] data;

    public LoginPluginRequestPacket() {
        super(0x04, PacketDirection.CLIENTBOUND);
    }

    public LoginPluginRequestPacket(String messageID, String channel, byte[] data) {
        this();
        this.messageID = messageID;
        this.channel = channel;
        this.data = data;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.messageID = in.readString();
        this.channel = in.readString();
        this.data = in.readAllBytes();
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeString(messageID);
        out.writeString(channel);
        out.write(data);
    }

    public String getMessageID() {
        return messageID;
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getData() {
        return data;
    }
}
