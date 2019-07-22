package me.souleo.axml.content.res;

import java.io.IOException;
import java.io.PrintStream;
import me.souleo.axml.utils.IntReader;
import me.souleo.axml.utils.IntWriter;

public abstract class AXMLChunk {
  AXMLChunk(int type) {
    chunkType = type;
  }

  int chunkType;
  int chunkSize;

  /**
   * Read without chunkType
   */
  void read(IntReader reader) throws IOException {
    chunkSize = reader.readInt();
    if (checkChunkSize()) {
      readContent(reader);
    }
  }

  /**
   * Write without chunkType
   */
  void write(IntWriter writer) throws IOException {
    writer.writeInt(chunkSize);
    writeContent(writer);
  }

  void writeType(IntWriter writer) throws IOException {
    writer.writeInt(chunkType);
  }

  public void print(PrintStream stream) {
    stream.println("Chunk type: 0x" + Integer.toHexString(
                     chunkType) + " size: " + chunkSize);
  }

  abstract boolean checkChunkSize() throws IOException;

  abstract void readContent(IntReader reader) throws IOException;
  abstract void writeContent(IntWriter writer) throws IOException;
}
