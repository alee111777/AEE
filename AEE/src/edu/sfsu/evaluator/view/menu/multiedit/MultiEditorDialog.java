/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.multiedit;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import edu.sfsu.evaluator.view.text.EvaluatorTextScreen;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author eric
 */
public class MultiEditorDialog extends JDialog
{

    private EvaluatorViewModel viewModel;
    private EvaluatorController controller;
    private EvaluatorTextScreen leftTextScreen;
    private EvaluatorTextScreen rightTextScreen;
    private JComboBox docComboBox;
    private String lastDocSelected = null;
    private JComboBox leftVerComboBox;
    private String lastLeftVerSel = null;
    private JComboBox rightVerComboBox;
    private String lastRightVerSel = null;

    public MultiEditorDialog(
            EvaluatorViewModel viewModel,
            EvaluatorController controller)
    {
        super(controller.getEvaluatorFrame(), true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(1000, 500);
        this.setTitle("Multi Document Editor");
        attachWindowListener();

        this.viewModel = viewModel;
        this.controller = controller;

        leftTextScreen = new EvaluatorTextScreen(viewModel, controller);
        rightTextScreen = new EvaluatorTextScreen(viewModel, controller);

        viewModel.attachView(leftTextScreen);
        viewModel.attachView(rightTextScreen);

        docComboBox = new JComboBox();
        leftVerComboBox = new JComboBox();
        rightVerComboBox = new JComboBox();

        addMenu();

        addItemListeners();
        ArrayList<String> docNames = viewModel.getAvailableDocuments();
        Collections.sort(docNames);
        for (String docName
                : docNames)
        {
            docComboBox.addItem(docName);
        }

        Container pane = this.getContentPane();
        pane.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        ScrollablePanel jpanel = new ScrollablePanel();
        jpanel.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 1.0;

        c.gridx = 0;
        c.gridy = 0;
        jpanel.add(leftTextScreen, c);

        c.gridx = 2;
        jpanel.add(rightTextScreen, c);

        c.weightx = 0.01;
        c.gridx = 1;
        jpanel.add(new JPanel(), c);

        c.weightx = 1.0;
        c.weighty = 0.0;

        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 2;
        pane.add(leftVerComboBox, c);

        c.gridy = 1;
        c.gridx = 2;
        c.gridwidth = 2;
        pane.add(rightVerComboBox, c);

        c.weighty = 0.0;
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 4;
        pane.add(docComboBox, c);

        c.weighty = 1.0;
        c.gridy = 2;
        c.gridx = 0;
        c.gridheight = 3;
        c.gridwidth = 4;

        //jpanel.setPreferredSize(new Dimension(800,400));
        jpanel.setSize(400, 400);
        JScrollPane scrollPane = new JScrollPane(jpanel);
        scrollPane.getViewport().setMaximumSize(new Dimension(1000, 400));

        pane.add(scrollPane, c);

    }

    private void addItemListeners()
    {
        docComboBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                JComboBox docCB = (JComboBox) e.getSource();
                String docName = (String) docCB.getSelectedItem();
                if (docName == null)
                {
                    return;
                }
                if (lastDocSelected == null)
                {
                    lastDocSelected = docName;
                    populateVerComboBoxes(docName);
                } else if (docName.compareTo(lastDocSelected) != 0)
                {
                    lastDocSelected = docName;
                    populateVerComboBoxes(docName);
                }

            }
        });
        rightVerComboBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                JComboBox verCB = (JComboBox) e.getSource();
                String verName = (String) verCB.getSelectedItem();
                if (verName == null)
                {
                    return;
                }
                if (lastRightVerSel == null)
                {
                    lastRightVerSel = verName;
                    if (lastDocSelected != null)
                    {
                        rightTextScreen.display(lastDocSelected, verName);
                    }
                } else if (verName.compareTo(lastRightVerSel) != 0)
                {
                    lastRightVerSel = verName;
                    if (lastDocSelected != null)
                    {
                        rightTextScreen.display(lastDocSelected, verName);
                    }
                }
            }
        });
        leftVerComboBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                JComboBox verCB = (JComboBox) e.getSource();
                String verName = (String) verCB.getSelectedItem();
                if (verName == null)
                {
                    return;
                }
                if (lastLeftVerSel == null)
                {
                    lastLeftVerSel = verName;
                    if (lastDocSelected != null)
                    {
                        leftTextScreen.display(lastDocSelected, verName);
                    }
                } else if (verName.compareTo(lastLeftVerSel) != 0)
                {
                    lastLeftVerSel = verName;
                    if (lastDocSelected != null)
                    {
                        leftTextScreen.display(lastDocSelected, verName);
                    }
                }
            }
        });
    }

    private void addMenu()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem exitMenuItem = new JMenuItem("Close Window");
        exitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                exit();
            }
        });
        fileMenu.add(exitMenuItem);


        JMenu preferencesMenu = new JMenu("Preferences");
        menuBar.add(preferencesMenu);

        JCheckBoxMenuItem autoAddAllMenuItem =
                new JCheckBoxMenuItem("Auto Add All");
        autoAddAllMenuItem.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.getSource();
                boolean b = checkBox.isSelected();
                leftTextScreen.setAutoAddAll(b);
                rightTextScreen.setAutoAddAll(b);
            }
        });
        preferencesMenu.add(autoAddAllMenuItem);
        autoAddAllMenuItem.setSelected(true);

        JCheckBoxMenuItem autoHighlightMenuItem =
                new JCheckBoxMenuItem("Highlight Expansion");
        autoHighlightMenuItem.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.getSource();
                boolean b = checkBox.isSelected();
                leftTextScreen.setAutoHighlight(b);
                rightTextScreen.setAutoHighlight(b);
            }
        });
        preferencesMenu.add(autoHighlightMenuItem);
        autoHighlightMenuItem.setSelected(true);

        JMenu highlightMenu = new JMenu("Highlight");
        ArrayList<String> labels =
                new ArrayList(viewModel.getLabels().keySet());
        Collections.sort(labels);
        for (final String label
                : labels)
        {
            JCheckBoxMenuItem labelCheckBox = new JCheckBoxMenuItem(label);
            labelCheckBox.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e)
                {
                    JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) e.
                            getSource();
                    boolean b = checkBox.isSelected();
                    // Only change highlight if boolean values differ
                    if (viewModel.isEntityTypeHighlighted(label) ^ b)
                    {
                        viewModel.setEntityTypeHighlight(label, b);
                        viewModel.repaintView();
                    }
                }
            });
            labelCheckBox.setSelected(true);
            highlightMenu.add(labelCheckBox);
        }
        menuBar.add(highlightMenu);

        this.setJMenuBar(menuBar);
    }

    private void attachWindowListener()
    {
        this.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {   // Do nothing
            }

            @Override
            public void windowClosing(WindowEvent e)
            {
                closing();
            }

            @Override
            public void windowClosed(WindowEvent e)
            {   // Do nothing
            }

            @Override
            public void windowIconified(WindowEvent e)
            {   // Do nothing
            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {   // Do nothing
            }

            @Override
            public void windowActivated(WindowEvent e)
            {   // Do nothing
            }

            @Override
            public void windowDeactivated(WindowEvent e)
            {   // Do nothing
            }
        });
    }

    /**
     * Detach views before closing
     */
    private void closing()
    {
        System.out.println("Detaching views");
        leftTextScreen.emptyScreen();
        rightTextScreen.emptyScreen();
        viewModel.detachView(leftTextScreen);
        viewModel.detachView(rightTextScreen);
        dispose();
    }

    private void exit()
    {
        closing();
    }

    private void populateVerComboBoxes(String docName)
    {
        leftVerComboBox.removeAllItems();
        rightVerComboBox.removeAllItems();
        leftTextScreen.emptyScreen();
        rightTextScreen.emptyScreen();
        ArrayList<String> verNames;
        try
        {
            verNames = viewModel.getAvailableDocumentVersions(docName);
        } catch (Exception e)
        {
            controller.showErrorMessage("Could not load document versions");
            return;
        }
        Collections.sort(verNames);
        for (String verName
                : verNames)
        {
            leftVerComboBox.addItem(verName);
            rightVerComboBox.addItem(verName);
        }
        leftVerComboBox.repaint();
        rightVerComboBox.repaint();
        String lastLeftVerSel = (String) leftVerComboBox.getSelectedItem();
        String lastRightVerSel = (String) rightVerComboBox.getSelectedItem();
    }

    public static void showMultiEditorDialog(EvaluatorViewModel viewModel,
                                             EvaluatorController controller)
    {
        MultiEditorDialog meDialog =
                new MultiEditorDialog(viewModel, controller);
        meDialog.setVisible(true);
    }

    public class ScrollablePanel extends JPanel implements Scrollable
    {

        @Override
        public Dimension getPreferredScrollableViewportSize()
        {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect,
                                              int orientation, int direction)
        {
            return 10;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect,
                                               int orientation, int direction)
        {
            return ((orientation == SwingConstants.VERTICAL) ? visibleRect.height : visibleRect.width) - 10;
        }

        @Override
        public boolean getScrollableTracksViewportWidth()
        {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight()
        {
            return false;
        }
    }
}
