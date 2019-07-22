package me.souleo.axml.content.res;

import java.io.IOException;

import me.souleo.axml.utils.IntWriter;

public class AXMLWriter {

  private final IntWriter writer;

  public AXMLWriter(IntWriter writer) {
    this.writer = writer;
  }

  public void write(AXML axml) throws IOException {
    if (axml == null) {
      throw new IllegalArgumentException("AXML == null");
    }
    int chunkSize = 8;
    if (axml.stringChunk != null) chunkSize += axml.stringChunk.chunkSize;
    if (axml.resourceChunk != null) chunkSize += axml.resourceChunk.chunkSize;
    for (XMLContentChunk chunk : axml.contentChunks) {
      chunkSize += chunk.chunkSize;
    }
    writer.writeInt(CHUNK_AXML_FILE);
    writer.writeInt(chunkSize);
    if (axml.stringChunk != null) {
      axml.stringChunk.writeType(writer);
      axml.stringChunk.write(writer);
    }
    if (axml.resourceChunk != null) {
      axml.resourceChunk.writeType(writer);
      axml.resourceChunk.write(writer);
    }
    for (XMLContentChunk chunk : axml.contentChunks) {
      chunk.writeType(writer);
      chunk.write(writer);
    }
  }

  public void flush() {
    if (writer == null) {
      return;
    }
    writer.flush();
  }

  public void close() {
    if (writer == null) {
      return;
    }
    writer.close();
  }

  private static final int CHUNK_AXML_FILE = 0x00080003;
}
