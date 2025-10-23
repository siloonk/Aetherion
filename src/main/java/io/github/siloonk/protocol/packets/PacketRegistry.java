package io.github.siloonk.protocol.packets;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

    private final Map<PacketKey, Class<? extends Packet>> clientboundPackets = new HashMap<>();
    private final Map<PacketKey, Class<? extends Packet>> serverboundPackets = new HashMap<>();

    public void register(PacketDirection direction, GameState state, int id, Class<? extends Packet> clazz) {
        PacketKey key = new PacketKey(state, id);
        if (direction == PacketDirection.CLIENTBOUND) {
            clientboundPackets.put(key, clazz);
        } else {
            serverboundPackets.put(key, clazz);
        }
    }

    public Packet create(PacketDirection direction, GameState state, int id) {
        PacketKey key = new PacketKey(state, id);
        Class<? extends Packet> clazz = (direction == PacketDirection.CLIENTBOUND)
                ? clientboundPackets.get(key)
                : serverboundPackets.get(key);

        if (clazz == null) return null;

        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate packet ID " + id + " in state " + state, e);
        }
    }
}