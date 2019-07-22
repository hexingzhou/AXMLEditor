package me.souleo.axml.content.res;

import java.io.IOException;

import me.souleo.axml.utils.IntReader;

public class AXMLReader extends ChunkReader {
  public AXMLReader(IntReader reader) {
    super(reader);
  }

  public AXML read() throws IOException {
    AXML.Builder builder = new AXML.Builder();
    AXMLChunk chunk;
    while ((chunk = next()) != null) {
      if (chunk instanceof XMLContentChunk) {
        builder.addContentChunk((XMLContentChunk) chunk);
        continue;
      }
      if (chunk instanceof StringChunk) {
        builder.stringChunk((StringChunk) chunk);
      }
      if (chunk instanceof ResourceChunk) {
        builder.resourceChunk((ResourceChunk) chunk);
      }
    }
    return builder.build();
  }
}
