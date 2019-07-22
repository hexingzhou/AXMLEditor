package me.souleo.axml.content.res;

import java.io.IOException;
import java.io.PrintStream;

import me.souleo.axml.utils.IntReader;
import me.souleo.axml.utils.IntWriter;

public class EndTagChunk extends TagChunk {
  EndTagChunk() {
    super(CHUNK_TYPE);
  }

  void readTagContent(IntReader reader) throws IOException {
  }

  @Override
  void writeTagContent(IntWriter writer) throws IOException {

  }

  public void print(PrintStream stream) {
    super.print(stream);
    stream.println("\t-> EndTagChunk");
  }

  public static final int CHUNK_TYPE = 0x00100103;
}
