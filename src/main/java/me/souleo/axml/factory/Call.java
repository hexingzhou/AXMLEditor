package me.souleo.axml.factory;

import java.io.IOException;

import me.souleo.axml.content.res.AXML;

public interface Call {
  AXML axml();
  void execute() throws IOException;

  interface Factory {
    Call call(AXML axml);
  }
}
