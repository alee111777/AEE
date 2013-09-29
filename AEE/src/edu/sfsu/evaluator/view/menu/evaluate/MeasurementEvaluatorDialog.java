/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Just a simple class to pick a document version. Launches the
 * <code>MeasurementTableDialog</code> once that selection has been made.
 * @author Eric Chiang
 */
public class MeasurementEvaluatorDialog extends javax.swing.JDialog
{

    private EvaluatorViewModel viewModel;
    private EvaluatorController controller;
    private String nameOfSelectedDoc = null;

    /**
     * Creates new form MeasurementEvaluatorDialog
     */
    public MeasurementEvaluatorDialog(
            EvaluatorViewModel viewModel,
            EvaluatorController controller)
    {
        super(controller.getEvaluatorFrame(), true);
        this.viewModel = viewModel;
        this.controller = controller;
        initComponents();
        myInitComponents();
        this.setSize(500, 300);
    }
    
    /**
     * Creates new form MeasurementEvaluatorDialog with docName
     *  at the default setting for Document pull down menu
     */
    public MeasurementEvaluatorDialog(
            EvaluatorViewModel viewModel,
            EvaluatorController controller, String docName)
    {
        super(controller.getEvaluatorFrame(), true);
        this.viewModel = viewModel;
        this.controller = controller;
        initComponents();
        myInitComponents(docName);
        this.setSize(500, 300);
    }

    private void myInitComponents()
    {
        // Populate docComboBox with document names
        ArrayList<String> docNames = viewModel.getAvailableDocuments();
        Collections.sort(docNames);
        for (String docName : docNames)
        {
            docComboBox.addItem(docName);
        }
        docComboBox.repaint();
        populateVerComboBox();
        nameOfSelectedDoc = (String) docComboBox.getSelectedItem();
        docComboBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                String newDocName = (String) docComboBox.getSelectedItem();
                if (nameOfSelectedDoc.compareTo(newDocName) != 0)
                {
                    // Change value of verComboBox
                    nameOfSelectedDoc = newDocName;
                    populateVerComboBox();
                }
            }
        });
    }
    
    private void myInitComponents(String firstDoc)
    {
        // Populate docComboBox with document names
        ArrayList<String> docNames = viewModel.getAvailableDocuments();
        Collections.sort(docNames);
        docComboBox.addItem(firstDoc);
        

        
        for (String docName : docNames)
        {
            if (!docName.matches(firstDoc))
                docComboBox.addItem(docName);
        }
        docComboBox.repaint();
        populateVerComboBox();
        nameOfSelectedDoc = (String) docComboBox.getSelectedItem();
        docComboBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                String newDocName = (String) docComboBox.getSelectedItem();
                if (nameOfSelectedDoc.compareTo(newDocName) != 0)
                {
                    // Change value of verComboBox
                    nameOfSelectedDoc = newDocName;
                    populateVerComboBox();
                }
            }
        });
    }

    /**
     * As selected document is changed, the verComboBox must change with it.
     */
    private void populateVerComboBox()
    {
        verComboBox.removeAllItems();
        String docName = (String) docComboBox.getSelectedItem();
        ArrayList<String> verNames;
        try
        {
            verNames = viewModel.getAvailableDocumentVersions(docName);
        } catch (Exception e)
        {
            System.err.println("Bad ver name");
            dispose();
            return;
        }
        Collections.sort(verNames);
        for (String verName : verNames)
        {
            verComboBox.addItem(verName);
        }
        verComboBox.repaint();
    }

    /**
     * Display this dialog.
     * @param viewModel
     * @param controller
     */
    public static void showMeasurementEvaluatorDialog(
            EvaluatorViewModel viewModel,
            EvaluatorController controller)
    {
        MeasurementEvaluatorDialog eDialog =
                new MeasurementEvaluatorDialog(viewModel, controller);
        eDialog.setVisible(true);
    }
    
    
    /**
     * Display this dialog. with docName as the first document in
     * Documents drop down menu.
     * @param viewModel EvaluatorViewModel
     * @param controller EvaluatorController
     * @param docName String
     */
    public static void showMeasurementEvaluatorDialog(
            EvaluatorViewModel viewModel,
            EvaluatorController controller, String docName)
    {
        MeasurementEvaluatorDialog eDialog =
                new MeasurementEvaluatorDialog(viewModel, controller, docName);
        eDialog.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPasswordField1 = new javax.swing.JPasswordField();
        docLabel = new javax.swing.JLabel();
        verLabel = new javax.swing.JLabel();
        docComboBox = new javax.swing.JComboBox();
        verComboBox = new javax.swing.JComboBox();
        calculateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        mainLabel = new javax.swing.JLabel();

        jPasswordField1.setText("jPasswordField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        docLabel.setText("Document:");

        verLabel.setText("Baseline Version:");

        calculateButton.setText("Calculate");
        calculateButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                calculateButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }
        });

        mainLabel.setText("Calculate Annotation Measurements");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(mainLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(docLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(docComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(verLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(verComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(calculateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(67, 67, 67))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(mainLabel)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(docLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(docComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(verLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(verComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calculateButton)
                    .addComponent(cancelButton))
                .addGap(40, 40, 40))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_calculateButtonActionPerformed
    {//GEN-HEADEREND:event_calculateButtonActionPerformed
        String verName = (String) verComboBox.getSelectedItem();
        if (nameOfSelectedDoc == null || verName == null)
        {
            System.err.println("Null values...");
            return;
        }
        // Launch the measurement table dialog
        MeasurementTableDialog.showMeasurementTableDialog(viewModel, controller,
                                                          nameOfSelectedDoc, verName);
    }//GEN-LAST:event_calculateButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton calculateButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox docComboBox;
    private javax.swing.JLabel docLabel;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JLabel mainLabel;
    private javax.swing.JComboBox verComboBox;
    private javax.swing.JLabel verLabel;
    // End of variables declaration//GEN-END:variables
}
