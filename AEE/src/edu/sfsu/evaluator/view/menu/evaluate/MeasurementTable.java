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
 *
 * @author eric
 */
public class MeasurementTable extends JTable
{

    public MeasurementTable(EvaluatorViewModel viewModel,
                            String docName, String verName)
            throws BadModelRequestException
    {
        super();
        ArrayList<String> labelNames =
                new ArrayList(viewModel.getLabels().keySet());
        ArrayList<String> verNames =
                new ArrayList(viewModel.getAvailableDocumentVersions(docName));
        ArrayList<Entity> truthAnnos =
                viewModel.getEntities(docName, verName);
        for (String vName
                : verNames)
        {
            if (vName.compareTo(verName) == 0)
            {
                verNames.remove(vName);
                break;
            }
        }
        int numCols = 3 + labelNames.size();
        int numRows = verNames.size() * 3;

        String[] columnNames = new String[numCols];
        columnNames[0] = "Version";
        columnNames[1] = "Measure";
        columnNames[2] = "All labels";
        for (int i = 0;
                i < labelNames.size();
                i++)
        {
            columnNames[3 + i] = labelNames.get(i);
        }
        DefaultTableModel tableModel = (DefaultTableModel) this.getModel();

        Object[][] data = new Object[numRows][numCols];

        for (int i = 0;
                i < verNames.size();
                i++)
        {
            String vName = verNames.get(i);
            int startRow = (3 * i);
            data[startRow + 0][0] = "";
            data[startRow + 1][0] = vName;
            data[startRow + 2][0] = "";
            data[startRow + 0][1] = "Precision";
            data[startRow + 1][1] = "Recall";
            data[startRow + 2][1] = "FMeasure";

            ArrayList<Entity> testAnnos =
                    viewModel.getEntities(docName, vName);

            // Calculate measurements for entire set of annotations
            double allPrecision =
                    MeasurementCalculate.calculatePrecision(truthAnnos,
                                                            testAnnos);
            double allRecall =
                    MeasurementCalculate.calculateRecall(truthAnnos, testAnnos);
            double allFMeasure =
                    MeasurementCalculate.calculateFMeasure(allPrecision,
                                                           allRecall);

            data[startRow + 0][2] = allPrecision;
            data[startRow + 1][2] = allRecall;
            data[startRow + 2][2] = allFMeasure;

            for (int j = 0;
                    j < labelNames.size();
                    j++)
            {
                String label = labelNames.get(j);
                ArrayList<Entity> subTruthAnnos =
                        getAnnotationSubset(truthAnnos, label);
                ArrayList<Entity> subTestAnnos =
                        getAnnotationSubset(testAnnos, label);
                columnNames[3 + j] = label;
                double precision =
                        MeasurementCalculate.calculatePrecision(subTruthAnnos,
                                                                subTestAnnos);
                double recall =
                        MeasurementCalculate.calculateRecall(subTruthAnnos,
                                                             subTestAnnos);
                double fMeasure =
                        MeasurementCalculate.calculateFMeasure(precision,
                                                               recall);
                data[startRow + 0][3 + j] = precision;
                data[startRow + 1][3 + j] = recall;
                data[startRow + 2][3 + j] = fMeasure;
            }
        }
        tableModel.setDataVector(data, columnNames);
        this.repaint();
    }

    /**
     * Get annotations which match the given label.
     * <p/>
     * @param annotations
     * @param label
     * @return
     */
    private static ArrayList<Entity> getAnnotationSubset(
            ArrayList<Entity> annotations,
            String label)
    {
        ArrayList<Entity> returnAnnos = new ArrayList();
        for (Entity a
                : annotations)
        {
            if (a.getEntityType().compareTo(label) == 0)
            {
                returnAnnos.add(a);
            }
        }
        return returnAnnos;

    }
}
