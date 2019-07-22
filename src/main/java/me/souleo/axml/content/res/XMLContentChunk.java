package me.souleo.axml.content.res;

import java.io.IOException;
import me.souleo.axml.utils.IntReader;
import me.souleo.axml.utils.IntWriter;

abstract class XMLContentChunk extends AXMLChunk {

  XMLContentChunk(int type) {
    super(type);
  }

  int lineNumber;

  @Override
  boolean checkChunkSize() throws IOException {
    return true;
  }

  @Override
  final void readContent(IntReader reader) throws IOException {
    lineNumber = reader.readInt();
    int unknown = reader.readInt();
    readXMLContent(reader);
  }

  @Override
  final void writeContent(IntWriter writer) throws IOException {
    writer.writeInt(lineNumber);
    writer.writeInt(0xFFFFFFFF);
    writeXMLContent(writer);
  }

  abstract void readXMLContent(IntReader reader) throws IOException;
  abstract void writeXMLContent(IntWriter writer) throws IOException;
}
