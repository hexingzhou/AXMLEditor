package me.souleo.axml;

import java.util.ArrayList;
import java.util.List;

import me.souleo.axml.factory.Call;
import me.souleo.axml.factory.Job;
import me.souleo.axml.factory.RealCall;
import me.souleo.axml.content.res.AXML;

public final class Scheduler implements Call.Factory {
  private final List<Job> jobs;

  public Scheduler() {
    this(new Builder());
  }

  Scheduler(Builder builder) {
    this.jobs = builder.jobs;
  }

  public List<Job> jobs() {
    return jobs;
  }

  public static final class Builder {
    final List<Job> jobs = new ArrayList<>();

    public Builder addJob(Job job) {
      if (job == null) {
        throw new IllegalArgumentException("job == null");
      }
      jobs.add(job);
      return this;
    }

    public Scheduler build() {
      return new Scheduler(this);
    }
  }

  public Call call(AXML axml) {
    return RealCall.newCall(this, axml);
  }
}
