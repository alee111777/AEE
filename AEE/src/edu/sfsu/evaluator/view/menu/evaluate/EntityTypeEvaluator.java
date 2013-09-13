/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import edu.sfsu.evaluator.model.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author eric
 */
public class EntityTypeEvaluator extends javax.swing.JDialog
{

    private EvaluatorViewModel viewModel;
    private EvaluatorController controller;

    /**
     * Creates new form EntityTypeEvaluator
     */
    public EntityTypeEvaluator(
            EvaluatorViewModel viewModel,
            EvaluatorController controller)
    {
        super(controller.getEvaluatorFrame(), true);

        this.viewModel = viewModel;
        this.controller = controller;
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(1000, 500);
        this.setTitle("Label Info");
        initComponents();

        ArrayList<String> docNames = viewModel.getAvailableDocuments();
        Collections.sort(docNames);
        for (String docName : docNames)
        {
            docComboBox.addItem(docName);
        }
    }

    private void populateEntityTypeInfoTable(String docName) throws Exception
    {
        ArrayList<String> verNames =
                viewModel.getAvailableDocumentVersions(docName);
        entityTypeTabbedPane.removeAll();
        ArrayList<String> labels =
                new ArrayList(viewModel.getEntityTypes().keySet());
        Collections.sort(labels);
        for (String label : labels)
        {
            HashMap<String, ArrayList<Integer>> annoFreq = new HashMap();
            String[] columnNames = new String[verNames.size() + 1];
            columnNames[0] = "Text";
            for (int i = 0; i < verNames.size(); i++)
            {
                String verName = verNames.get(i);
                columnNames[i + 1] = verName;
                ArrayList<Entity> annos =
                        viewModel.getEntities(docName, verName);
                for (Entity a : annos)
                {
                    if (a.getEntityType().compareTo(label) != 0)
                    {
                        continue;
                    }
                    ArrayList<Integer> verFreq = annoFreq.get(a.getText());
                    if (verFreq == null)
                    {
                        verFreq = new ArrayList();
                        for (int j = 0; j < verNames.size(); j++)
                        {
                            if (j == i)
                            {
                                verFreq.add(1);
                            } else
                            {
                                verFreq.add(0);
                            }
                        }
                        annoFreq.put(a.getText(), verFreq);
                    } else
                    {
                        Integer n = verFreq.get(i);
                        verFreq.set(i, n + 1);
                        annoFreq.put(a.getText(), verFreq);
                    }
                }
            }
            ArrayList<String> texts = new ArrayList(annoFreq.keySet());
            Collections.sort(texts);
            Object data[][] = new Object[texts.size()][columnNames.length];
            for (int i = 0;
                    i < texts.size();
                    i++)
            {
                String text = texts.get(i);
                data[i][0] = text;
                ArrayList<Integer> textFreq = annoFreq.get(text);
                for (int j = 0;
                        j < verNames.size();
                        j++)
                {
                    data[i][j + 1] = textFreq.get(j);
                }
            }
            DefaultTableModel model = new DefaultTableModel(data, columnNames)
            {
                @Override
                public Class getColumnClass(int column)
                {
                    if (column == 0)
                    {
                        return String.class;
                    } else
                    {
                        return Integer.class;
                    }
                }
            };
            JTable jtable = new JTable(model);
            TableRowSorter<TableModel> sorter = new TableRowSorter(jtable.
                    getModel());
            jtable.setRowSorter(sorter);
            entityTypeTabbedPane.add(label, new JScrollPane(jtable));
            entityTypeTabbedPane.repaint();
        }

    }

    public static void showLabelEvaluator(EvaluatorViewModel viewModel,
                                          EvaluatorController controller)
    {
        EntityTypeEvaluator labelEvaluator =
                new EntityTypeEvaluator(viewModel, controller);
        labelEvaluator.setVisible(true);
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

        docComboBox = new javax.swing.JComboBox();
        entityTypeTabbedPane = new javax.swing.JTabbedPane();
        calculateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(entityTypeTabbedPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(docComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calculateButton, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(entityTypeTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(docComboBox)
                    .addComponent(calculateButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_calculateButtonActionPerformed
    {//GEN-HEADEREND:event_calculateButtonActionPerformed
        String docName = (String) docComboBox.getSelectedItem();
        if (docName == null)
        {
            return;
        }
        try
        {
            populateEntityTypeInfoTable(docName);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_calculateButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton calculateButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox docComboBox;
    private javax.swing.JTabbedPane entityTypeTabbedPane;
    // End of variables declaration//GEN-END:variables

    private void HashMap()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
