package me.souleo.axml.content.res;

import java.io.IOException;

import me.souleo.axml.utils.IntReader;
import me.souleo.axml.utils.IntWriter;

abstract class TagChunk extends XMLContentChunk {
  TagChunk(int type) {
    super(type);
  }

  int namespaceUri;
  int name;

  @Override
  void readXMLContent(IntReader reader) throws IOException {
    namespaceUri = reader.readInt();
    name = reader.readInt();
    readTagContent(reader);
  }

  @Override
  void writeXMLContent(IntWriter writer) throws IOException {
    writer.writeInt(namespaceUri);
    writer.writeInt(name);
    writeTagContent(writer);
  }

  public String getName(StringChunk chunk) {
    if (name == -1) {
      return null;
    }
    if (chunk == null) {
      return null;
    }
    return chunk.getString(name);
  }

  abstract void readTagContent(IntReader reader) throws IOException;
  abstract void writeTagContent(IntWriter writer) throws IOException;
}
