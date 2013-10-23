/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import edu.sfsu.evaluator.view.importer.ImportDialogSWING;
import edu.sfsu.evaluator.view.menu.add.AddEntityTypeDialog;
import edu.sfsu.evaluator.view.menu.add.CreateComplexEntityRuleDialog;
import edu.sfsu.evaluator.view.menu.evaluate.CorpusMeasurementsCalculator;
import edu.sfsu.evaluator.view.menu.evaluate.EntityTypeEvaluator;
import edu.sfsu.evaluator.view.menu.evaluate.MeasurementEvaluatorDialog;
import edu.sfsu.evaluator.view.menu.evaluate.groupabletableheader.CorpusTableJDialog;
import edu.sfsu.evaluator.view.menu.multiedit.MultiEditorDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author eric
 */
public class EvaluatorMenu extends JMenuBar
{

    private EvaluatorViewModel viewModel;
    private EvaluatorController controller;
    private String lastFileDir = null;
    private String lastWkspDir = null;

    public EvaluatorMenu(
            EvaluatorViewModel viewModel,
            EvaluatorController controller)
    {
        super();
        this.viewModel = viewModel;
        this.controller = controller;

        // File
        final JMenu fileMenu = new JMenu("File");
        this.add(fileMenu);

        // File --> Set Workspace
        JMenuItem setWkspMenuItem = new JMenuItem("Set Workspace");
        setWkspMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedFileSetWorkspace();
            }
        });
        fileMenu.add(setWkspMenuItem);

        // File separator
        fileMenu.addSeparator();

        // File --> Save Workspace State
        JMenuItem saveMenuItem = new JMenuItem("Save Workspace State");
        saveMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedFileSaveState();
            }
        });
        fileMenu.add(saveMenuItem);

        // File separator
        fileMenu.addSeparator();

        // File --> Add File(s)
        JMenuItem addFilesMenuItem = new JMenuItem("Add Files(s)");
        addFilesMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedFileAddFiles();
            }
        });
        fileMenu.add(addFilesMenuItem);

        // File --> Add Dir(s)
        JMenuItem addDirsMenuItem = new JMenuItem("Add Dir(s)");
        addDirsMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedFileAddDirs();
            }
        });
        fileMenu.add(addDirsMenuItem);

        // File --> Import
        JMenuItem importMenuItem = new JMenuItem("Import");
        importMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedFileImport();
            }
        });
        fileMenu.add(importMenuItem);

        // File separator
        fileMenu.addSeparator();

        // File --> Exit
        JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        // Add
        JMenu createMenu = new JMenu("Create");
        this.add(createMenu);

        // Add --> Entity Type
        JMenuItem addEntityTypeMenuItem = new JMenuItem("Entity Type");
        addEntityTypeMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedAddEntityType();
            }
        });
        createMenu.add(addEntityTypeMenuItem);

        // Add --> Complex Entity Rule
        JMenuItem complexEntityRuleMenuItem =
                new JMenuItem("Complex Entity Rule");
        complexEntityRuleMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedAddComplexEntityRule();
            }
        });
        createMenu.add(complexEntityRuleMenuItem);

        // Preferences
        JMenu preferencesMenu = new JMenu("Preferences");
        this.add(preferencesMenu);

        // Preferences --> Auto-addAll
        JCheckBoxMenuItem autoAddAllMenuItem =
                new JCheckBoxMenuItem("Auto Add All");
        autoAddAllMenuItem.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.getSource();
                preferencesAutoAddAllChanged(checkBox.isSelected());
            }
        });
        preferencesMenu.add(autoAddAllMenuItem);
        autoAddAllMenuItem.setSelected(true);

        // Preferences --> Auto-highlight
        JCheckBoxMenuItem autoHighlightMenuItem =
                new JCheckBoxMenuItem("Highlight Expansion");
        autoHighlightMenuItem.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.getSource();
                preferencesAutoHighlightChanged(checkBox.isSelected());
            }
        });
        preferencesMenu.add(autoHighlightMenuItem);
        autoHighlightMenuItem.setSelected(true);

        // Evaluate
        JMenu evaluateMenu = new JMenu("Evaluate");
        this.add(evaluateMenu);

        // Evaluate --> Measurements
        JMenuItem measurementsMenuItem = new JMenuItem("Measurements");
        measurementsMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedEvaluateMeasurements();
            }
        });
        evaluateMenu.add(measurementsMenuItem);
        
        // Evaluate --> Measurements
        JMenuItem corpusMeasurementsMenuItem = new JMenuItem("Corpus Measurements");
        corpusMeasurementsMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               
                buttonPressedEvaluateCorpusMeasurements();
            }
        });
        evaluateMenu.add(corpusMeasurementsMenuItem);

        // Evaluate --> Label Info
        JMenuItem labelInfoMenuItem = new JMenuItem("Label Info");
        labelInfoMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedEvaluateLabelInfo();
            }
        });
        evaluateMenu.add(labelInfoMenuItem);

        // MultiEditor
        JButton multiEditorButton = new JButton("Mutli-Editor");
        multiEditorButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedMutliEditor();
            }
        });
        this.add(multiEditorButton);
    }

    /**
     * File --> Set Workspace button pressed.
     */
    private void buttonPressedFileSetWorkspace()
    {
        JFileChooser fc;
        if (lastWkspDir != null)
        {
            fc = new JFileChooser(lastWkspDir);
        } else
        {
            fc = new JFileChooser();
        }
        // Create filter for directories.
        fc.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(File f)
            {
                return f.isDirectory();
            }

            @Override
            public String getDescription()
            {
                return "Select a directory";
            }
        });
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);
        // Ask user for directory
        // If successful have Controller set it as workspace
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            controller.setWorkspace(file.getPath());
        }
    }

    /**
     * File --> Add Dir(s) button pressed.
     * <p/>
     * Method is public to allow other buttons to create the same effect.
     */
    public void buttonPressedFileAddDirs()
    {
        // Model must be loaded to add dirs to corpus
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }

        // Open in last directory the user navigated to
        JFileChooser fc;
        if (lastFileDir != null)
        {
            fc = new JFileChooser(lastFileDir);
        } else
        {
            fc = new JFileChooser();
        }
        // Create filter for directories.
        fc.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(File f)
            {
                return f.isDirectory();
            }

            @Override
            public String getDescription()
            {
                return "Select a directory";
            }
        });
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // Allow mutiple directories to be added.
        fc.setMultiSelectionEnabled(true);
        // Ask user for files
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            // For each directory selected
            File[] dirs = fc.getSelectedFiles();
            for (int i = 0; i < dirs.length; i++)
            {
                if (dirs[i].isDirectory())
                {
                    // For each file in those directories
                    File[] files = dirs[i].listFiles();
                    for (int j = 0; j < files.length; j++)
                    {
                        // Attempt to add document to corpus
                        if (files[j].isFile())
                        {
                            lastFileDir = files[j].getParent();
                            controller.requestDocumentAdd(files[j].getPath());
                        }
                    }
                }
            }
            // Repaint view
            viewModel.repaintView();
        }
    }

    /**
     * File --> Add File(s) button pressed.
     * <p/>
     * Method is public to allow other buttons to create the same effect.
     */
    public void buttonPressedFileAddFiles()
    {
        // Model must be set to add files to corpus
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }

        // Open in last dir user navigated to
        JFileChooser fc;
        if (lastFileDir != null)
        {
            fc = new JFileChooser(lastFileDir);
        } else
        {
            fc = new JFileChooser();
        }
        fc.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
        fc.setMultiSelectionEnabled(true);

        // Ask user for files
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            // For each file, add to directory
            File[] files = fc.getSelectedFiles();
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].isFile())
                {
                    lastFileDir = files[i].getParent();
                    controller.requestDocumentAdd(files[i].getPath());
                }
            }
            viewModel.repaintView();
        }
    }

    /**
     * File --> Import button pressed.
     */
    public void buttonPressedFileImport()
    {
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }

        // Display import dialog
        ImportDialogSWING.showImportDialog(viewModel, controller);
    }
    
    /**
     * Import popup menu item selected.
     */
    public void buttonPressedFileImport(String docName, String verName)
    {
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }

        // Display import dialog with docName and verName at top of combo boxes
        ImportDialogSWING.showImportDialog(
                viewModel, controller, docName, verName);
    }

    /**
     * File --> Save State button pressed.
     */
    private void buttonPressedFileSaveState()
    {
        controller.requestSaveState();
    }

    /**
     * Add --> Entity Type button pressed
     */
    public void buttonPressedAddEntityType()
    {
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }
        // Display add entity type dialog
        AddEntityTypeDialog.showAddEntityTypeDialog(viewModel,
                                                            controller);
    }

    /**
     * Add --> Complex Entity Rule button pressed
     */
    public void buttonPressedAddComplexEntityRule()
    {
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }
        // Can't gave complex entities with no entities!
        if (viewModel.getEntityTypes().isEmpty())
        {
            String message = "Must create entity types first";
            controller.showWarningMessage(message);
            return;
        }
        // Display create complex entity rule dialog
        CreateComplexEntityRuleDialog.showCreateComplexEntityRuleDialog(
                viewModel, controller);
    }

    /**
     * Preferences --> auto-highlight selection made.
     * @param b
     */
    private void preferencesAutoHighlightChanged(boolean b)
    {
        controller.setAutoHighlight(b);
    }

    /**
     * Preferences --> auto-add all selection made.
     * @param b
     */
    private void preferencesAutoAddAllChanged(boolean b)
    {
        controller.setAutoAddAll(b);
    }

    /**
     * Evaluate --> measurements button pressed.
     */
    public void buttonPressedEvaluateMeasurements()
    {
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }
        // Display measurements dialog
        MeasurementEvaluatorDialog.showMeasurementEvaluatorDialog(
                viewModel, controller);
    }
    
    /**
     * Evaluate --> measurements button pressed. with docName at
     * default for Documents in dialog box
     */
    public void buttonPressedEvaluateMeasurements(String docName)
    {
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }
        // Display measurements dialog
        MeasurementEvaluatorDialog.showMeasurementEvaluatorDialog(
                viewModel, controller, docName);
    }

    /**
     * Evaluate --> Label info button pressed.
     */
    private void buttonPressedEvaluateLabelInfo()
    {
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }
        // Display label evaluator dialog
        EntityTypeEvaluator.showLabelEvaluator(viewModel, controller);
    }

    /**
     * Multi-Editor button pressed
     */
    private void buttonPressedMutliEditor()
    {
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }
        // Display multi editor
        MultiEditorDialog.showMultiEditorDialog(viewModel, controller);
    }
    
    public void buttonPressedEvaluateCorpusMeasurements() {
        if (!viewModel.isModelSet())
        {
            controller.showWarningMessage(EvaluatorViewModel.MODEL_NOT_SET);
            return;
        }
        // Display corpus measurements table
        
        CorpusMeasurementsCalculator calc = new CorpusMeasurementsCalculator();
        
        calc.calculate(viewModel.getDocs());
        new CorpusTableJDialog(calc.getFormattedDocData(),
                calc.getFormattedMentionData(),calc.getMentionNames());
        
        
        
    }
}