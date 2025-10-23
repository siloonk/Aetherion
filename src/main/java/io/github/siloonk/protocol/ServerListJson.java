package io.github.siloonk.protocol;

public class ServerListJson {

    private String versionName = "1.21.10";
    private int protocol = 773;
    private int maxPlayers = 20;
    private int onlinePlayers = 1;
    private String description = "Hello, world!";

    public ServerListJson() {}

    public ServerListJson setVersionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    public ServerListJson setProtocol(int protocol) {
        this.protocol = protocol;
        return this;
    }

    public ServerListJson setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public ServerListJson setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
        return this;
    }

    public ServerListJson setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the JSON string, properly escaped and ready for use in Java.
     */
    public String toJsonString() {
        return String.format(
                "{\"version\":{\"name\":\"%s\",\"protocol\":%d},\"players\":{\"max\":%d,\"online\":%d,\"sample\":[]},\"description\":{\"text\":\"%s\"},\"enforcesSecureChat\":false}",
                escapeJson(versionName),
                protocol,
                maxPlayers,
                onlinePlayers,
                escapeJson(description)
        );
    }

    /**
     * Escapes quotes and backslashes for safe JSON inclusion.
     */
    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
