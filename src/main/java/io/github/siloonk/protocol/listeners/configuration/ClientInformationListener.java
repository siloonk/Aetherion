package io.github.siloonk.protocol.listeners.configuration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.siloonk.Logger;
import io.github.siloonk.protocol.ClientHandler;
import io.github.siloonk.protocol.packets.PacketListener;
import io.github.siloonk.protocol.packets.configuration.clientbound.RegistryDataPacket;
import io.github.siloonk.protocol.packets.configuration.serverbound.ConfigurationClientInformationPacket;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientInformationListener implements PacketListener<ConfigurationClientInformationPacket> {

    @Override
    public void handle(ConfigurationClientInformationPacket packet, ClientHandler client) throws IOException {
        Logger.info("Sending the registry data!");
        // Read the registry file into a JSON object
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("registries.json");
        if (inputStream == null) {
            throw new IllegalStateException("Could not find registries.json in resources folder!");
        }

        JsonObject root = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

        // For each top-level registry
        for (Map.Entry<String, JsonElement> registryEntry : root.entrySet()) {
            String registryId = registryEntry.getKey();
            JsonObject entriesJson = registryEntry.getValue().getAsJsonObject();

            // Prepare entries map: entry ID → optional JSON data
            Map<String, JsonObject> entries = new LinkedHashMap<>();

            for (Map.Entry<String, JsonElement> entry : entriesJson.entrySet()) {
                String entryId = entry.getKey();
                JsonElement data = entry.getValue();

                JsonObject jsonData = null;
                if (data.isJsonObject() && !data.getAsJsonObject().entrySet().isEmpty()) {
                    jsonData = data.getAsJsonObject();
                }

                entries.put(entryId, jsonData);
            }

            // Create and send one packet per top-level registry
            RegistryDataPacket registryDataPacket = new RegistryDataPacket(registryId, entries);
            client.getOut().writePacket(registryDataPacket);
        }
    }
}
