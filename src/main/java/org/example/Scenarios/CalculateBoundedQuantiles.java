package org.example.Scenarios;


import com.google.common.base.Stopwatch;
import com.google.privacy.differentialprivacy.BoundedQuantiles;
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

public class CalculateBoundedQuantiles {

    private static final String NON_PRIVATE_OUTPUT = "non_private_bounded_quantiles.csv";
    private static final String PRIVATE_OUTPUT = "private_bounded_quantiles.csv";
    private static final double LN_3 = Math.log(3);
    private static final int MAX_CONTRIBUTED_DAYS = 4;
    private static final int MAX_CONTRIBUTED_TIMES_PER_DAY = 3;
    private static final int MIN_EUROS_SPENT = 0;
    private static final int MAX_EUROS_SPENT = 350;
    private static final double RANK = 0.5;

    private CalculateBoundedQuantiles() {}

    public static void run() {
        VisitsForWeek visitsForWeek = IOUtils.readWeeklyVisits(InputFilePath.WEEK_STATISTICS);

        EnumMap<DayOfWeek, Double> nonPrivateQuantiles = getNonPrivateQuantiles(visitsForWeek);
        EnumMap<DayOfWeek, Double> privateQuantiles = getPrivateQuantiles(visitsForWeek);

        IOUtils.writeMeanPerDayOfWeek(nonPrivateQuantiles, NON_PRIVATE_OUTPUT);
        IOUtils.writeMeanPerDayOfWeek(privateQuantiles, PRIVATE_OUTPUT);
    }

    private static EnumMap<DayOfWeek, Double> getNonPrivateQuantiles(VisitsForWeek visits) {
        EnumMap<DayOfWeek, Double> bqPerDay = new EnumMap<>(DayOfWeek.class);
        Arrays.stream(DayOfWeek.values()).forEach(d ->
                bqPerDay.put(d, Calculator.findByRank(visits.getVisitsForDay(d), RANK)));
        return bqPerDay;
    }

    private static EnumMap<DayOfWeek, Double> getPrivateQuantiles(VisitsForWeek visits) {
        //Strat timer
        Stopwatch watch = Stopwatch.createStarted();

        EnumMap<DayOfWeek, Double> privateQuantile = new EnumMap<>(DayOfWeek.class);

        VisitsForWeek boundedVisits =
                ContributionBoundingUtils.boundContributedDays(visits, MAX_CONTRIBUTED_DAYS);

        for (DayOfWeek d : DayOfWeek.values()) {
            BoundedQuantiles dpQuantiles =
                    BoundedQuantiles.builder()
                            .epsilon(LN_3)
                            .maxPartitionsContributed(MAX_CONTRIBUTED_DAYS)
                            .maxContributionsPerPartition(MAX_CONTRIBUTED_TIMES_PER_DAY)
                            .lower(MIN_EUROS_SPENT)
                            .upper(MAX_EUROS_SPENT)
                            .treeHeight(10)
                            .branchingFactor(10)
                            .build();

            for (Visit v : boundedVisits.getVisitsForDay(d)) {
                dpQuantiles.addEntry(v.eurosSpent());
            }

            privateQuantile.put(d, dpQuantiles.computeResult(RANK));
        }

        //Write timer to file
        IOUtils.WriteAddNoiseTimer(String.valueOf(watch.stop().elapsed(TimeUnit.MILLISECONDS)), "CAL_BOUNDED_QUANTILES");

        return privateQuantile;
    }






}

