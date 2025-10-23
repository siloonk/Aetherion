package io.github.siloonk.protocol.streams;

import io.github.siloonk.protocol.packets.Packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PacketOutputStream extends OutputStream {

    private static final int SEGMENT_BITS = 0x7F; // 0111 1111
    private static final int CONTINUE_BIT = 0x80; // 1000 0000

    private final OutputStream out;

    public PacketOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    /**
     * Writes a single byte to the stream.
     */
    public void writeByte(int value) throws IOException {
        out.write(value & 0xFF);
    }

    public void writeShort(short value) throws IOException {
        writePrimitive(value & 0xFFFF, 2);
    }

    public void writeInt(int value) throws IOException {
        writePrimitive(value, 4);
    }

    public void writeLong(long value) throws IOException {
        writePrimitive(value, 8);
    }

    public void writeString(String value) throws IOException {
        int length = value.length();
        writeVarInt(length);
        write(value.getBytes(StandardCharsets.UTF_8));
    }

    public void writePacket(Packet packet) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PacketOutputStream tempOut = new PacketOutputStream(buffer);

        tempOut.writeVarInt(packet.getId());
        packet.write(tempOut);
        tempOut.close();

        byte[] data = buffer.toByteArray();
        writeVarInt(data.length);
        write(data);
        flush();
    }


    /**
     * Writes a primitive (short/int/long) using big-endian byte order.
     */
    private void writePrimitive(long value, int byteCount) throws IOException {
        for (int i = byteCount - 1; i >= 0; i--) {
            int shift = i * 8;
            int b = (int) ((value >>> shift) & 0xFF);
            out.write(b);
        }
    }


    /**
     * Writes a variable-length integer (VarInt).
     *
     * @param value the integer to write
     * @throws IOException if an I/O error occurs
     */
    public void writeVarInt(int value) throws IOException {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                writeByte(value);
                return;
            }

            writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            value >>>= 7; // unsigned right shift
        }
    }

    /**
     * Writes a variable-length long (VarLong).
     *
     * @param value the long to write
     * @throws IOException if an I/O error occurs
     */
    public void writeVarLong(long value) throws IOException {
        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                writeByte((int) value);
                return;
            }

            writeByte((int) ((value & SEGMENT_BITS) | CONTINUE_BIT));
            value >>>= 7; // unsigned right shift
        }
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
