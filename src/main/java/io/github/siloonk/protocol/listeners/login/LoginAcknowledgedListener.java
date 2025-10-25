package io.github.siloonk.protocol.listeners.login;

import io.github.siloonk.Logger;
import io.github.siloonk.aetherion.Player;
import io.github.siloonk.protocol.ClientHandler;
import io.github.siloonk.protocol.data.GameState;
import io.github.siloonk.protocol.packets.PacketListener;
import io.github.siloonk.protocol.packets.login.LoginAcknowledgedPacket;

import java.io.IOException;

public class LoginAcknowledgedListener implements PacketListener<LoginAcknowledgedPacket> {

    @Override
    public void handle(LoginAcknowledgedPacket packet, ClientHandler client) throws IOException {
        Player player = client.getServer().getPendingPlayers().remove(client);

        if (player == null) {
            client.getServer().disconnectPlayer(client, "No pending player found for this client!");
            return;
        }

        client.getServer().addPlayer(player);
        client.setState(GameState.CONFIGURATION);
        Logger.info("Adding %s to the player list!".formatted(player.getUsername()));
    }
}
