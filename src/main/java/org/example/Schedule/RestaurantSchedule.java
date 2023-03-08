package org.example.Schedule;

import com.google.common.collect.Range;

public class RestaurantSchedule {
  /** An hour when visitors start entering the restaurant. */
  public static final int OPENING_HOUR = 9;
  /** An hour when visitors stop entering the restaurant. */
  public static final int CLOSING_HOUR = 20;
  /**For how many hours visitors can enter the restaurant. */
  static final int NUM_OF_WORK_HOURS = CLOSING_HOUR - OPENING_HOUR + 1;
  /** Range of valid work hours when a visitor can enter the restaurant. */
  public static final Range<Integer> VALID_HOURS = Range.closed(OPENING_HOUR, CLOSING_HOUR);

  private RestaurantSchedule() {}
}
