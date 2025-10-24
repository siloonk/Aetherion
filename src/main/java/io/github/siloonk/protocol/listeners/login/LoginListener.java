package io.github.siloonk.protocol.listeners.login;

import io.github.siloonk.Logger;
import io.github.siloonk.MinecraftServer;
import io.github.siloonk.aetherion.Player;
import io.github.siloonk.protocol.ClientHandler;
import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.packets.PacketListener;
import io.github.siloonk.protocol.packets.login.LoginStartPacket;
import io.github.siloonk.protocol.packets.login.LoginSuccessPacket;

import java.io.IOException;
import java.util.List;

public class LoginListener implements PacketListener<LoginStartPacket> {


    @Override
    public void handle(LoginStartPacket packet, ClientHandler client) throws IOException {
        Player player = new Player(packet.getUsername(), packet.getUuid(), client);

        List<Player> players = client.getServer().getPlayers();

        if (players.stream().anyMatch(p -> p.getUsername().equals(player.getUsername()))) {
            player.disconnect("A player with that name is already on the server!");
            return;
        }

        if (players.stream().anyMatch(p -> p.getUuid().equals(player.getUuid()))) {
            player.disconnect("You have already joined the server!");
            return;
        }

        if (players.size() > MinecraftServer.MAX_PLAYERS) {
            player.disconnect("The max amount of players has been reached!");
            return;
        }

        client.getOut().writePacket(new LoginSuccessPacket(player.getUsername(), player.getUuid(), null));
        Logger.info("Adding %s as a pending player!".formatted(player.getUsername()));
        client.getServer().getPendingPlayers().put(client, player);
    }
}
