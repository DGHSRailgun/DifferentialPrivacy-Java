package org.example.Entity;

import com.google.auto.value.AutoValue;

import java.time.DayOfWeek;
import java.time.LocalTime;

/** Stores data about single visit of a user to the restaurant. */
@AutoValue
public abstract class Visit {

  public static Visit create(
      String visitorId, LocalTime entryTime, int minutesSpent, Double eurosSpent, DayOfWeek day) {
    return new AutoValue_Visit(visitorId, entryTime, minutesSpent, eurosSpent, day);
  }

  public abstract String visitorId();

  public abstract LocalTime entryTime();

  public abstract int minutesSpent();

  public abstract Double eurosSpent();

  public abstract DayOfWeek day();
}
