package io.github.siloonk.protocol.packets.configuration.serverbound;

import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;

public class ConfigurationClientInformationPacket extends Packet {

    private String locale;
    private byte viewDistance;
    private byte displayedSkinParts;
    private int mainHand;

    public ConfigurationClientInformationPacket() {
        super(0x00, PacketDirection.SERVERBOUND);
    }

    public ConfigurationClientInformationPacket(
            String locale,
            byte viewDistance,
            byte displayedSkinParts,
            int mainHand
    ) {
        this();
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.displayedSkinParts = displayedSkinParts;
        this.mainHand = mainHand;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.locale = in.readString();
        this.viewDistance = in.readByte();
        in.readVarInt();
        displayedSkinParts = in.readByte();
        this.mainHand = in.readVarInt();
        in.readBoolean();
        in.readBoolean();
        in.readVarInt();
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeString(locale);
        out.writeByte(viewDistance);
        // Enable chat mode
        out.writeVarInt(0);
        // Enable chat colors
        out.write(1);
        out.writeByte(displayedSkinParts);
        out.writeVarInt(mainHand);
        // Disable text filtering
        out.write(0);
        // Allow server listings
        out.write(1);
        // Particle Status to all
        out.writeVarInt(0);
    }
}
