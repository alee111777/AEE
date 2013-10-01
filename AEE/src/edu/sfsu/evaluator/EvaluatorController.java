/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator;

import edu.sfsu.evaluator.model.ComplexEntity;
import edu.sfsu.evaluator.model.ComplexEntityRule;
import edu.sfsu.evaluator.model.Entity;
import edu.sfsu.evaluator.view.EvaluatorInfoLabel;
import edu.sfsu.evaluator.view.menu.EvaluatorMenu;
import edu.sfsu.evaluator.view.text.EvaluatorTextScreen;
import edu.sfsu.evaluator.view.tree.EvaluatorCorpusTree;
import edu.sfsu.evaluator.view.tree.EvaluatorEntityTree;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * Contains the main frame and organizes all GUI elements.
 * <p/>
 * View elements may request changes to the evaluatorModel via the controller.
 * To receive information about the evaluatorModel, views must go through the
 * ViewModel.
 * <p/>
 * The controller is also the starting point of the entire program.
 * <p/>
 * @author eric
 */
public class EvaluatorController
{

    // Evaluator elements.
    private EvaluatorViewModel viewModel;
    private EvaluatorModel evaluatorModel;
    private EvaluatorCorpusTree evaluatorCorpusTree;
    private EvaluatorEntityTree evaluatorEntityTree;
    private EvaluatorTextScreen evaluatorTextScreen;
    private EvaluatorInfoLabel evaluatorInfoBar;
    private EvaluatorMenu evaluatorMenu;
    private JFrame evaluatorViewFrame;

    public EvaluatorController()
    {
        // Initialize view evaluatorModel
        viewModel = new EvaluatorViewModel();

        // Initialize main frame
        evaluatorViewFrame = new JFrame("Annotation Evaluator");
        evaluatorViewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        evaluatorViewFrame.setSize(1000, 800);

        // Initialize all views
        evaluatorCorpusTree = new EvaluatorCorpusTree(viewModel, this);
        evaluatorEntityTree = new EvaluatorEntityTree(viewModel, this);
        evaluatorTextScreen = new EvaluatorTextScreen(viewModel, this);
        evaluatorInfoBar = new EvaluatorInfoLabel(viewModel, this);
        evaluatorMenu = new EvaluatorMenu(viewModel, this);
        evaluatorViewFrame.setJMenuBar(evaluatorMenu);

        // Attach views to the View Model
        viewModel.attachView(evaluatorCorpusTree);
        viewModel.attachView(evaluatorEntityTree);
        viewModel.attachView(evaluatorTextScreen);
        viewModel.attachView(evaluatorInfoBar);

        // Initalize buttons
        JButton addFileButton = new JButton("+ File");
        JButton addDirButton = new JButton("+ Dir");
        JButton addEntityType = new JButton("+ Entity Type");
        JButton addComplexEntityRule = new JButton("+ Complex Entity Rule");

        /**
         * Add action listeners to buttons.
         * To prevent rewriting code these action listeners call
         * evaluatorMenu methods which already implement the same actions.
         */
        addFileButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                evaluatorMenu.buttonPressedFileAddFiles();
            }
        });
        addDirButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                evaluatorMenu.buttonPressedFileAddDirs();
            }
        });
        addEntityType.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                evaluatorMenu.buttonPressedAddEntityType();
            }
        });
        addComplexEntityRule.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                evaluatorMenu.buttonPressedAddComplexEntityRule();
            }
        });

        // Set layout of context pane of main frame.
        Container pane = evaluatorViewFrame.getContentPane();
        pane.setLayout(new GridBagLayout());

        // Create grid bag constraints
        GridBagConstraints c = new GridBagConstraints();

        // Attach trees to scrollable panes
        JScrollPane corpusScrollPane = new JScrollPane(evaluatorCorpusTree);
        JScrollPane entityScrollPane = new JScrollPane(evaluatorEntityTree);

        // Construct upper workspace panel with layout
        JPanel upperPanel = new JPanel(new GridBagLayout());

        // Attach text screen to upper panel
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        upperPanel.add(new JScrollPane(evaluatorTextScreen), c);

        // Attach info bar to upper panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 1;
        upperPanel.add(evaluatorInfoBar, c);

        // Create tree panels
        JPanel wkspPanel = new JPanel(new GridBagLayout());
        JPanel infoPanel = new JPanel(new GridBagLayout());

        // Attach trees and buttons to respective panels
        // This is done at the same time for both because of their similar
        // layout
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 2;

        // Add main trees
        wkspPanel.add(corpusScrollPane, c);
        infoPanel.add(entityScrollPane, c);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = 1;

        // Add buttons
        wkspPanel.add(addFileButton, c);
        infoPanel.add(addEntityType, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = 1;

        // Add buttons
        wkspPanel.add(addDirButton, c);
        infoPanel.add(addComplexEntityRule, c);

        // Crate lower panel
        JPanel lowerPanel = new JPanel(new GridBagLayout());

        c.fill = GridBagConstraints.BOTH;

        // Attach both tree panels to lower panel
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        lowerPanel.add(wkspPanel, c);
        c.gridx = 1;
        c.gridy = 0;
        lowerPanel.add(infoPanel, c);


        // Create split pane with both upper and lower panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                              upperPanel,
                                              lowerPanel);

        splitPane.setDividerLocation(400);
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(splitPane, c);

        // Display frame!
        evaluatorViewFrame.setVisible(true);
    }

    /**
     * Display a specific document version within all views. This will
     * essentially cause the text screen and info tree to load information
     * pertaining to a specific document version.
     * <p/>
     * @param docName
     * @param verName
     */
    public void displayDocumentVersion(
            String docName,
            String verName)
    {
        // If these GUI elements are already displaying this document version
        // then don't bother redisplaying.
        if (!evaluatorEntityTree.isDisplaying(docName, verName))
        {
            evaluatorEntityTree.display(docName, verName);
        }
        if (!evaluatorTextScreen.isDisplaying(docName, verName))
        {
            evaluatorTextScreen.display(docName, verName);
            evaluatorInfoBar.display(docName, verName);
        }

    }

    /**
     * Move the text screen cursor to a specific entity. Will scroll to that
     * entity if it is off the screen.
     * <p/>
     * @param docName
     * @param verName
     * @param entity
     */
    public void displayEntity(String docName, String verName, Entity entity)
    {
        evaluatorTextScreen.display(docName, verName, entity);
    }

    /**
     * Get main frame of GUI for dialogs to set as a parent.
     * <p/>
     * @return
     */
    public JFrame getEvaluatorFrame()
    {
        return evaluatorViewFrame;
    }

    /**
     * Request to add an entity to the evaluatorModel. Document and version name
     * specify the document version to be added to.
     * <p/>
     * @param docName Document which contains the entity.
     * @param verName
     * @param entity
     */
    public void requestAddEntity(
            String docName,
            String verName,
            Entity entity)
    {
        try
        {
            evaluatorModel.requestEntityAdd(docName, verName, entity);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to delete an entity from the evaluatorModel. Document and version
     * name specify the document version to be added to.
     * <p/>
     * @param docName
     * @param verName
     * @param annotation
     */
    public void requestAnnotationDelete(String docName,
                                        String verName,
                                        Entity annotation)
    {
        try
        {
            evaluatorModel.requestEntityDelete(docName, verName, annotation);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add a complex entity to the evaluatorModel.
     * <p/>
     * @param docName
     * @param verName
     * @param complexEntity
     */
    public void requestComplexEntityAdd(
            String docName,
            String verName,
            ComplexEntity complexEntity)
    {
        try
        {
            evaluatorModel.requestComplexEntityAdd(docName, verName, complexEntity);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to delete a complex entity from the evaluatorModel.
     * <p/>
     * @param docName
     * @param verName
     * @param complexEntity
     */
    public void requestComplexEntityDelete(
            String docName,
            String verName,
            ComplexEntity complexEntity)
    {
        try
        {
            evaluatorModel.requestComplexEntityDelete(docName, verName, complexEntity);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add a complex entity rule to the evaluatorModel.
     * @param complexEntityRule
     */
    public void requestComplexEntityRuleAdd(
            ComplexEntityRule complexEntityRule)
    {
        try
        {
            evaluatorModel.requestComplexEntityRuleAdd(complexEntityRule);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to change the color of a complex entity rule.
     * @param complexEntityRuleName
     * @param color
     */
    public void requestComplexEntityRuleColorChanged(
            String complexEntityRuleName, Color color)
    {
        try
        {
            evaluatorModel.requestComplexEntityRuleColorChanged(complexEntityRuleName,
                                                       color);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to delete a complex entity rule.
     * @param complexEntityRuleName
     */
    public void requestComplexEntityRuleDelete(String complexEntityRuleName)
    {
        try
        {
            evaluatorModel.requestComplexEntityRuleDelete(complexEntityRuleName);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add a document with document name and document text.
     * <p/>
     * @param docName
     * @param docText
     */
    public void requestDocumentAdd(
            String docName,
            String docText)
    {
        try
        {
            evaluatorModel.requestDocumentAdd(docName, docText);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add a document with document path.
     * <p/>
     * @param docPath
     */
    public void requestDocumentAdd(
            String docPath)
    {
        try
        {
            // Get document text from file path.
            String docText = edu.sfsu.util.FileUtilities.getTextFromFile(docPath);

            // Get file name from path. Ex: "corpus/1234a.txt" -> "1234.txt"
            int i = docPath.lastIndexOf(System.getProperty("file.separator"));
            String docName;
            if (i < 0)
            {
                docName = docPath;
            } else
            {
                docName = docPath.substring(i + 1);
            }

            // While evaluatorModel contains document name create tmp
            String tmpName = docName;
            int n = 1;
            while (viewModel.containsDocument(tmpName))
            {
                tmpName = String.format("%s(%d)", docName, i++);
            }
            docName = tmpName;

            // Add document to evaluatorModel
            requestDocumentAdd(docName, docText);

        } catch (Exception e)
        {
            showErrorMessage(e);
        }

    }

    /**
     * Request to delete a document from the evaluatorModel.
     * <p/>
     * @param docName
     */
    public void requestDocumentDelete(
            String docName)
    {
        try
        {
            evaluatorModel.requestDocumentDelete(docName);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to rename a document by requesting a new document name from user.
     * <p/>
     * @param oldDocName
     */
    public void requestDocumentRename(
            String oldDocName)
    {
        // Get document name
        String newDocName = JOptionPane.showInputDialog(evaluatorViewFrame,
                                                        "Enter new document name",
                                                        oldDocName);
        if (newDocName == null)
        {
            return;
        }

        // While evaluatorModel contains document name ask for a new name.
        while (viewModel.containsDocument(newDocName))
        {
            String warning =
                    String.format("Already a document named '%s'",
                                  newDocName);
            JOptionPane.showMessageDialog(evaluatorViewFrame, warning, "Naming Error",
                                          JOptionPane.ERROR_MESSAGE);
            newDocName = JOptionPane.showInputDialog(evaluatorViewFrame,
                                                     "Enter new document name",
                                                     newDocName);
            if (newDocName == null)
            {
                return;
            }
        }
        requestDocumentRename(oldDocName, newDocName);
    }

    /**
     * Request to rename a document contained by the evaluatorModel.
     * <p/>
     * @param oldDocName
     * @param newDocName
     */
    public void requestDocumentRename(
            String oldDocName,
            String newDocName)
    {
        try
        {
            evaluatorModel.requestDocumentRename(oldDocName, newDocName);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add a document version.
     * <p/>
     * @param docName
     * @param verName
     */
    public void requestDocumentVersionAdd(
            String docName,
            String verName)
    {
        try
        {
            evaluatorModel.requestDocumentVersionAdd(docName, verName);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add a document version and generate the version name.
     * <p/>
     * @param docName
     */
    public void requestDocumentVersionAdd(
            String docName)
    {
        String verName;
        int i;
        try
        {
            i = viewModel.getAvailableDocumentVersions(docName).size();
            // Generate new name
            do
            {
                verName = String.format("%s(%d)", docName, i++);
            } while (viewModel.containsDocumentVersion(docName, verName));
            requestDocumentVersionAdd(docName, verName);
        } catch (Exception e)
        {
            showErrorMessage(
                    String.format("Could not load document: %s", docName));
        }
    }

    /**
     * Request to delete a document version.
     * <p/>
     * @param docName
     * @param verName
     */
    public void requestDocumentVersionDelete(
            String docName,
            String verName)
    {
        try
        {
            evaluatorModel.requestDocumentVersionDelete(docName, verName);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to rename a document version.
     * <p/>
     * @param docName
     * @param oldVerName
     * @param newVerName
     */
    public void requestDocumentVersionRename(
            String docName,
            String oldVerName,
            String newVerName)
    {
        try
        {
            evaluatorModel.requestDocumentVersionRename(docName, oldVerName, newVerName);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to rename a document version and get version name from user.
     * <p/>
     * @param docName
     * @param oldVerName
     */
    public void requestDocumentVersionRename(
            String docName,
            String oldVerName)
    {
        // Get version name
        String newVerName = JOptionPane.showInputDialog(evaluatorViewFrame,
                                                        "Enter new document verison name",
                                                        oldVerName);
        if (newVerName == null)
        {
            return;
        }
        try
        {
            // While evaluatorModel contains version name ask for a new name.
            while (viewModel.containsDocumentVersion(docName, newVerName))
            {
                String warning =
                        String.format("Already a document version named '%s'",
                                      newVerName);
                JOptionPane.
                        showMessageDialog(evaluatorViewFrame, warning, "Naming Error",
                                          JOptionPane.ERROR_MESSAGE);
                newVerName = JOptionPane.showInputDialog(evaluatorViewFrame,
                                                         "Enter new document verison name",
                                                         newVerName);
                if (newVerName == null)
                {
                    return;
                }
            }
            requestDocumentVersionRename(docName, oldVerName, newVerName);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add a entity type to the evaluatorModel.
     * <p/>
     * @param entityTypeName
     * @param color
     */
    public void requestEntityTypeAdd(
            String entityTypeName,
            Color color)
    {
        try
        {
            evaluatorModel.requestEntityTypeAdd(entityTypeName, color);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add an entity type to the evaluator Model with a random color.
     * <p/>
     * @param entityTypeName
     */
    public void requestEntityTypeAdd(
            String entityTypeName)
    {
        try
        {
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            evaluatorModel.requestEntityTypeAdd(entityTypeName, randomColor);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }
    
    /**
     * Request to add an entity type to the evaluator Model with a random color.
     * <p/>
     * @param entityTypeName
     */
    public void requestEntityTypeAddFromFile(
            String entityTypeName)
    {
        try
        {
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            evaluatorModel.requestEntityTypeAddFromFile(entityTypeName, randomColor);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to change the color of a entity type within the evaluatorModel.
     * <p/>
     * @param entityTypeName
     * @param newColor
     */
    public void requestEntityTypeColorChange(
            String entityTypeName,
            Color newColor)
    {
        try
        {
            evaluatorModel.requestEntityTypeColorChange(entityTypeName, newColor);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to delete a entity type from the evaluatorModel.
     * <p/>
     * @param entityTypeName
     */
    public void requestEntityTypeDelete(
            String entityTypeName)
    {
        try
        {
            evaluatorModel.requestEntityTypeDelete(entityTypeName);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to rename a entity type from the evaluatorModel.
     * <p/>
     * @param oldEntityTypeName
     * @param newEntityTypeName
     */
    public void requestLabelRename(
            String oldEntityTypeName,
            String newEntityTypeName)
    {
        try
        {
            evaluatorModel.requestEntityTypeRename(oldEntityTypeName,
                                                   newEntityTypeName);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to save the state of the workspace.
     */
    public void requestSaveState()
    {
        try
        {
            evaluatorModel.requestSaveState();
        } catch (Exception e)
        {
            String message = "Could not save workspace state";
            showErrorMessage(message);
        }
    }
    
    /**
     * request evaluate measurement process.
     *  toggles opening of measurement dialog box
     */
    public void requestEvaluateMeasurement(String docName) {
        evaluatorMenu.buttonPressedEvaluateMeasurements(docName);
    }
    
    public void requestImport(String docName, String verName) {
        evaluatorMenu.buttonPressedFileImport(docName, verName);
    }

    /**
     * Set preference for text screen.
     * <p/>
     * @param b
     */
    public void setAutoAddAll(boolean b)
    {
        evaluatorTextScreen.setAutoAddAll(b);
    }

    /**
     * Set preference for text screen.
     * <p/>
     * @param b
     */
    public void setAutoHighlight(boolean b)
    {
        evaluatorTextScreen.setAutoHighlight(b);
    }

    /**
     * Set if complex entity rule should be highlighted by name.
     * @param entityRuleName
     * @param highlighted
     */
    public void setComplexEntityRuleHighlight(String entityRuleName,
                                              boolean highlighted)
    {
        viewModel.setComplexEntityRuleHighlighted(entityRuleName, highlighted);
    }

    /**
     * Set if entity should be highlighted by name.
     * @param entityName
     * @param highlighted
     */
    public void setEntityHighlight(String entityName, boolean highlighted)
    {
        viewModel.setEntityTypeHighlight(entityName, highlighted);
    }

    /**
     * Set workspace.
     * <p/>
     * @param docPath
     */
    public void setWorkspace(String docPath)
    {
        try
        {
            EvaluatorModel model = new EvaluatorModel(viewModel, docPath);
            this.evaluatorModel = model;
            viewModel.setModel(model);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Display error message from exception.
     * <p/>
     * @param e
     */
    public void showErrorMessage(Exception e)
    {
        System.err.println(e.getMessage());
        e.printStackTrace(System.err);
        JOptionPane.showMessageDialog(evaluatorViewFrame, e.getMessage(), "ERROR",
                                      JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display error message.
     * <p/>
     * @param message
     */
    public void showErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(evaluatorViewFrame, message, "ERROR",
                                      JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display warning message.
     * <p/>
     * @param e
     */
    public void showWarningMessage(String message)
    {
        JOptionPane.showMessageDialog(evaluatorViewFrame, message, "WARNING",
                                      JOptionPane.WARNING_MESSAGE);
    }
}
