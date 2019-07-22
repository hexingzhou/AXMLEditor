package me.souleo.axml.content.res;

import java.io.IOException;
import java.io.PrintStream;

import me.souleo.axml.utils.IntReader;
import me.souleo.axml.utils.IntWriter;

public class StringChunk extends AXMLChunk {
  StringChunk() {
    super(CHUNK_TYPE);
  }

  int[] stringOffsets;
  int[] strings;
  int[] styleOffsets;
  int[] styles;

  @Override
  boolean checkChunkSize() throws IOException {
    return true;
  }

  @Override
  void readContent(IntReader reader) throws IOException {
    int stringCount = reader.readInt();
    int styleCount = reader.readInt();
    int unknown = reader.readInt();
    int stringPoolOffset = reader.readInt();
    int stylePoolOffset = reader.readInt();

    stringOffsets = reader.readIntArray(stringCount);
    if (styleCount != 0) {
      styleOffsets = reader.readIntArray(styleCount);
    }
    {
      int size =
          ((stylePoolOffset == 0) ? chunkSize : stylePoolOffset) - stringPoolOffset;
      if ((size % 4) != 0) {
        throw new IOException(
            "String data size is not multiple of 4 (" + size + ").");
      }
      strings = reader.readIntArray(size / 4);
    }
    if (stylePoolOffset != 0) {
      int size = (chunkSize - stylePoolOffset);
      if ((size % 4) != 0) {
        throw new IOException(
            "Style data size is not multiple of 4 (" + size + ").");
      }
      styles = reader.readIntArray(size / 4);
    }
  }

  @Override
  void writeContent(IntWriter writer) throws IOException {
    int stringCount = 0;
    int styleCount = 0;
    int stringPoolOffset = 0;
    int stylePoolOffset = 0;
    if (stringOffsets != null) stringCount = stringOffsets.length;
    if (styleOffsets != null) styleCount = styleOffsets.length;
    if (styles != null && styles.length > 0) {
      stylePoolOffset = chunkSize - styles.length * 4;
    }
    if (strings != null && strings.length > 0) {
      if (stylePoolOffset > 0) {
        stringPoolOffset = stylePoolOffset - strings.length * 4;
      } else {
        stringPoolOffset = chunkSize - strings.length * 4;
      }
    }
    writer.writeInt(stringCount);
    writer.writeInt(styleCount);
    writer.writeInt(0);
    writer.writeInt(stringPoolOffset);
    writer.writeInt(stylePoolOffset);
    if (stringOffsets != null && stringOffsets.length > 0) {
      writer.writeIntArray(stringOffsets, 0, stringCount);
    }
    if (styleOffsets != null && styleOffsets.length > 0) {
      writer.writeIntArray(styleOffsets, 0, styleCount);
    }
    if (strings != null && strings.length > 0) {
      writer.writeIntArray(strings, 0, strings.length);
    }
    if (styles != null && styles.length > 0) {
      writer.writeIntArray(styles, 0, styles.length);
    }
  }

  public String getString(int index) {
    if (index < 0
        || stringOffsets == null
        || index >= stringOffsets.length) {
      return null;
    }
    int offset = stringOffsets[index];
    int length = getShort(strings, offset);
    StringBuilder result = new StringBuilder(length);
    for (; length != 0; length -= 1) {
      offset += 2;
      result.append((char) getShort(strings, offset));
    }
    return result.toString();
  }

  private static int getShort(int[] array, int offset) {
    int value = array[offset / 4];
    if ((offset % 4) / 2 == 0) {
      return (value & 0xFFFF);
    } else {
      return (value >>> 16);
    }
  }

  @Override
  public void print(PrintStream stream) {
    super.print(stream);
    stream.println("\t-> StringChunk");
  }

  public static final int CHUNK_TYPE = 0x001C0001;
}
