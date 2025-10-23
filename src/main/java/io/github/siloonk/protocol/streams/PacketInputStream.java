package io.github.siloonk.protocol.streams;

import io.github.siloonk.protocol.GameState;
import io.github.siloonk.protocol.Packet;
import io.github.siloonk.protocol.PacketDirection;
import io.github.siloonk.protocol.PacketRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PacketInputStream extends InputStream {

    private static final int SEGMENT_BITS = 0x7F; // 0111 1111
    private static final int CONTINUE_BIT = 0x80; // 1000 0000

    private final InputStream in;

    public PacketInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    /**
     * Reads a single byte from the stream.
     */
    public byte readByte() throws IOException {
        int b = in.read();
        if (b == -1) throw new IOException("Unexpected end of stream");
        return (byte) b;
    }

    public short readShort() throws IOException {
        return (short) readPrimitive(2);
    }

    public int readInt() throws IOException {
        return (int) readPrimitive(4);
    }

    public long readLong() throws IOException {
        return readPrimitive(8);
    }

    public String readString() throws IOException {
        int length = readVarInt();
        byte[] text = in.readNBytes(length);
        return new String(text, StandardCharsets.UTF_8);
    }

    /**
     * Reads a primitive type (short/int/long) using big-endian order.
     *
     * @param byteCount number of bytes to read
     * @return the reconstructed value as a long
     */
    private long readPrimitive(int byteCount) throws IOException {
        long value = 0;
        for (int i = 0; i < byteCount; i++) {
            int b = readByte() & 0xFF;
            value = (value << 8) | b;
        }
        return value;
    }

    /**
     * Reads exactly buffer.length bytes from the stream into buffer.
     * Throws IOException if the stream ends prematurely.
     */
    private void readFully(byte[] buffer) throws IOException {
        int read, offset = 0;
        while (offset < buffer.length) {
            read = in.read(buffer, offset, buffer.length - offset);
            if (read == -1) throw new IOException("Unexpected end of stream while reading packet data");
            offset += read;
        }
    }

    public int getVarIntSize(int value) {
        int size = 0;
        do {
            value >>>= 7;
            size++;
        } while (value != 0);
        return size;
    }

    public Packet readPacket(PacketRegistry registry, PacketDirection direction, GameState state) throws IOException {
        int length = readVarInt();      // Total length of Packet ID + Data
        int packetId = readVarInt();    // Packet ID

        // Use state when creating the packet
        Packet packet = registry.create(direction, state, packetId);
        if (packet == null) {
            // Skip unknown packet data
            byte[] skip = new byte[length - getVarIntSize(packetId)];
            readFully(skip);
            return null;
        }

        // Let the packet parse its own data
        packet.read(this);
        return packet;
    }


    /**
     * Reads a variable-length encoded integer (VarInt).
     *
     * @return The decoded integer.
     * @throws IOException if the stream ends prematurely.
     */
    public int readVarInt() throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }

    /**
     * Reads a variable-length encoded long (VarLong).
     *
     * @return The decoded long value.
     * @throws IOException if the stream ends prematurely.
     */
    public long readVarLong() throws IOException {
        long value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 64) throw new RuntimeException("VarLong is too big");
        }

        return value;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
