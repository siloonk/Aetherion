package io.github.siloonk;

import io.github.siloonk.aetherion.Player;
import io.github.siloonk.protocol.ClientHandler;
import io.github.siloonk.protocol.data.GameState;
import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.data.PacketRegistry;
import io.github.siloonk.protocol.packets.HandshakingPacket;
import io.github.siloonk.protocol.packets.configuration.clientbound.*;
import io.github.siloonk.protocol.packets.configuration.serverbound.ConfigurationClientInformationPacket;
import io.github.siloonk.protocol.packets.configuration.serverbound.ConfigurationFinishAcknowledgedPacket;
import io.github.siloonk.protocol.packets.login.*;
import io.github.siloonk.protocol.packets.status.PingRequestPacket;
import io.github.siloonk.protocol.packets.status.PingResponsePacket;
import io.github.siloonk.protocol.packets.status.StatusRequestPacket;
import io.github.siloonk.protocol.packets.status.StatusResponsePacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MinecraftServer {

    /**
     * Constants
     */
    public static final int MAX_PLAYERS = 50;
    public static final String MOTD = "Welcome to the Java Minecraft Server";
    public static final int PROTOCOL_VERSION = 773;
    public static final String VERSION_NAME = "1.21.10";


    private final ServerSocket serverSocket;

    private final HashMap<ClientHandler, Player> players = new HashMap<>();
    private final HashMap<ClientHandler, Player> pendingPlayers = new HashMap<>();

    private final PacketRegistry registry = new PacketRegistry();

    public MinecraftServer() {
        try {
            this.serverSocket = new ServerSocket(25565);
            registerPackets();
            Logger.info("Server has started!");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Something went wrong while starting the server!");
            throw new RuntimeException(e);
        }
    }

    public void start() {
        Logger.info("Listening for connections on port %d".formatted(this.serverSocket.getLocalPort()));
        try {
            while (!this.isClosed()) {
                Socket socket = serverSocket.accept();

                ClientHandler handler = new ClientHandler(socket, this);
                handler.start();
            }
        } catch (IOException e) {
            Logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return this.serverSocket.isClosed() || !this.serverSocket.isBound();
    }

    private void registerPackets() {

        /**
         * handshaking
         */
        this.registry.register(PacketDirection.SERVERBOUND, GameState.HANDSHAKING, 0x00, HandshakingPacket.class);

        /**
         * Status
         */
        this.registry.register(PacketDirection.SERVERBOUND, GameState.STATUS, 0x00, StatusRequestPacket.class);
        this.registry.register(PacketDirection.SERVERBOUND, GameState.STATUS, 0x01, PingRequestPacket.class);

        this.registry.register(PacketDirection.CLIENTBOUND, GameState.STATUS, 0x00, StatusResponsePacket.class);
        this.registry.register(PacketDirection.CLIENTBOUND, GameState.STATUS, 0x01, PingResponsePacket.class);

        /**
         * Login
         */
        this.registry.register(PacketDirection.SERVERBOUND, GameState.LOGIN, 0x00, LoginStartPacket.class);
        this.registry.register(PacketDirection.SERVERBOUND, GameState.LOGIN, 0x02, LoginPluginResponsePacket.class);
        this.registry.register(PacketDirection.SERVERBOUND, GameState.LOGIN, 0x03, LoginAcknowledgedPacket.class);

        this.registry.register(PacketDirection.CLIENTBOUND, GameState.LOGIN, 0x00, LoginDisconnectPacket.class);
        this.registry.register(PacketDirection.CLIENTBOUND, GameState.LOGIN, 0x02, LoginSuccessPacket.class);
        this.registry.register(PacketDirection.CLIENTBOUND, GameState.LOGIN, 0x04, LoginPluginResponsePacket.class);

        /**
         * Configuration
         */
        this.registry.register(PacketDirection.CLIENTBOUND, GameState.CONFIGURATION, 0x02, ConfigurationDisconnectPacket.class);
        this.registry.register(PacketDirection.CLIENTBOUND, GameState.CONFIGURATION, 0x03, FinishConfigurationPacket.class);
        this.registry.register(PacketDirection.CLIENTBOUND, GameState.CONFIGURATION, 0x04, ConfigurationKeepAlivePacket.class);
        this.registry.register(PacketDirection.CLIENTBOUND, GameState.CONFIGURATION, 0x05, ConfigurationPingPacket.class);
        this.registry.register(PacketDirection.CLIENTBOUND, GameState.CONFIGURATION, 0x06, ConfigurationResetChatPacket.class);
        this.registry.register(PacketDirection.CLIENTBOUND, GameState.CONFIGURATION, 0x07, RegistryDataPacket.class);

        this.registry.register(PacketDirection.SERVERBOUND, GameState.CONFIGURATION, 0x00, ConfigurationClientInformationPacket.class);
        this.registry.register(PacketDirection.SERVERBOUND, GameState.CONFIGURATION, 0x03, ConfigurationFinishAcknowledgedPacket.class);
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    /**
     * Send the disconnect packet to the player and close their socket
     * @param handler The client handler that should be disconnected
     * @throws IOException
     */
    public void disconnectPlayer(ClientHandler handler, String reason) throws IOException {
        if (handler.getGameState() == GameState.LOGIN) {
            handler.getOut().writePacket(new LoginDisconnectPacket(reason));
            handler.getOut().close();
            this.players.remove(handler);
        }
    }

    public PacketRegistry getPacketRegistry() {
        return registry;
    }


    public HashMap<ClientHandler, Player> getPendingPlayers() {
        return pendingPlayers;
    }

    public void addPlayer(Player player) {
        this.players.put(player.getHandler(), player);
    }
}
