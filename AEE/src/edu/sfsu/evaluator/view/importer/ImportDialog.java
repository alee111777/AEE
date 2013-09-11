/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.importer;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import edu.sfsu.evaluator.model.Entity;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author eric
 */
public class ImportDialog extends JDialog
{

    private EvaluatorViewModel viewModel;
    private EvaluatorController controller;
    private JComboBox docComboBox;
    private JComboBox verComboBox;
    private JComboBox labelComboBox;
    private JButton fileNameButton;
    private String lastDocSelected = "";
    private String lastFileDir = null;
    protected static final String initialText = "*Select File*";

    public ImportDialog(EvaluatorViewModel viewModel,
                        EvaluatorController controller)
    {
        super(controller.getEvaluatorFrame());

        this.setTitle("IMPORT");
        this.viewModel = viewModel;
        this.controller = controller;

        ArrayList<String> labelNames =
                new ArrayList(viewModel.getLabels().keySet());

        Collections.sort(labelNames);

        labelComboBox = new JComboBox(labelNames.toArray(new String[0]));

        ArrayList<String> docNames =
                new ArrayList(viewModel.getAvailableDocuments());

        Collections.sort(docNames);

        docComboBox = new JComboBox(docNames.toArray(new String[0]));
        verComboBox = new JComboBox();
        populateVerComboBox();
        docComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String docName = (String) docComboBox.getSelectedItem();
                if (docName.compareTo(lastDocSelected) != 0)
                {
                    lastDocSelected = docName;
                    populateVerComboBox();
                }
            }
        });

        fileNameButton = new JButton(initialText);

        fileNameButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                selectFile();
            }
        });

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ok();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cancel();
            }
        });

        Container pane = this.getContentPane();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridwidth = 2;
        pane.add(docComboBox, c);
        c.gridy = 1;
        pane.add(verComboBox, c);
        c.gridy = 2;
        pane.add(labelComboBox, c);
        c.gridy = 3;
        pane.add(fileNameButton, c);
        c.gridwidth = 1;
        c.gridy = 4;
        c.weightx = 1;
        pane.add(okButton, c);
        c.gridx = 1;
        pane.add(cancelButton, c);

        this.setSize(400, 400);
    }

    public static void showImportDialog(EvaluatorViewModel viewModel,
                                        EvaluatorController controller)
    {
        ImportDialog importDialog = new ImportDialog(viewModel, controller);
        importDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        importDialog.setVisible(true);
    }

    private void populateVerComboBox()
    {
        String docName = (String) docComboBox.getSelectedItem();
        if (docName == null)
        {
            return;
        }
        try
        {
            verComboBox.removeAllItems();
            ArrayList<String> verNames =
                    viewModel.getAvailableDocumentVersions(docName);
            Collections.sort(verNames);
            for (String verName
                    : verNames)
            {
                verComboBox.addItem(verName);
            }
        } catch (Exception e)
        {
            controller.showErrorMessage("Error populating ver combo box");
        }
    }

    private void selectFile()
    {
        JFileChooser fc;
        if (lastFileDir != null)
        {
            fc = new JFileChooser(lastFileDir);
        } else
        {
            fc = new JFileChooser();
        }
        fc.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
        fc.setMultiSelectionEnabled(false);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();

            if (file.isFile())
            {
                lastFileDir = file.getParent();
                fileNameButton.setText(file.getPath());
            }
        }
    }

    private void cancel()
    {
        this.dispose();
    }

    private void ok()
    {
        try
        {
            String docName = (String) docComboBox.getSelectedItem();
            String verName = (String) verComboBox.getSelectedItem();
            String label = (String) labelComboBox.getSelectedItem();
            if ((docName == null) || (verName == null) || (label == null))
            {
                return;
            }

            String docText = viewModel.getDocumentText(docName);
            String filePath = fileNameButton.getText();
            if (filePath.compareTo(initialText) == 0)
            {
                return;
            }
            ArrayList<Entity> annotations =
                    AnnotationReader.readAnnotations(filePath, docText, label);
            for (Entity a
                    : annotations)
            {
                controller.requestAddEntity(docName, verName, a);
            }
        } catch (Exception e)
        {
        }
        this.dispose();
    }
}
