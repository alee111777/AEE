/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * Measurement Table Dialog
 * @author Eric Chiang
 */
public class MeasurementTableDialog extends JDialog
{

    public MeasurementTableDialog(EvaluatorViewModel viewModel,
                                  EvaluatorController controller, String docName,
                                  String verName)
    {
        super(controller.getEvaluatorFrame(), true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle("Measurements");
        this.setSize(800, 400);

        Container pane = this.getContentPane();
        pane.setLayout(new GridBagLayout());
        MeasurementTable mt;
        // Generate measurement table
        try
        {
            mt = new MeasurementTable(viewModel, docName, verName);
            new NewMeasurementTable(viewModel, docName, verName);
        } catch (Exception e)
        {
            dispose();
            return;
        }

        // Add info labels
        JLabel infoLabel = new JLabel();
        infoLabel.setText(String.format("Measurements for document: '%s'",
                                        docName));

        JLabel baseLineLabel = new JLabel();
        baseLineLabel.setText(String.format("Baseline version: '%s'", verName));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridheight = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;

        pane.add(infoLabel, c);

        c.gridy = 1;
        pane.add(baseLineLabel, c);

        c.fill = GridBagConstraints.BOTH;

        c.gridy = 2;
        c.gridheight = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;

        pane.add(new JScrollPane(mt), c);

    }

    public static void showMeasurementTableDialog(
            EvaluatorViewModel viewModel,
            EvaluatorController controller,
            String docName, String verName)
    {
        MeasurementTableDialog tDialog =
                new MeasurementTableDialog(viewModel, controller, docName,
                                           verName);
        tDialog.setVisible(true);
    }
}
