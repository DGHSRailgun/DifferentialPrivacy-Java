package org.example.Utils;

import org.example.Entity.Visit;
import org.example.Entity.VisitsForWeek;

import java.time.DayOfWeek;
import java.util.*;

/**
 * Static utils that bound contributions on the input data.
 */
public class ContributionBoundingUtils {

  public ContributionBoundingUtils() { }

  /**
   * @return {@link VisitsForWeek} containing the restaurant visits where the number of days
   * contributed by a single visitor is limited to {@code maxContributedDays}.
   */
  public static VisitsForWeek boundContributedDays(VisitsForWeek visits, int maxContributedDays) {
    Map<String, Set<DayOfWeek>> boundedVisitorDays = new HashMap<>();
    List<Visit> allVisits = new ArrayList<>();
    VisitsForWeek boundedVisits = new VisitsForWeek();

    // Add all visits to a list in order to shuffle them.
    for (DayOfWeek d : DayOfWeek.values()) {
      Collection<Visit> visitsForDay = visits.getVisitsForDay(d);
      allVisits.addAll(visitsForDay);
    }
    Collections.shuffle(allVisits);

    // For each visitorId, copy their visits for at most maxContributedDays days to the result.
    for (Visit visit : allVisits) {
      String visitorId = visit.visitorId();
      DayOfWeek visitDay = visit.day();
      if (boundedVisitorDays.containsKey(visitorId)) {
        Set<DayOfWeek> visitorDays = boundedVisitorDays.get(visitorId);
        if (visitorDays.contains(visitDay)) {
          boundedVisits.addVisit(visit);
        } else if (visitorDays.size() < maxContributedDays) {
          visitorDays.add(visitDay);
          boundedVisits.addVisit(visit);
        }
      } else {
        Set<DayOfWeek> visitorDays = new HashSet<>();
        boundedVisitorDays.put(visitorId, visitorDays);
        visitorDays.add(visitDay);
        boundedVisits.addVisit(visit);
      }
    }

    return boundedVisits;
  }
}
