package org.example;

import org.example.Scenarios.*;

import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    if (args == null || args.length == 0) {
      throw new IllegalArgumentException(
          "The scenario should be set as a first argument. "
              + "Accepted values: "
              + Arrays.toString(Scenario.values()));
    }

    Scenario scenario = Scenario.valueOf(args[0]);

    switch (scenario) {
      case COUNT_VISITS_PER_HOUR:
        CountVisitsPerHour.run();
        break;
      case COUNT_VISITS_PER_DAY:
        CountVisitsPerDay.run();
        break;
      case SUM_REVENUE_PER_DAY:
        SumRevenuePerDay.run();
        break;
      case SUM_REVENUE_PER_DAY_WITH_PREAGGREGATION:
        SumRevenuePerDayWithPreAggregation.run();
        break;
      case CAL_VARIANCE_PER_DAY:
        CalculateBoundedVariance.run();
        break;
      case CAL_MEAN_PER_DAY:
        CalculateBoundedMean.run();
        break;
      case CAL_STANDARD_DEVIATION:
        CalculateStandardDeviation.run();
        break;
      case CAL_BOUNDED_QUANTILES:
        CalculateBoundedQuantiles.run();
        break;
    }
  }

  private Main() {}

  enum Scenario {
    COUNT_VISITS_PER_HOUR,
    COUNT_VISITS_PER_DAY,
    SUM_REVENUE_PER_DAY,
    SUM_REVENUE_PER_DAY_WITH_PREAGGREGATION,
    CAL_VARIANCE_PER_DAY,
    CAL_MEAN_PER_DAY,
    CAL_STANDARD_DEVIATION,
    CAL_BOUNDED_QUANTILES
  }
}
