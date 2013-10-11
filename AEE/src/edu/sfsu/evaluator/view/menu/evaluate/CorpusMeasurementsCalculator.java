/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

import edu.sfsu.evaluator.EvaluatorModel;
import edu.sfsu.evaluator.model.AnnotatedDocument;
import edu.sfsu.evaluator.model.AnnotatedDocumentVersion;
import edu.sfsu.evaluator.model.Entity;
import java.util.ArrayList;

/**
 *
 * @author anthony
 */
public class CorpusMeasurementsCalculator
{
    private double truePositive, falsePositive, falseNegative,
            recall, precision, fMeasure;
    private String measurements;
    
    
    public CorpusMeasurementsCalculator() {
        truePositive = 0.0;
        falsePositive = 0.0;
        falseNegative = 0.0;
        recall = 0.0;
        precision = 0.0;
        fMeasure = 0.0;
    }
    
    public void calculate(ArrayList<AnnotatedDocument> documents) {
        
        AnnotatedDocumentVersion baseLineVersion;
        ArrayList<Entity> truthEntities;
        ArrayList<Entity> testEntities;
        for (AnnotatedDocument doc : documents) {
            ArrayList<AnnotatedDocumentVersion> versions;
            versions = doc.getVersions();
            baseLineVersion = doc.getBaseLineVersion();
            truthEntities = baseLineVersion.getEntities();
            for (AnnotatedDocumentVersion anoVersion : versions) {
                if (anoVersion != baseLineVersion) {
                    testEntities = anoVersion.getEntities();
                    calculate(truthEntities, testEntities);
                }
            }
        }
        
        recall = truePositive / (truePositive + falseNegative);
        precision = truePositive / (truePositive + falsePositive);
        fMeasure = 2.0 * ((precision * recall) / (precision + recall));
        
    }
    
    
     private void calculate(ArrayList<Entity> truthEntities,
                                            ArrayList<Entity> testEntities)
     {
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
             }
         }
     }
     
     public String getMeasurements() {
         measurements = "Corpus Measurements:\n\n"
                      + "                Reacall: " + recall + "\n\n"
                      + "              Precision: " + precision + "\n\n"
                      + "               Fmeasure: " + fMeasure;
         return measurements;
     }
     
     
}
