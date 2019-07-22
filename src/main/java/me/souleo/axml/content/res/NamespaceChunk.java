package me.souleo.axml.content.res;

import java.io.IOException;
import me.souleo.axml.utils.IntReader;
import me.souleo.axml.utils.IntWriter;

abstract class NamespaceChunk extends XMLContentChunk {
  NamespaceChunk(int type) {
    super(type);
  }

  int prefix;
  int uri;

  @Override
  void readXMLContent(IntReader reader) throws IOException {
    prefix = reader.readInt();
    uri = reader.readInt();
  }

  @Override
  void writeXMLContent(IntWriter writer) throws IOException {
    writer.writeInt(prefix);
    writer.writeInt(uri);
  }
}
