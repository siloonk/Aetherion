package io.github.siloonk.protocol.packets.configuration.clientbound;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


public class RegistryDataPacket extends Packet {

    private String registryId; // e.g. "minecraft:item"
    private final Map<String, JsonObject> entries = new LinkedHashMap<>();

    public RegistryDataPacket() {
        super(0x07, PacketDirection.CLIENTBOUND);
    }

    public RegistryDataPacket(String registryId, Map<String, JsonObject> entries) {
        this();
        this.registryId = registryId;
        this.entries.putAll(entries);
    }

    public String getRegistryId() {
        return registryId;
    }

    public Map<String, JsonObject> getEntries() {
        return entries;
    }

    @Override
    public void read(PacketInputStream in) throws IOException {
        registryId = in.readString();

        int entryCount = in.readVarInt();
        for (int i = 0; i < entryCount; i++) {
            String entryId = in.readString();
            boolean hasData = in.readBoolean();
            JsonObject data = null;
            if (hasData) {
                String json = in.readString();
                data = JsonParser.parseString(json).getAsJsonObject();
            }
            entries.put(entryId, data);
        }
    }

    @Override
    public void write(PacketOutputStream out) throws IOException {
        out.writeString(registryId);
        out.writeVarInt(entries.size());

        for (Map.Entry<String, JsonObject> entry : entries.entrySet()) {
            out.writeString(entry.getKey());
            JsonObject data = entry.getValue();
            if (data == null) {
                out.write(0);
            } else {
                out.write(1);
                out.writeString(data.toString()); // write as JSON text
            }
        }
    }
}
