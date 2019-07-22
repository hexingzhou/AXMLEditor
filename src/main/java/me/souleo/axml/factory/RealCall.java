package me.souleo.axml.factory;

import java.io.IOException;

import me.souleo.axml.Scheduler;
import me.souleo.axml.content.res.AXML;

public final class RealCall implements Call {
  private final Scheduler scheduler;
  private final AXML axml;
  private boolean executed = false;

  public static RealCall newCall(Scheduler scheduler, AXML axml) {
    RealCall call = new RealCall(scheduler, axml);
    return call;
  }

  private RealCall(Scheduler scheduler, AXML axml) {
    this.scheduler = scheduler;
    this.axml = axml;
  }

  @Override
  public AXML axml() {
    return axml;
  }

  @Override
  public void execute() throws IOException {
    synchronized (this) {
      if (executed) {
        throw new IllegalStateException("Already Executed");
      }
      executed = true;
    }
    proceedJobChain();
  }

  private void proceedJobChain() throws IOException {
    Job.Chain chain = new RealJobChain(scheduler.jobs(), 0, axml);
    chain.proceed(axml);
  }
}
