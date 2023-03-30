package org.example.Scenarios;

import com.google.common.base.Stopwatch;
import com.google.privacy.differentialprivacy.BoundedMean;
import org.example.Entity.Visit;
import org.example.Entity.VisitsForWeek;
import org.example.Utils.Calculator;
import org.example.Utils.ContributionBoundingUtils;
import org.example.Utils.IOUtils;
import org.example.Utils.InputFilePath;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.concurrent.TimeUnit;

public class CalculateBoundedMean {

    private static final String NON_PRIVATE_OUTPUT = "non_private_bounded_mean_per_day.csv";
    private static final String PRIVATE_OUTPUT = "private_bounded_variance_mean_day.csv";
    private static final double LN_3 = Math.log(3);
    private static final int MAX_CONTRIBUTED_DAYS = 4;
    private static final int MAX_CONTRIBUTED_TIMES_PER_DAY = 1;
    private static final int MIN_EUROS_SPENT = 0;
    private static final int MAX_EUROS_SPENT = 50;

    private CalculateBoundedMean() {}

    public static void run() {
        VisitsForWeek visitsForWeek = IOUtils.readWeeklyVisits(InputFilePath.WEEK_STATISTICS);

        EnumMap<DayOfWeek, Double> nonPrivateMean = getNonPrivateMean(visitsForWeek);
        EnumMap<DayOfWeek, Double> privateMean = getPrivateMean(visitsForWeek);

        IOUtils.writeMeanPerDayOfWeek(nonPrivateMean, NON_PRIVATE_OUTPUT);
        IOUtils.writeMeanPerDayOfWeek(privateMean, PRIVATE_OUTPUT);
    }

    private static EnumMap<DayOfWeek, Double> getNonPrivateMean(VisitsForWeek visits) {
        EnumMap<DayOfWeek, Double> variancePerDay = new EnumMap<>(DayOfWeek.class);
        Arrays.stream(DayOfWeek.values()).forEach(d ->
                variancePerDay.put(d, Calculator.calSpentMean(visits.getVisitsForDay(d))));
        return variancePerDay;
    }

    private static EnumMap<DayOfWeek, Double> getPrivateMean(VisitsForWeek visits) {
        //Strat timer
        Stopwatch watch = Stopwatch.createStarted();

        EnumMap<DayOfWeek, Double> privateMeanPerDay = new EnumMap<>(DayOfWeek.class);

        VisitsForWeek boundedVisits =
                ContributionBoundingUtils.boundContributedDays(visits, MAX_CONTRIBUTED_DAYS);

        for (DayOfWeek d : DayOfWeek.values()) {
            BoundedMean dpMean =
                    BoundedMean.builder()
                            .epsilon(LN_3)
                            .maxPartitionsContributed(MAX_CONTRIBUTED_DAYS)
                            .maxContributionsPerPartition(MAX_CONTRIBUTED_TIMES_PER_DAY)
                            .lower(MIN_EUROS_SPENT)
                            .upper(MAX_EUROS_SPENT)
                            .build();

            for (Visit v : boundedVisits.getVisitsForDay(d)) {
                dpMean.addEntry(v.eurosSpent());
            }

            dpMean.computeConfidenceInterval(0.05);
            privateMeanPerDay.put(d, dpMean.computeResult());
        }

        //Write timer to file
        IOUtils.WriteAddNoiseTimer(String.valueOf(watch.stop().elapsed(TimeUnit.MILLISECONDS)), "CAL_MEAN_PER_DAY");

        return privateMeanPerDay;
    }






}
