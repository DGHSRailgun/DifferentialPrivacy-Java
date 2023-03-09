package org.example.Scenarios;

import com.google.common.collect.ImmutableSortedMap;
import com.google.privacy.differentialprivacy.BoundedSum;
import com.google.privacy.differentialprivacy.BoundedVariance;
import com.google.privacy.differentialprivacy.Count;
import org.example.Entity.Visit;
import org.example.Entity.VisitsForWeek;
import org.example.Utils.Calculator;
import org.example.Utils.ContributionBoundingUtils;
import org.example.Utils.IOUtils;
import org.example.Utils.InputFilePath;

import java.time.DayOfWeek;
import java.util.*;


public class CalculateBoundedVariance {

    private static final String NON_PRIVATE_OUTPUT = "non_private_bounded_variance_per_day.csv";
    private static final String PRIVATE_OUTPUT = "private_bounded_variance_per_day.csv";

    private static final double LN_3 = Math.log(3);

    private static final int MAX_CONTRIBUTED_DAYS = 4;

    private static final int MAX_CONTRIBUTED_TIMES_PER_DAY = 1;
    private static final int MIN_EUROS_SPENT = 0;
    private static final int MAX_EUROS_SPENT = 50;

    private CalculateBoundedVariance() {}

    public static void run() {
        VisitsForWeek visitsForWeek = IOUtils.readWeeklyVisits(InputFilePath.WEEK_STATISTICS);

        EnumMap<DayOfWeek, Double> nonPrivateVariance = getNonPrivateVariance(visitsForWeek);
        EnumMap<DayOfWeek, Double> privateVariance = getPrivateVariance(visitsForWeek);



        IOUtils.writeVariancesPerDayOfWeek(nonPrivateVariance, NON_PRIVATE_OUTPUT);
        IOUtils.writeVariancesPerDayOfWeek(privateVariance, PRIVATE_OUTPUT);
    }

    private static EnumMap<DayOfWeek, Double> getNonPrivateVariance(VisitsForWeek visits) {
        EnumMap<DayOfWeek, Double> variancePerDay = new EnumMap<>(DayOfWeek.class);
        Arrays.stream(DayOfWeek.values()).forEach(d ->
                variancePerDay.put(d, Calculator.calSpentVariance(visits.getVisitsForDay(d))));
        return variancePerDay;
    }

    private static EnumMap<DayOfWeek, Double> getPrivateVariance(VisitsForWeek visits) {
        EnumMap<DayOfWeek, Double> privateVariancePerDay = new EnumMap<>(DayOfWeek.class);

        VisitsForWeek boundedVisits =
                ContributionBoundingUtils.boundContributedDays(visits, MAX_CONTRIBUTED_DAYS);

        for (DayOfWeek d : DayOfWeek.values()) {
            BoundedVariance dpVariance =
                    BoundedVariance.builder()
                            .epsilon(LN_3)
                            .maxPartitionsContributed(MAX_CONTRIBUTED_DAYS)
                            .maxContributionsPerPartition(MAX_CONTRIBUTED_TIMES_PER_DAY)
                            .lower(MIN_EUROS_SPENT)
                            .upper(MAX_EUROS_SPENT)
                            .build();

            for (Visit v : boundedVisits.getVisitsForDay(d)) {
                dpVariance.addEntry(v.eurosSpent());
            }

            privateVariancePerDay.put(d, dpVariance.computeResult());
        }

        return privateVariancePerDay;
    }




}
