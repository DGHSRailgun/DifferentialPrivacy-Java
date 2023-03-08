package org.example.Entity;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;

/**
 * Stores {@link Visit}s for each {@link DayOfWeek}.
 */
public class VisitsForWeek {
  public final EnumMap<DayOfWeek, Collection<Visit>> visits;

  public VisitsForWeek() {
    visits = new EnumMap<>(DayOfWeek.class);
    Arrays.stream(DayOfWeek.values()).forEach(d -> visits.put(d, new HashSet<>()));
  }

  /**
   * Adds the given {@link Visit}.
   */
  public void addVisit(Visit visit) {
    visits.get(visit.day()).add(visit);
  }

  public Collection<Visit> getVisitsForDay(DayOfWeek day) {
    return visits.get(day);
  }
}
