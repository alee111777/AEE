/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

import edu.sfsu.evaluator.model.Entity;
import java.util.ArrayList;

/**
 *
 * @author eric
 */
public class MeasurementCalculate
{

    public static double calculateFMeasure(double precision, double recall)
    {
        return 2.0 * ((precision * recall) / (precision + recall));
    }

    public static double calculatePrecision(ArrayList<Entity> truth,
                                            ArrayList<Entity> test)
    {
        double truePositive = 0.0;
        double falsePositive = 0.0;
        for (Entity aTest
                : test)
        {
            boolean isFP = true; // Is false positive
            for (Entity aTruth
                    : truth)
            {
                if (aTruth.equals(aTest))
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

    public static double calculateRecall(ArrayList<Entity> truth,
                                         ArrayList<Entity> test)
    {
        double truePositive = 0.0;
        double falseNegative = 0.0;
        for (Entity aTruth
                : truth)
        {
            boolean isFN = true; // Is false negative
            for (Entity aTest
                    : test)
            {
                if (aTruth.equals(aTest))
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
