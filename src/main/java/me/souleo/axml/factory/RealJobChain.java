package me.souleo.axml.factory;

import java.io.IOException;
import java.util.List;

import me.souleo.axml.content.res.AXML;

public final class RealJobChain implements Job.Chain {
  private final List<Job> jobs;
  private final int index;
  private final AXML axml;

  public RealJobChain(List<Job> jobs, int index, AXML axml) {
    this.jobs = jobs;
    this.index = index;
    this.axml = axml;
  }

  @Override
  public AXML axml() {
    return axml;
  }

  @Override
  public void proceed(AXML axml) throws IOException {
    if (index >= jobs.size()) {
      return;
    }
    RealJobChain next = new RealJobChain(jobs, index + 1, axml);
    Job job = jobs.get(index);
    job.run(next);
  }
}
