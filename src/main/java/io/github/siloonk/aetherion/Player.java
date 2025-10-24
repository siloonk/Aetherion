package io.github.siloonk.aetherion;

import io.github.siloonk.protocol.ClientHandler;

public class Player {

    // Name that's used when logging in
    // This name will also be used for commands etc...
    private String username;

    // The name that'll be shown in the tab and chat
    private String displayName;

    // The current position of the player
    private Location location;

    // The Client handler that is handling everything on the lower level
    private ClientHandler handler;

    public Player(String username, Location location, ClientHandler handler) {
        this.username = username;
        this.displayName = username;
        this.location = location;
        this.handler = handler;
    }

    public Player(String username, ClientHandler handler) {
        this.username = username;
        this.location = new Location(new World(), 0, 0, 0);
        this.displayName = username;
        this.handler = handler;
    }

    public ClientHandler getHandler() {
        return handler;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Location getLocation() {
        return location;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
