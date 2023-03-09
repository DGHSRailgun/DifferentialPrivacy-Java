package org.example.Utils;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import org.example.Entity.Visit;
import org.example.Entity.VisitsForWeek;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.nio.charset.StandardCharsets.UTF_8;

/** Reads visitors' data and prints statistics. */
public class IOUtils {

  public static final String CSV_ITEM_SEPARATOR = ",";
  public static final DateTimeFormatter TIME_FORMATTER =
      new DateTimeFormatterBuilder()
          // case insensitive
          .parseCaseInsensitive()
          // pattern
          .appendPattern("h:mm:ss a")
          // set Locale that uses "AM" and "PM"
          .toFormatter(Locale.ENGLISH);
  public static final String CSV_HOUR_COUNT_WRITE_TEMPLATE = "%d,%d\n";
  public static final String CSV_DAY_COUNT_WRITE_TEMPLATE = "%s,%d\n";
  public static final String CSV_DAY_VARIANCE_WRITE_TEMPLATE = "%s,%f\n";

  public IOUtils() {}

  /**
   * Reads daily visitors' data.
   * {@see #convertCsvLineWithoutDayToList} for details on the format.
   */
  public static ImmutableSet<Visit> readDailyVisits(String file) {
    try {
      List<String> visitsAsText =
          Resources.readLines(Resources.getResource(file), UTF_8);

      return visitsAsText.stream()
          .skip(1)
          .map(IOUtils::convertLineToVisit)
          .collect(toImmutableSet());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Converts a line of format "visitorId,entryTime, minutesSpent, moneySpent, day" to
   * {@link Visit}.
   */
  public static Visit convertLineToVisit(String visitAsText) {
    Iterator<String> splitVisit = Splitter.on(CSV_ITEM_SEPARATOR).split(visitAsText).iterator();
    // element 0
    String visitorId = splitVisit.next();
    // element 1
    LocalTime timeEntered = LocalTime.parse(splitVisit.next(), TIME_FORMATTER);
    // element 2
    int timeSpent = Integer.parseInt(splitVisit.next());
    // element 3
    int moneySpent = Integer.parseInt(splitVisit.next());
    // element 4
    DayOfWeek day = DayOfWeek.of(Integer.parseInt(splitVisit.next()));

    return Visit.create(visitorId, timeEntered, timeSpent, moneySpent, day);
  }

  /**
   * Reads daily visitors' data. Assumes that the input file is a .csv file of format "visitorId,
   * entryTime, minutesSpent, moneySpent, day".
   */
  public static VisitsForWeek readWeeklyVisits(String file) {
    VisitsForWeek result = new VisitsForWeek();

    try {
      List<String> visitsAsText =
          Resources.readLines(Resources.getResource(file), UTF_8);
      visitsAsText.stream()
          .skip(1)
          .forEach(v -> {
            Visit visit = convertLineToVisit(v);
            result.addVisit(visit);
          });

    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

    return result;
  }

  public static void writeCountsPerHourOfDay(Map<Integer, Integer> counts, String file) {
    try (PrintWriter pw = new PrintWriter(new File(file), UTF_8.name())) {
      counts.forEach((
          hour, count) -> pw.write(String.format(CSV_HOUR_COUNT_WRITE_TEMPLATE, hour, count)));
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void writeCountsPerDayOfWeek(EnumMap<DayOfWeek, Integer> counts, String file) {
    try (PrintWriter pw = new PrintWriter(new File(file), UTF_8.name())) {
      counts.forEach(
          (day, count) -> pw.write(String.format(CSV_DAY_COUNT_WRITE_TEMPLATE, day.name(), count)));
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void writeVariancesPerDayOfWeek(EnumMap<DayOfWeek, Double> variances, String file) {
    try (PrintWriter pw = new PrintWriter(new File(file), UTF_8.name())) {
      variances.forEach(
              (day, variance) -> pw.write(String.format(CSV_DAY_VARIANCE_WRITE_TEMPLATE, day.name(), variance)));
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
