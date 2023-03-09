package org.example.Utils;

import org.example.Entity.Visit;

import java.util.Collection;

public class Calculator {

    public static double calSpentVariance(Collection<Visit> visits) {
        double val = 0.0;
        double sum = 0.0;
        double ave = 0.0;

        for(Visit v: visits) {
            sum += v.eurosSpent();
        }

        ave = sum / visits.size();

        for (Visit v: visits) {
            val += (v.eurosSpent() - ave) * (v.eurosSpent() + ave);
        }

        return val/ visits.size();
    }


}
