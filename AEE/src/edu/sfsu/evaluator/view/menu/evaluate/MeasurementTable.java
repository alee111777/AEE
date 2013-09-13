/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

import edu.sfsu.evaluator.EvaluatorViewModel;
import edu.sfsu.evaluator.exceptions.BadModelRequestException;
import edu.sfsu.evaluator.model.Entity;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * A JTable composed of precision, recall, and FMeasure for each document
 * version and each entity type.
 * @author Eric Chiang
 */
public class MeasurementTable extends JTable
{

    public MeasurementTable(EvaluatorViewModel viewModel,
                            String docName, String verName)
            throws BadModelRequestException
    {
        super();
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
        /**
         * One column for version name, one column for measurements, one column
         * for all entity types, and one column for every individual entity
         * type.
         *
         * Three rows for every document version which is not the baseline
         * (truth) version, and that version was just removed from verNames.
         */
        int numCols = 3 + entityTypeNames.size();
        int numRows = verNames.size() * 3;

        String[] columnNames = new String[numCols];
        columnNames[0] = "Version";
        columnNames[1] = "Measure";
        columnNames[2] = "All labels";
        for (int i = 0; i < entityTypeNames.size(); i++)
        {
            columnNames[3 + i] = entityTypeNames.get(i);
        }

        // Get table model to edit
        DefaultTableModel tableModel = (DefaultTableModel) this.getModel();


        Object[][] data = new Object[numRows][numCols];
        // Fill data one document version at a time
        for (int i = 0; i < verNames.size(); i++)
        {
            // Fill version and measurement column
            String vName = verNames.get(i);
            int startRow = (3 * i);
            data[startRow + 0][0] = "";
            data[startRow + 1][0] = vName;
            data[startRow + 2][0] = "";
            data[startRow + 0][1] = "Precision";
            data[startRow + 1][1] = "Recall";
            data[startRow + 2][1] = "FMeasure";

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

            // Enter measurements into data
            data[startRow + 0][2] = allPrecision;
            data[startRow + 1][2] = allRecall;
            data[startRow + 2][2] = allFMeasure;

            // For each entity type fill out that column
            for (int j = 0; j < entityTypeNames.size(); j++)
            {
                // Get the sub groups of entities for each entity type
                String entityTypeName = entityTypeNames.get(j);
                ArrayList<Entity> subTruthEntities =
                        getAnnotationSubset(truthEntities, entityTypeName);
                ArrayList<Entity> subTestEntities =
                        getAnnotationSubset(testEntities, entityTypeName);
                // Set column name to entity type name
                columnNames[3 + j] = entityTypeName;
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
                data[startRow + 0][3 + j] = precision;
                data[startRow + 1][3 + j] = recall;
                data[startRow + 2][3 + j] = fMeasure;
            }
        }
        tableModel.setDataVector(data, columnNames);
        this.repaint();
    }

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
}
