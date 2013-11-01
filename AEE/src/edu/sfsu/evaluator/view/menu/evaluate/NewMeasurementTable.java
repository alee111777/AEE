/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

import edu.sfsu.evaluator.EvaluatorViewModel;
import edu.sfsu.evaluator.exceptions.BadModelRequestException;
import edu.sfsu.evaluator.model.Entity;
import edu.sfsu.evaluator.view.menu.evaluate.groupabletableheader.DocTableJDialog;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * A JTable composed of precision, recall, and FMeasure for each document
 * version and each entity type.
 * @author Anthony Lee
 */
public class NewMeasurementTable
{

    public NewMeasurementTable(EvaluatorViewModel viewModel,
                            String docName, String verName)
            throws BadModelRequestException
    {
        // Get the name of all entity types
        // Measurements will be calculated for each
        ArrayList<String> entityTypeNames =
                new ArrayList(viewModel.getEntityTypes().keySet());
        ArrayList<String> verNames =
                new ArrayList(viewModel.getAvailableDocumentVersions(docName));
        ArrayList<Entity> truthEntities =
                viewModel.getEntities(docName, verName);
        // Pull out truth version from version names
        for (String truthVerName : verNames)
        {
            if (truthVerName.compareTo(verName) == 0)
            {
                verNames.remove(truthVerName);
                break;
            }
        }
        
        Vector<String> docMentionTypeList = new Vector<String>();
        
        //get all mentions for all versions of this doc
        for (String version : verNames) {
            ArrayList<Entity> mentionList = truthEntities;
            for (Entity mention : mentionList) {
                if (!docMentionTypeList.contains(mention.getEntityType()))
                    docMentionTypeList.add(mention.getEntityType());
            }
        }
        
        Vector<Vector<String>> docData = new Vector<Vector<String>>();
        Vector<Vector<String>> mentionData = new Vector<Vector<String>>();
        
        // Fill data one document version at a time
        for (int i = 0; i < verNames.size(); i++)
        {
            Vector<String> versRow =  new Vector<String>();
            Vector<String> mentionRow = new Vector<String>();
            String vName = verNames.get(i);   
            versRow.add(vName);

            // Get entities relavent to this document version
            ArrayList<Entity> testEntities =
                    viewModel.getEntities(docName, vName);

            // Calculate measurements for entire set of annotations
            double allPrecision =
                    MeasurementCalculator.calculatePrecision(truthEntities,
                                                            testEntities);
            double allRecall =
                    MeasurementCalculator.calculateRecall(truthEntities,
                                                          testEntities);
            double allFMeasure =
                    MeasurementCalculator.calculateFMeasure(allPrecision,
                                                           allRecall);

            // Enter measurements into

            versRow.add(String.valueOf(allPrecision));
            versRow.add(String.valueOf(allRecall));
            versRow.add(String.valueOf(allFMeasure));
            // For each entity type fill out that column
            for (int j = 0; j < docMentionTypeList.size(); j++)
            {                                
                // Get the sub groups of entities for each entity type
                String entityTypeName = docMentionTypeList.get(j);
                ArrayList<Entity> subTruthEntities =
                        getAnnotationSubset(truthEntities, entityTypeName);
                ArrayList<Entity> subTestEntities =
                getAnnotationSubset(testEntities, entityTypeName);

                // Calculate measurements for sub group
                double precision =
                        MeasurementCalculator.calculatePrecision(
                        subTruthEntities,subTestEntities);
                double recall =
                        MeasurementCalculator.calculateRecall(
                        subTruthEntities,
                        subTestEntities);
                double fMeasure =
                        MeasurementCalculator.calculateFMeasure(precision,
                                                               recall);
                // insert measurements into table
                
                mentionRow.add(stringConverter(precision));
                mentionRow.add(stringConverter(recall));
                mentionRow.add(stringConverter(fMeasure));
                
            } //end of mention row loop
        
            docData.add(versRow);
            mentionData.add(mentionRow);
        } // end of version row loop
        
        new DocTableJDialog(docData, mentionData, docMentionTypeList);
       
    } // end of MeasurementTable()

    /**
     * Get entities which match the given entity type.
     * <p/>
     * @param entities
     * @param entityTypeName
     * @return
     */
    private static ArrayList<Entity> getAnnotationSubset(
            ArrayList<Entity> entities,
            String entityType)
    {
        ArrayList<Entity> matchingEntities = new ArrayList();
        for (Entity e : entities)
        {
            if (e.getEntityType().compareTo(entityType) == 0)
            {
                matchingEntities.add(e);
            }
        }
        return matchingEntities;

    }
    
    private String stringConverter(Double num) {
        String value = String.valueOf(num);
        if (value.compareTo("NaN") == 0) {
                value = " - ";
        }
        
        return value;        
    }
    
}// end of class
