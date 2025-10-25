package io.github.siloonk.protocol.packets;

import io.github.siloonk.protocol.ClientHandler;
import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.listeners.HandshakeListener;
import io.github.siloonk.protocol.listeners.configuration.ClientInformationListener;
import io.github.siloonk.protocol.listeners.login.LoginAcknowledgedListener;
import io.github.siloonk.protocol.listeners.login.LoginListener;
import io.github.siloonk.protocol.listeners.status.PingListener;
import io.github.siloonk.protocol.listeners.status.StatusListener;
import io.github.siloonk.protocol.packets.configuration.serverbound.ConfigurationClientInformationPacket;
import io.github.siloonk.protocol.packets.login.LoginAcknowledgedPacket;
import io.github.siloonk.protocol.packets.login.LoginStartPacket;
import io.github.siloonk.protocol.packets.status.PingRequestPacket;
import io.github.siloonk.protocol.packets.status.StatusRequestPacket;

import java.util.HashMap;
import java.util.Map;

public class PacketHandlerRegistry {

    private final Map<Class<? extends Packet>, PacketListener<? extends Packet>> handlers = new HashMap<>();

    private ClientHandler handler;

    public PacketHandlerRegistry(ClientHandler client) {
        this.handler = client;

        registerHandlers();
    }

    public <T extends Packet> void register(Class<T> packetClass, PacketListener<T> listener) {
        handlers.put(packetClass, listener);
    }

    @SuppressWarnings("unchecked")
    public void handle(Packet packet) {
        PacketListener<Packet> listener = (PacketListener<Packet>) handlers.get(packet.getClass());
        if (listener != null) {
            try {
                listener.handle(packet, handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void registerHandlers() {
        register(HandshakingPacket.class, new HandshakeListener());
        register(StatusRequestPacket.class, new StatusListener());
        register(PingRequestPacket.class, new PingListener());

        register(LoginStartPacket.class, new LoginListener());
        register(LoginAcknowledgedPacket.class, new LoginAcknowledgedListener());

        
        register(ConfigurationClientInformationPacket.class, new ClientInformationListener());
    }
}
