/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator;

import edu.sfsu.evaluator.model.Entity;
import edu.sfsu.evaluator.model.ComplexEntity;
import edu.sfsu.evaluator.model.ComplexEntityRule;
import edu.sfsu.evaluator.view.EvaluatorInfo;
import edu.sfsu.evaluator.view.menu.EvaluatorMenu;
import edu.sfsu.evaluator.view.text.EvaluatorTextScreen;
import edu.sfsu.evaluator.view.tree.EvaluatorAnnotationInfo;
import edu.sfsu.evaluator.view.tree.EvaluatorWorkspace;
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
 * View elements may request changes to the model via the controller. To receive
 * information about the model, views must go through the ViewModel.
 * <p/>
 * The controller is also the starting point of the entire program.
 * <p/>
 * @author eric
 */
public class EvaluatorController
{

    // Evaluator elements.
    private EvaluatorViewModel viewModel;
    private EvaluatorModel model;
    private EvaluatorWorkspace evaluatorWorkspace;
    private EvaluatorAnnotationInfo evaluatorAnnoInfo;
    private EvaluatorTextScreen evaluatorTextScreen;
    private EvaluatorInfo evaluatorInfo;
    private EvaluatorMenu evaluatorMenu;
    private JFrame viewFrame;

    public EvaluatorController()
    {
        // Initialize view model
        viewModel = new EvaluatorViewModel();

        // Initialize main frame
        viewFrame = new JFrame("Annotation Evaluator");
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setSize(1000, 800);

        // Initialize all views
        evaluatorWorkspace = new EvaluatorWorkspace(viewModel, this);
        evaluatorAnnoInfo = new EvaluatorAnnotationInfo(viewModel, this);
        evaluatorTextScreen = new EvaluatorTextScreen(viewModel, this);
        evaluatorInfo = new EvaluatorInfo(viewModel, this);
        evaluatorMenu = new EvaluatorMenu(viewModel, this);
        viewFrame.setJMenuBar(evaluatorMenu);

        // Attach views to the View Model
        viewModel.attachView(evaluatorWorkspace);
        viewModel.attachView(evaluatorAnnoInfo);
        viewModel.attachView(evaluatorTextScreen);
        viewModel.attachView(evaluatorInfo);

        // Initalize buttons
        JButton addFileButton = new JButton("+ File");
        JButton addDirButton = new JButton("+ Dir");
        JButton addEntityType = new JButton("+ Entity Type");
        JButton addComplexEntityRule = new JButton("+ Complex Entity Rule");
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
                evaluatorMenu.buttonPressedAddAnnotation();
            }
        });
        addComplexEntityRule.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                evaluatorMenu.buttonPressedAddComplexEntity();
            }
        });

        // Set layout of context pane
        Container pane = viewFrame.getContentPane();
        pane.setLayout(new GridBagLayout());

        // Create grid bag constraints
        GridBagConstraints c = new GridBagConstraints();

        // Attach trees to scrollable panes
        JScrollPane wkspScrollPane = new JScrollPane(evaluatorWorkspace);
        JScrollPane infoScrollPane = new JScrollPane(evaluatorAnnoInfo);

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
        upperPanel.add(evaluatorInfo, c);

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

        wkspPanel.add(wkspScrollPane, c);
        infoPanel.add(infoScrollPane, c);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = 1;

        wkspPanel.add(addFileButton, c);
        infoPanel.add(addEntityType, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = 1;

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
        viewFrame.setVisible(true);
    }

    /**
     * Display a specific document version within all views.
     * <p/>
     * @param docName
     * @param verName
     */
    public void displayDocumentVersion(
            String docName,
            String verName)
    {
        if (!evaluatorAnnoInfo.isDisplaying(docName, verName))
        {
            evaluatorAnnoInfo.display(docName, verName);
        }
        if (!evaluatorTextScreen.isDisplaying(docName, verName))
        {
            evaluatorTextScreen.display(docName, verName);
            evaluatorInfo.display(docName, verName);
        }

    }

    /**
     * Within the text screen display entity specific entity.
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
        return viewFrame;
    }

    /**
     * Request to add an entity to the model. Document and version name specify
     * the document version to be added to.
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
            model.requestEntityAdd(docName, verName, entity);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to delete an entity from the model. Document and version name
     * specify the document version to be added to.
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
            model.requestEntityDelete(docName, verName, annotation);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add a complex entity to the model.
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
            model.requestComplexEntityAdd(docName, verName, complexEntity);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to delete a complex entity from the model.
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
            model.requestComplexEntityDelete(docName, verName, complexEntity);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add a complex entity rule to the model.
     * @param complexEntityRule
     */
    public void requestComplexEntityRuleAdd(
            ComplexEntityRule complexEntityRule)
    {
        try
        {
            model.requestComplexEntityRuleAdd(complexEntityRule);
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
            model.requestComplexEntityRuleColorChanged(complexEntityRuleName,
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
            model.requestComplexEntityRuleDelete(complexEntityRuleName);
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
            model.requestDocumentAdd(docName, docText);
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
            String docText = edu.sfsu.io.FileReader.getTextFromFile(docPath);

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

            // While model contains document name create tmp
            String tmpName = docName;
            int n = 1;
            while (viewModel.containsDocument(tmpName))
            {
                tmpName = String.format("%s(%d)", docName, i++);
            }
            docName = tmpName;

            // Add document to model
            requestDocumentAdd(docName, docText);

        } catch (Exception e)
        {
            showErrorMessage(e);
        }

    }

    /**
     * Request to delete a document from the model.
     * <p/>
     * @param docName
     */
    public void requestDocumentDelete(
            String docName)
    {
        try
        {
            model.requestDocumentDelete(docName);
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
        String newDocName = JOptionPane.showInputDialog(viewFrame,
                                                        "Enter new document name",
                                                        oldDocName);
        if (newDocName == null)
        {
            return;
        }

        // While model contains document name ask for a new name.
        while (viewModel.containsDocument(newDocName))
        {
            String warning =
                    String.format("Already a document named '%s'",
                                  newDocName);
            JOptionPane.showMessageDialog(viewFrame, warning, "Naming Error",
                                          JOptionPane.ERROR_MESSAGE);
            newDocName = JOptionPane.showInputDialog(viewFrame,
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
     * Request to rename a document contained by the model.
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
            model.requestDocumentRename(oldDocName, newDocName);
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
            model.requestDocumentVersionAdd(docName, verName);
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
            model.requestDocumentVersionDelete(docName, verName);
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
            model.requestDocumentVersionRename(docName, oldVerName, newVerName);
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
        String newVerName = JOptionPane.showInputDialog(viewFrame,
                                                        "Enter new document verison name",
                                                        oldVerName);
        if (newVerName == null)
        {
            return;
        }
        try
        {
            // While model contains version name ask for a new name.
            while (viewModel.containsDocumentVersion(docName, newVerName))
            {
                String warning =
                        String.format("Already a document version named '%s'",
                                      newVerName);
                JOptionPane.
                        showMessageDialog(viewFrame, warning, "Naming Error",
                                          JOptionPane.ERROR_MESSAGE);
                newVerName = JOptionPane.showInputDialog(viewFrame,
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
     * Request to add a label to the model.
     * <p/>
     * @param label
     * @param color
     */
    public void requestLabelAdd(
            String label,
            Color color)
    {
        try
        {
            model.requestEntityTypeAdd(label, color);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to add a label to the model with a random color.
     * <p/>
     * @param label
     */
    public void requestLabelAdd(
            String label)
    {
        try
        {
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            model.requestEntityTypeAdd(label, randomColor);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to change the color of a label within the model.
     * <p/>
     * @param label
     * @param newColor
     */
    public void requestLabelColorChange(
            String label,
            Color newColor)
    {
        try
        {
            model.requestEntityTypeColorChange(label, newColor);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to delete a label from the model.
     * <p/>
     * @param label
     */
    public void requestLabelDelete(
            String label)
    {
        try
        {
            model.requestEntityTypeDelete(label);
        } catch (Exception e)
        {
            showErrorMessage(e);
        }
    }

    /**
     * Request to rename a label from the model.
     * <p/>
     * @param oldLabel
     * @param newLabel
     */
    public void requestLabelRename(
            String oldLabel,
            String newLabel)
    {
        try
        {
            model.requestEntityTypeRename(oldLabel, newLabel);
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
            model.requestSaveState();
        } catch (Exception e)
        {
            String message = "Could not save workspace state";
            showErrorMessage(message);
        }
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
            this.model = model;
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
        JOptionPane.showMessageDialog(viewFrame, e.getMessage(), "ERROR",
                                      JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display error message.
     * <p/>
     * @param message
     */
    public void showErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(viewFrame, message, "ERROR",
                                      JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display warning message.
     * <p/>
     * @param e
     */
    public void showWarningMessage(String message)
    {
        JOptionPane.showMessageDialog(viewFrame, message, "WARNING",
                                      JOptionPane.WARNING_MESSAGE);
    }
}
