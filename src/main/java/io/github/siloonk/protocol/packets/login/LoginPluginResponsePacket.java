package io.github.siloonk.protocol.packets.login;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class LoginPluginResponsePacket extends Packet {

    private String messageID;
    private byte[] data;

    public LoginPluginResponsePacket() {
        super(0x02, PacketDirection.SERVERBOUND);
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        messageID = in.readString();

        if (in.readBoolean()) {
            this.data = in.readAllBytes();
        }
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeString(messageID);
        out.write(this.data);
    }

    public String getMessageID() {
        return messageID;
    }

    public byte[] getData() {
        return data;
    }
}
