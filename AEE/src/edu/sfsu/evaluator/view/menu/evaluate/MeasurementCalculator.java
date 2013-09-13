/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

import edu.sfsu.evaluator.model.Entity;
import java.util.ArrayList;

/**
 * Module to calculate precision, recall, and FMeasure
 * @author Eric Chiang
 */
public class MeasurementCalculator
{

    /**
     * Calculate FMeasure using beta of 1.
     * @param precision
     * @param recall
     * @return
     */
    public static double calculateFMeasure(double precision, double recall)
    {
        return 2.0 * ((precision * recall) / (precision + recall));
    }

    /**
     * Calculate FMeasure
     * @param precision
     * @param recall
     * @param beta
     * @return
     */
    public static double calculateFMeasure(double precision,
                                           double recall,
                                           double beta)
    {
        double betaSquared = beta * beta;
        return (1 + betaSquared) *
                ((precision * recall) / ((precision * betaSquared) + recall));
    }

    /**
     * Calculate precision. No consideration for partial matches.
     * @param truthEntities
     * @param testEntities
     * @return
     */
    public static double calculatePrecision(ArrayList<Entity> truthEntities,
                                            ArrayList<Entity> testEntities)
    {
        double truePositive = 0.0;
        double falsePositive = 0.0;
        for (Entity testEntity : testEntities)
        {
            boolean isFP = true; // Is false positive
            for (Entity truthEntity : truthEntities)
            {
                if (truthEntity.equals(testEntity))
                {
                    isFP = false;
                    break;
                }
            }
            if (isFP)
            {
                falsePositive = falsePositive + 1.0;
            } else
            {
                truePositive = truePositive + 1.0;
            }
        }
        return truePositive / (truePositive + falsePositive);
    }

    /**
     * Calculate recall. No consideration for partial matches.
     * @param truthEntities
     * @param testEntities
     * @return
     */
    public static double calculateRecall(ArrayList<Entity> truthEntities,
                                         ArrayList<Entity> testEntities)
    {
        double truePositive = 0.0;
        double falseNegative = 0.0;
        for (Entity truthEntity : truthEntities)
        {
            boolean isFN = true; // Is false negative
            for (Entity testEntity : testEntities)
            {
                if (truthEntity.equals(testEntity))
                {
                    isFN = false;
                    break;
                }
            }
            if (isFN)
            {
                falseNegative = falseNegative + 1.0;
            } else
            {
                truePositive = truePositive + 1.0;
            }

        }
        return truePositive / (truePositive + falseNegative);
    }
}
