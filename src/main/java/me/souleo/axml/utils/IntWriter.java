package me.souleo.axml.utils;

import java.io.IOException;
import java.io.OutputStream;

public class IntWriter {

  private OutputStream stream;
  private boolean bigEndian;
  private int position;

  public IntWriter(OutputStream stream, boolean bigEndian) {
    reset(stream, bigEndian);
  }

  public final void reset(OutputStream stream, boolean bigEndian) {
    this.stream = stream;
    this.bigEndian = bigEndian;
    position = 0;
  }

  public final void flush() {
    if (stream == null) {
      return;
    }
    try {
      stream.flush();
    } catch (IOException e) {
    }
  }

  public final void close() {
    if (stream == null) {
      return;
    }
    try {
      stream.close();
    } catch (IOException e) {
    }
    reset(null, false);
  }

  public final void writeByte(byte value) throws IOException {
    writeInt(1, value);
  }

  public final void writeShort(short value) throws IOException {
    writeInt(2, value);
  }

  public final void writeInt(int value) throws IOException {
    writeInt(4, value);
  }

  public final void writeIntArray(int[] array, int offset, int length) throws IOException {
    for (int i = offset; i < length; i++) {
      writeInt(4, array[i]);
    }
  }

  private final void writeInt(int length, int value) throws IOException {
    if (length < 0 || length > 4) {
      throw new IllegalArgumentException();
    }
    byte[] bytes = new byte[length];
    if (bigEndian) {
      for (int i = length - 1; i >= 0; i--) {
        bytes[length - i - 1] = (byte) (value >>> (i * 8));
      }
      stream.write(bytes);
      position += length;
    } else {
      for (int i = 0; i < length; i++) {
        bytes[i] = (byte) (value >>> (i * 8));
      }
      stream.write(bytes);
      position += length;
    }
  }

  public final int getPosition() {
    return position;
  }
}
