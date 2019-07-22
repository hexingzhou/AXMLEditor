package me.souleo.axml.utils;

import java.io.IOException;

public class ChunkUtil {
  public static final void readCheckType(IntReader reader,
                                         int expectedType) throws IOException {
    int type = reader.readInt();
    if (type != expectedType) {
      throw new IOException(
              "Expected chunk of type 0x" + Integer.toHexString(expectedType)
              + ", read 0x" + Integer.toHexString(type) + ".");
    }
  }
}
