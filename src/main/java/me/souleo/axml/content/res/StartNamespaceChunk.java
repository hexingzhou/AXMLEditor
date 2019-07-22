package me.souleo.axml.content.res;

import java.io.PrintStream;

public class StartNamespaceChunk extends NamespaceChunk {
  StartNamespaceChunk() {
    super(CHUNK_TYPE);
  }

  @Override
  public void print(PrintStream stream) {
    super.print(stream);
    stream.println("\t-> StartNamespaceChunk");
  }

  public static final int CHUNK_TYPE = 0x00100100;
}
