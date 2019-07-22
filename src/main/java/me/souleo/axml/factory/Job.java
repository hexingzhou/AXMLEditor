package me.souleo.axml.factory;

import java.io.IOException;

import me.souleo.axml.content.res.AXML;

public interface Job {
  void run(Chain chain) throws IOException;

  public interface Chain {
    AXML axml();

    void proceed(AXML axml) throws IOException;
  }
}
