package me.souleo.axml.content.res;

import java.io.PrintStream;

public class EndNamespaceChunk extends NamespaceChunk {

  EndNamespaceChunk() {
    super(CHUNK_TYPE);
  }

  @Override
  public void print(PrintStream stream) {
    super.print(stream);
    stream.println("\t-> EndNamespaceChunk");
  }

  public static final int CHUNK_TYPE = 0x00100101;
}
