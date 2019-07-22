package me.souleo.axml.content.res;

import java.io.IOException;
import java.io.PrintStream;

import me.souleo.axml.utils.IntReader;
import me.souleo.axml.utils.IntWriter;

public class ResourceChunk extends AXMLChunk {
  ResourceChunk() {
    super(CHUNK_TYPE);
  }

  int[] m_resourceIDs;

  @Override
  boolean checkChunkSize() throws IOException {
    if (chunkSize < 8 || (chunkSize % 4) != 0) {
      throw new IOException("Invalid resource ids size (" + chunkSize + ").");
    }
    return true;
  }

  @Override
  void readContent(IntReader reader) throws IOException {
    m_resourceIDs = reader.readIntArray(chunkSize / 4 - 2);
  }

  @Override
  void writeContent(IntWriter writer) throws IOException {
    writer.writeIntArray(m_resourceIDs, 0, chunkSize / 4 - 2);
  }

  @Override
  public void print(PrintStream stream) {
    super.print(stream);
    stream.println("\t-> ResourceChunk");
  }

  public static final int CHUNK_TYPE = 0x00080180;
}
