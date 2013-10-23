/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

/**
 *
 * @author anthony
 */
public class MentionData
{
    private String name;
    private Double precision, recall,
                   fMeasure, falsePositive,
                   truePositive,  falseNegative;
    
    
    
    public MentionData(String newName) {
        name = newName;
        precision = 0.0; 
        recall = 0.0;
        fMeasure = 0.0; 
        falsePositive = 0.0;
        truePositive = 0.0;
        falseNegative = 0.0;
    }
    
    public String getName() { return name; }
    public Double getPrecision() { return precision; }
    public Double getRecall() { return recall; }
    public Double getFMeasure() { return fMeasure; }
    
    public void incrementTruePos() { ++truePositive; }
    public void incrementFalsePos() { ++falsePositive; }
    public void incrementFalseNeg() { ++falseNegative; }
    
    public void calculate() {
        recall = truePositive / (truePositive + falseNegative);
        precision = truePositive / (truePositive + falsePositive);
        fMeasure = 2.0 * ((precision * recall) / (precision + recall));
    }
    
}
