package io.github.siloonk.protocol.packets.login;

import io.github.siloonk.datatypes.Tuple;
import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class LoginSuccessPacket extends Packet {

    private String username;
    private UUID uuid;

    private String texture;

    HashMap<String, Tuple<String, String>> properties = new HashMap<>();

    public LoginSuccessPacket() {
        super(0x02, PacketDirection.CLIENTBOUND);
    }

    /**
     *
     * @param username the username of the user
     * @param uuid UUID of the user
     * @param texture The skin texture string, if left empty it'll send no texture
     */
    public LoginSuccessPacket(String username, UUID uuid, String texture) {
        this();
        this.username = username;
        this.uuid = uuid;
        this.texture = texture;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        this.username = in.readString();
        this.uuid = in.readUUID();

        int length = in.readVarInt();
        for (int i = 0; i < length; i++) {
            String name = in.readString();
            String value = in.readString();
            String signature = null;

            if (in.readBoolean()) {
                signature = in.readString();
            }

            properties.put(name, new Tuple<>(value, signature));
        }
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeUUID(uuid);
        out.writeString(username);
        out.writeVarInt(0);

        // TODO: Implement the texture part
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTexture() {
        return texture;
    }

    public HashMap<String, Tuple<String, String>> getProperties() {
        return properties;
    }
}
