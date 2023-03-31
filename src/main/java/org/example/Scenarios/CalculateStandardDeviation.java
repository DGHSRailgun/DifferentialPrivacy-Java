package org.example.Scenarios;

import com.google.common.base.Stopwatch;
import com.google.privacy.differentialprivacy.StandardDeviation;
import org.example.Entity.Visit;
import org.example.Entity.VisitsForWeek;
import org.example.Utils.Calculator;
import org.example.Utils.ContributionBoundingUtils;
import org.example.Utils.IOUtils;
import org.example.Utils.InputFilePath;

import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class CalculateStandardDeviation {

    private static final String NON_PRIVATE_OUTPUT = "non_private_standard_deviation_per_day.csv";
    private static final String PRIVATE_OUTPUT = "private_standard_deviation_per_day.csv";

    private static final double LN_3 = Math.log(3);

    private static final int MAX_CONTRIBUTED_DAYS = 4;

    private static final int MAX_CONTRIBUTED_TIMES_PER_DAY = 3;
    private static final int MIN_EUROS_SPENT = 0;
    private static final int MAX_EUROS_SPENT = 350;

    private CalculateStandardDeviation() {}

    public static void run() {
        VisitsForWeek visitsForWeek = IOUtils.readWeeklyVisits(InputFilePath.WEEK_STATISTICS);

        EnumMap<DayOfWeek, Double> nonPrivateSD = getNonPrivateSD(visitsForWeek);
        EnumMap<DayOfWeek, Double> privateSD = getPrivateSD(visitsForWeek);



        IOUtils.writeStandardDeviationPerDayOfWeek(nonPrivateSD, NON_PRIVATE_OUTPUT);
        IOUtils.writeStandardDeviationPerDayOfWeek(privateSD, PRIVATE_OUTPUT);
    }

    private static EnumMap<DayOfWeek, Double> getNonPrivateSD(VisitsForWeek visits) {
        EnumMap<DayOfWeek, Double> SDPerDay = new EnumMap<>(DayOfWeek.class);
        Arrays.stream(DayOfWeek.values()).forEach(d ->
                SDPerDay.put(d, Math.sqrt(Calculator.calSpentVariance(visits.getVisitsForDay(d)))));
        return SDPerDay;
    }

    private static EnumMap<DayOfWeek, Double> getPrivateSD(VisitsForWeek visits) {
        //Strat timer
        Stopwatch watch = Stopwatch.createStarted();

        EnumMap<DayOfWeek, Double> privateVariancePerDay = new EnumMap<>(DayOfWeek.class);

        VisitsForWeek boundedVisits =
                ContributionBoundingUtils.boundContributedDays(visits, MAX_CONTRIBUTED_DAYS);

        for (DayOfWeek d : DayOfWeek.values()) {
            StandardDeviation SD =
                    StandardDeviation.builder()
                            .epsilon(LN_3)
                            .maxPartitionsContributed(MAX_CONTRIBUTED_DAYS)
                            .maxContributionsPerPartition(MAX_CONTRIBUTED_TIMES_PER_DAY)
                            .lower(MIN_EUROS_SPENT)
                            .upper(MAX_EUROS_SPENT)
                            .build();

            for (Visit v : boundedVisits.getVisitsForDay(d)) {
                SD.addEntry(v.eurosSpent());
            }

            privateVariancePerDay.put(d, SD.computeResult());
        }

        //Write timer to file
        IOUtils.WriteAddNoiseTimer(String.valueOf(watch.stop().elapsed(TimeUnit.MILLISECONDS)), "CAL_VARIANCE_PER_DAY");


        return privateVariancePerDay;
    }




}
