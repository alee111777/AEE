/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import edu.sfsu.evaluator.view.importer.ImportDialogSWING;
import edu.sfsu.evaluator.view.menu.add.AddAnnotationTypeDialog;
import edu.sfsu.evaluator.view.menu.add.CreateComplexEntityRuleDialog;
import edu.sfsu.evaluator.view.menu.evaluate.LabelEvaluator;
import edu.sfsu.evaluator.view.menu.evaluate.MeasurementEvaluatorDialog;
import edu.sfsu.evaluator.view.menu.multiedit.MultiEditorDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
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

        // Add --> Annotation Type
        JMenuItem annotationMenuItem = new JMenuItem("Annotation Type");
        annotationMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedAddAnnotation();
            }
        });
        createMenu.add(annotationMenuItem);

        // Add --> Complex Entity
        JMenuItem complexEntityMenuItem = new JMenuItem("Complex Entity");
        complexEntityMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonPressedAddComplexEntity();
            }
        });
        createMenu.add(complexEntityMenuItem);
        /*
         // Edit
         JMenu editMenu = new JMenu("Edit");
         this.add(editMenu);

         // Edit -> Annotations
         JMenuItem annotationsMenuItem = new JMenuItem("Annotations");
         annotationsMenuItem.addActionListener(new ActionListener()
         {
         @Override
         public void actionPerformed(ActionEvent e)
         {
         buttonPressedEditAnnotations();
         }
         });
         editMenu.add(annotationsMenuItem);

         JMenuItem complexEntityRuleMenuItem = new JMenuItem("Complex Entity Rules");
         complexEntityRuleMenuItem.addActionListener(new ActionListener()
         {
         @Override
         public void actionPerformed(ActionEvent e)
         {
         buttonPressedEditComplexEntityRules();
         }
         });
         editMenu.add(complexEntityRuleMenuItem);
         */
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
        if (!viewModel.isModelLoaded())
        {
            controller.showWarningMessage(EvaluatorViewModel.NO_MODEL_LOADED);
            return;
        }

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
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File[] dirs = fc.getSelectedFiles();
            for (int i = 0;
                    i < dirs.length;
                    i++)
            {
                if (dirs[i].isDirectory())
                {
                    File[] files = dirs[i].listFiles();
                    for (int j = 0;
                            j < files.length;
                            j++)
                    {
                        if (files[j].isFile())
                        {
                            lastFileDir = files[j].getParent();
                            controller.requestDocumentAdd(files[j].getPath());
                        }
                    }
                }
            }
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
        if (!viewModel.isModelLoaded())
        {
            controller.showWarningMessage(EvaluatorViewModel.NO_MODEL_LOADED);
            return;
        }

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
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File[] files = fc.getSelectedFiles();
            for (int i = 0;
                    i < files.length;
                    i++)
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
    private void buttonPressedFileImport()
    {
        if (!viewModel.isModelLoaded())
        {
            controller.showWarningMessage(EvaluatorViewModel.NO_MODEL_LOADED);
            return;
        }

        ImportDialogSWING.showImportDialog(viewModel, controller);
    }

    /**
     * File --> Save State button pressed.
     */
    private void buttonPressedFileSaveState()
    {
        controller.requestSaveState();
    }

    public void buttonPressedAddAnnotation()
    {
        if (!viewModel.isModelLoaded())
        {
            controller.showWarningMessage(EvaluatorViewModel.NO_MODEL_LOADED);
            return;
        }
        AddAnnotationTypeDialog.showAddAnnotationTypeDialog(viewModel,
                                                            controller);
    }

    public void buttonPressedAddComplexEntity()
    {
        if (!viewModel.isModelLoaded())
        {
            controller.showWarningMessage(EvaluatorViewModel.NO_MODEL_LOADED);
            return;
        }
        if (viewModel.getLabels().isEmpty())
        {
            String message = "Must create annotation types first";
            controller.showWarningMessage(message);
            return;
        }
        CreateComplexEntityRuleDialog.showCreateComplexEntityRuleDialog(
                viewModel, controller);
    }

    private void preferencesAutoHighlightChanged(boolean b)
    {
        controller.setAutoHighlight(b);
    }

    private void preferencesAutoAddAllChanged(boolean b)
    {
        controller.setAutoAddAll(b);
    }

    private void buttonPressedEvaluateMeasurements()
    {
        if (!viewModel.isModelLoaded())
        {
            controller.showWarningMessage(EvaluatorViewModel.NO_MODEL_LOADED);
            return;
        }
        MeasurementEvaluatorDialog.showMeasurementEvaluatorDialog(
                viewModel, controller);
    }

    private void buttonPressedEvaluateLabelInfo()
    {
        if (!viewModel.isModelLoaded())
        {
            controller.showWarningMessage(EvaluatorViewModel.NO_MODEL_LOADED);
            return;
        }
        LabelEvaluator.showLabelEvaluator(viewModel, controller);
    }

    private void buttonPressedMutliEditor()
    {
        if (!viewModel.isModelLoaded())
        {
            controller.showWarningMessage(EvaluatorViewModel.NO_MODEL_LOADED);
            return;
        }
        MultiEditorDialog.showMultiEditorDialog(viewModel, controller);
    }
}
