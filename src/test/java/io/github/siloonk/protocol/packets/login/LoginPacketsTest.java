package io.github.siloonk.protocol.packets.login;

import io.github.siloonk.protocol.data.PacketDirection;
import io.github.siloonk.protocol.streams.PacketInputStream;
import io.github.siloonk.protocol.streams.PacketOutputStream;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LoginPacketsTest {

    @Test
    void loginBasePacketIdsAndDirectionsAreSet() {
        assertEquals(0x00, new LoginDisconnectPacket().getId());
        assertEquals(PacketDirection.CLIENTBOUND, new LoginDisconnectPacket().getDirection());

        assertEquals(0x00, new LoginStartPacket().getId());
        assertEquals(PacketDirection.SERVERBOUND, new LoginStartPacket().getDirection());

        assertEquals(0x03, new LoginAcknowledgedPacket().getId());
        assertEquals(PacketDirection.SERVERBOUND, new LoginAcknowledgedPacket().getDirection());

        assertEquals(0x05, new LoginCookieRequestPacket().getId());
        assertEquals(PacketDirection.CLIENTBOUND, new LoginCookieRequestPacket().getDirection());

        assertEquals(0x04, new LoginCookieResponsePacket().getId());
        assertEquals(PacketDirection.SERVERBOUND, new LoginCookieResponsePacket().getDirection());
    }

    @Test
    void loginCookieRequestRoundTrips() throws IOException {
        LoginCookieRequestPacket packet = new LoginCookieRequestPacket("aetherion:session");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        packet.write(new PacketOutputStream(output));

        LoginCookieRequestPacket decoded = new LoginCookieRequestPacket();
        decoded.read(new PacketInputStream(new ByteArrayInputStream(output.toByteArray())));

        assertEquals("aetherion:session", decoded.getKey());
    }

    @Test
    void loginCookieResponseRoundTripsWithPayload() throws IOException {
        byte[] payload = new byte[]{1, 2, 3};
        LoginCookieResponsePacket packet = new LoginCookieResponsePacket("aetherion:session", payload);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        packet.write(new PacketOutputStream(output));

        LoginCookieResponsePacket decoded = new LoginCookieResponsePacket();
        decoded.read(new PacketInputStream(new ByteArrayInputStream(output.toByteArray())));

        assertEquals("aetherion:session", decoded.getKey());
        assertArrayEquals(payload, decoded.getPayload());
    }

    @Test
    void loginCookieResponseRoundTripsWithoutPayload() throws IOException {
        LoginCookieResponsePacket packet = new LoginCookieResponsePacket("aetherion:session", null);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        packet.write(new PacketOutputStream(output));

        LoginCookieResponsePacket decoded = new LoginCookieResponsePacket();
        decoded.read(new PacketInputStream(new ByteArrayInputStream(output.toByteArray())));

        assertEquals("aetherion:session", decoded.getKey());
        assertNull(decoded.getPayload());
    }
}
