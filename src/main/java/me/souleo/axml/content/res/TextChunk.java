package me.souleo.axml.content.res;

import java.io.IOException;
import java.io.PrintStream;

import me.souleo.axml.utils.IntReader;
import me.souleo.axml.utils.IntWriter;

public class TextChunk extends XMLContentChunk {

  TextChunk() {
    super(CHUNK_TYPE);
  }

  int name;

  @Override
  void readXMLContent(IntReader reader) throws IOException {
    name = reader.readInt();
    int unknown_1 = reader.readInt();
    int unknown_2 = reader.readInt();
  }

  @Override
  void writeXMLContent(IntWriter writer) throws IOException {
    writer.writeInt(name);
    writer.writeInt(0xFFFFFFFF);
    writer.writeInt(0xFFFFFFFF);
  }

  @Override
  public void print(PrintStream stream) {
    super.print(stream);
    stream.println("\t-> TextChunk");
  }

  public static final int CHUNK_TYPE = 0x00100104;
}
