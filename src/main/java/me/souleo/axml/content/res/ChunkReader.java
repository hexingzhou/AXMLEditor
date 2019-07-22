package me.souleo.axml.content.res;

import java.io.EOFException;
import java.io.IOException;

import me.souleo.axml.utils.IntReader;
import me.souleo.axml.utils.ChunkUtil;

public class ChunkReader {

  private final IntReader reader;

  private int chunkSize;
  private boolean opened = false;

  public ChunkReader(IntReader reader) {
    this.reader = reader;
  }

  public void open() throws IOException {
    ChunkUtil.readCheckType(reader, CHUNK_AXML_FILE);
    chunkSize = reader.readInt();
    opened = true;
  }

  public final AXMLChunk next() throws IOException {
    if (!opened) {
      throw new IllegalStateException("Reader should be open first!");
    }
    int chunkType;
    try {
      chunkType = reader.readInt();
    } catch (EOFException e) {
      return null;
    }
    AXMLChunk chunk;
    switch(chunkType) {
    case StringChunk.CHUNK_TYPE:
      chunk = new StringChunk();
      break;
    case ResourceChunk.CHUNK_TYPE:
      chunk = new ResourceChunk();
      break;
    case TextChunk.CHUNK_TYPE:
      chunk = new TextChunk();
      break;
    case StartTagChunk.CHUNK_TYPE:
      chunk = new StartTagChunk();
      break;
    case EndTagChunk.CHUNK_TYPE:
      chunk = new EndTagChunk();
      break;
    case StartNamespaceChunk.CHUNK_TYPE:
      chunk = new StartNamespaceChunk();
      break;
    case EndNamespaceChunk.CHUNK_TYPE:
      chunk = new EndNamespaceChunk();
      break;
    default:
      throw new IOException("Unknown chunk type 0x"
                            + Integer.toHexString(chunkType));
    }
    chunk.read(reader);
    return chunk;
  }

  public void close() {
    if (reader == null) {
      opened = false;
      return;
    }
    reader.close();
    opened = false;
  }

  private static final int CHUNK_AXML_FILE = 0x00080003;
}
