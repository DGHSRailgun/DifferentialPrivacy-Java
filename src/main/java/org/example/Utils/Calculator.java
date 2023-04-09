package org.example.Utils;

import org.example.Entity.Visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static double calSpentMean(Collection<Visit> visits) {
        double sum = 0.0;
        double ave = 0.0;

        for(Visit v: visits) {
            sum += v.eurosSpent();
        }

        ave = sum / visits.size();

        return ave;

    }

    //Find by rank
    public static double findByRank(Collection<Visit> visits, double rank) {
        List<Visit> visitList = new ArrayList<>(visits);
        for (Visit v: visitList){
            System.out.println(v.eurosSpent());
        }

        return (1-rank) * visitList.get(0).eurosSpent() + rank * visitList.get(visitList.size() - 1).eurosSpent();

    }




}
