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
 * Dialog for importing entities. Activated by 'file' --> 'import'
 * @author eric
 */
public class ImportDialog extends JDialog
{

    private EvaluatorViewModel viewModel;
    private EvaluatorController controller;
    private JComboBox docComboBox;
    private JComboBox verComboBox;
    private JComboBox entityTypeComboBox;
    private JButton fileNameButton;
    // For document selector
    private String lastDocSelected = "";
    private String lastFileDir = null;
    protected static final String INITIAL_MESSAGE = "*Select File*";

    public ImportDialog(EvaluatorViewModel viewModel,
                        EvaluatorController controller)
    {
        super(controller.getEvaluatorFrame());

        this.setTitle("IMPORT");
        this.viewModel = viewModel;
        this.controller = controller;


        // Get names of all entity types (colors are not needed)
        ArrayList<String> namesOfEntityTypes =
                new ArrayList(viewModel.getEntityTypes().keySet());
        Collections.sort(namesOfEntityTypes);

        // Create combo box of entity types
        entityTypeComboBox =
                new JComboBox(namesOfEntityTypes.toArray(new String[0]));

        // Get document names
        ArrayList<String> docNames =
                new ArrayList(viewModel.getAvailableDocuments());
        Collections.sort(docNames);

        // Create combo box of document names.
        docComboBox = new JComboBox(docNames.toArray(new String[0]));
        verComboBox = new JComboBox();
        populateVerComboBox();
        docComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                /*
                 * When document combo box is changed, if the selected document
                 * is different than the last one then repopulate the version
                 * combo box with the versions associated with the selected
                 * document.
                 */
                String docName = (String) docComboBox.getSelectedItem();
                if (docName.compareTo(lastDocSelected) != 0)
                {
                    lastDocSelected = docName;
                    populateVerComboBox();
                }
            }
        });

        // Fill file button with initial message prompt: "Select a file"
        fileNameButton = new JButton(INITIAL_MESSAGE);
        fileNameButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // When button is pushed, prompt the user to select a file
                selectFile();
            }
        });

        // Create ok button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Attempt to import te document
                ok();
            }
        });

        // Create cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Close dialog
                cancel();
            }
        });

        // Use grid bag layout to add buttons to dialog pane
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
        pane.add(entityTypeComboBox, c);
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

    /**
     * Static method which may be called from outside to display this dialog.
     * @param viewModel
     * @param controller
     */
    public static void showImportDialog(EvaluatorViewModel viewModel,
                                        EvaluatorController controller)
    {
        ImportDialog importDialog = new ImportDialog(viewModel, controller);
        importDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        importDialog.setVisible(true);
    }

    /**
     * When a new document has been selected the version combo box must change
     * to reflect that document.
     */
    private void populateVerComboBox()
    {
        // Get the document name the document combo box has selected
        String docName = (String) docComboBox.getSelectedItem();
        verComboBox.removeAllItems();
        if (docName == null)
        {
            return; // If no document is selected, do nothing.
        }
        try
        {
            // Get version names associated with selected document.
            ArrayList<String> verNames =
                    viewModel.getAvailableDocumentVersions(docName);
            Collections.sort(verNames);
            for (String verName : verNames)
            {
                verComboBox.addItem(verName);
            }
        } catch (Exception e)
        {
            controller.showErrorMessage("Error populating ver combo box");
        }
    }

    /**
     * Prompt the user with a <code>JFileChooser</code> for the input file.
     */
    private void selectFile()
    {
        JFileChooser fc;
        // Open file chooser in last directory user navigated to.
        if (lastFileDir != null)
        {
            fc = new JFileChooser(lastFileDir);
        } else
        {
            fc = new JFileChooser();
        }
        // Filter out text files
        fc.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
        fc.setMultiSelectionEnabled(false);

        /*
         * If file has been selected then store that file information in the
         * fileNameButton. This allows it to be retrieved later and displayed to
         * the user.
         */
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

    /**
     * Close dialog.
     */
    private void cancel()
    {
        this.dispose();
    }

    /**
     * OK button pressed.
     */
    private void ok()
    {
        try
        {
            /**
             * Try to get document name, version name, and entity type from
             * combo boxes.
             */
            String docName = (String) docComboBox.getSelectedItem();
            String verName = (String) verComboBox.getSelectedItem();
            String entityType = (String) entityTypeComboBox.getSelectedItem();
            if ((docName == null) || (verName == null) || (entityType == null))
            {
                return;
            }

            /**
             * Get document text from Model to ensure that the text matches any
             * entities we might try to add.
             */
            String docText = viewModel.getDocumentText(docName);
            String filePath = fileNameButton.getText();
            if (filePath.compareTo(INITIAL_MESSAGE) == 0)
            {
                return;
            }
            // Use entity reader to read entities from file.
            ArrayList<Entity> entities =
                    EntityReader.readEntities(filePath, docText, entityType);
            for (Entity e : entities)
            {
                controller.requestAddEntity(docName, verName, e);
            }
        } catch (Exception e)
        {
        }
        // Close dialog
        this.dispose();
    }
}
