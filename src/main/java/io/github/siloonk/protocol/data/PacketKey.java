package io.github.siloonk.protocol.data;

import java.util.Objects;

public class PacketKey {
    private final GameState state;
    private final int id;

    public PacketKey(GameState state, int id) {
        this.state = state;
        this.id = id;
    }

    public GameState getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PacketKey)) return false;
        PacketKey key = (PacketKey) o;
        return id == key.id && state == key.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, id);
    }
}

