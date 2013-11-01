/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.tree;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import edu.sfsu.evaluator.view.EvaluatorView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author eric
 */
public class EvaluatorCorpusTree extends JTree implements EvaluatorView
{

    private EvaluatorController controller;
    private EvaluatorViewModel viewModel;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private TreePath lastSelectionPath = null;
    private boolean popupShowing = false;

    public EvaluatorCorpusTree(
            EvaluatorViewModel viewModel,
            EvaluatorController controller)
    {
        super();
        this.controller = controller;
        this.viewModel = viewModel;
        root = new DefaultMutableTreeNode("No Workspace Loaded");
        model = (DefaultTreeModel) this.getModel();
        model.setRoot(root);
    }

    /**
     * Attach mouse listeners.
     */
    private void attachMouseListeners()
    {
        // Mouse motion listener attached
        this.addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {   // Do nothing
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
                mouseListenerMouseMoved(e);
            }
        });

        // Mouse click listener attached
        this.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                mouseListenerMouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e)
            {   // Do nothing
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {   // Do nothing
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {   // Do nothing
            }

            @Override
            public void mouseExited(MouseEvent e)
            {   // Do nothing
            }
        });
    }

    @Override
    public void display(String docName, String verName)
    {   // Do nothing
    }

    @Override
    public void repaintView()
    {
        ArrayList<String> docNames =
                viewModel.getAvailableDocuments();
        repaintTreeBranch(root, docNames);
        for (int i = 0;
                i < root.getChildCount();
                i++)
        {
            DefaultMutableTreeNode docNode =
                    (DefaultMutableTreeNode) root.getChildAt(i);
            String docName = (String) docNode.getUserObject();
            ArrayList<String> verNames;
            try
            {
                verNames = viewModel.getAvailableDocumentVersions(docName);
                repaintTreeBranch(docNode, verNames);
            } catch (Exception e)
            {
                controller.showErrorMessage("Could not document versions");
            }
        }
    }

    /**
     * Update tree node. All existing child nodes must contain String user
     * objects and be of type DefaultMutableTreeNode. Children must already be
     * alphabetized.
     * <p/>
     * @param parent
     * @param strings
     */
    private void repaintTreeBranch(
            DefaultMutableTreeNode parent,
            ArrayList<String> strings)
    {
        Collections.sort(strings);
        boolean changed = false; // To determine if model must be reloaded
        int i = 0;
        for (String newString
                : strings)
        {
            // Go until String is inserted or matched
            boolean insertedOrMatched = false;
            do
            {
                if (i >= parent.getChildCount())
                {
                    DefaultMutableTreeNode newStringNode =
                            new DefaultMutableTreeNode(newString);
                    model.insertNodeInto(newStringNode, parent, i);
                    i++;
                    insertedOrMatched = true; // Inserted
                    changed = true;
                } else
                {
                    DefaultMutableTreeNode oldStringNode =
                            (DefaultMutableTreeNode) parent.getChildAt(i);
                    String oldString =
                            (String) oldStringNode.getUserObject();
                    int compare = newString.compareTo(oldString);
                    if (compare < 0)
                    {
                        DefaultMutableTreeNode newStringNode =
                                new DefaultMutableTreeNode(newString);
                        model.insertNodeInto(newStringNode, parent, i);
                        i++;
                        insertedOrMatched = true; // Inserted
                        changed = true;
                    } else if (compare > 0)
                    {
                        model.removeNodeFromParent(oldStringNode);
                        changed = true;
                    } else
                    {
                        i++;
                        insertedOrMatched = true; // Matched
                    }
                }
            } while (!insertedOrMatched);
        }

        // Done with new Strings, all remaining Strings must be different
        while (i < parent.getChildCount())
        {
            model.removeNodeFromParent(
                    (DefaultMutableTreeNode) parent.getChildAt(i));
            changed = true;
        }
        if (changed)
        {
            model.reload(parent);
        }
    }

    /**
     * Mouse clicked.
     */
    private void mouseListenerMouseClicked(MouseEvent e)
    {
        int x = e.getX(), y = e.getY();
        TreePath path = this.getPathForLocation(x, y);
        if (path == null)
        {
            return;
        }

        int col = path.getPath().length;
        if (col == 2)
        {
            String docName = (String) ((DefaultMutableTreeNode) path.getPath()[1]).
                    getUserObject();
            DocumentPopup docPopup = new DocumentPopup(docName);
            if (e.getButton() == MouseEvent.BUTTON3)
            {
                docPopup.show(this, x, y);
            } else if (e.getButton() == MouseEvent.BUTTON1)
            {
                try
                {
                    if (viewModel
                            .getAvailableDocumentVersions(docName).isEmpty())
                    {
                        controller.requestDocumentVersionAdd(docName);
                        viewModel.repaintView();
                        this.expandPath(path);
                    }
                } catch (Exception ex)
                {
                }
            }
        } else if (col == 3)
        {
            String docName = (String) ((DefaultMutableTreeNode) path.getPath()[1]).
                    getUserObject();
            String verName = (String) ((DefaultMutableTreeNode) path.getPath()[2]).
                    getUserObject();
            DocumentVersionPopup verPopup =
                    new DocumentVersionPopup(docName, verName);
            if (e.getButton() == MouseEvent.BUTTON1)
            {
                controller.displayDocumentVersion(docName, verName);
            } else if (e.getButton() == MouseEvent.BUTTON3)
            {
                verPopup.show(this, x, y);
            }

        }

    }

    /**
     * Mouse moved.
     */
    private void mouseListenerMouseMoved(MouseEvent e)
    {
        if (popupShowing)
        {
            return;
        }
        int x = e.getX(), y = e.getY();
        TreePath path = this.getPathForLocation(x, y);
        if (lastSelectionPath != null)
        {
            this.removeSelectionPath(lastSelectionPath);
        }
        if (path == null)
        {
            lastSelectionPath = null;
        } else
        {
            this.setSelectionPath(path);
            lastSelectionPath = path;
        }
    }

    @Override
    public void updateDocuentRenamed(String oldDocName, String newDocName)
    {
        repaintView();
    }

    @Override
    public void updateDocumentVersionRenamed(String docName, String oldVerName,
                                             String newVerName)
    {
        repaint();
    }

    @Override
    public void updateModelSet()
    {
        root.setUserObject("Corpus");
        root.removeAllChildren();
        model.reload();
        repaintView();
        attachMouseListeners();
    }

    /**
     * Popup to display when a document is selected.
     */
    private class DocumentPopup extends JPopupMenu
    {

        public DocumentPopup(final String docName)
        {
            // New version menu item
            JMenuItem newVersionMenuItem = new JMenuItem("New Version");
            newVersionMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.requestDocumentVersionAdd(docName);
                    viewModel.repaintView();
                }
            });
            this.add(newVersionMenuItem);

            // Rename menu item
            JMenuItem renameMenuItem = new JMenuItem("Rename");
            renameMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.requestDocumentRename(docName);
                    viewModel.repaintView();
                }
            });
            this.add(renameMenuItem);

            // Delete menu item
            JMenuItem deleteMenuItem = new JMenuItem("Delete");
            deleteMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.requestDocumentDelete(docName);
                    viewModel.repaintView();
                }
            });
            this.add(deleteMenuItem);
            
            // calc menu item
            JMenuItem calcMenuItem = new JMenuItem("Evaluate Document");
            calcMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.requestEvaluateMeasurement(docName);
                }
            });
            this.add(calcMenuItem);

            // Stop automatic tree selection on popup display
            this.addPopupMenuListener(new PopupMenuListener()
            {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e)
                {
                    popupShowing = true;
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
                {
                    popupShowing = false;
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e)
                {
                    popupShowing = false;
                }
            });
        }
    }

    /**
     * Popup to display when
     */
    private class DocumentVersionPopup extends JPopupMenu
    {

        public DocumentVersionPopup(final String docName, final String verName)
        {
            // Display menu item
            JMenuItem openMenuItem = new JMenuItem("Display");
            openMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.displayDocumentVersion(docName, verName);
                }
            });
            this.add(openMenuItem);

            // Rename menu item
            JMenuItem renameMenuItem = new JMenuItem("Rename");
            renameMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.requestDocumentVersionRename(docName, verName);
                    viewModel.repaintView();
                }
            });
            this.add(renameMenuItem);

            // Delete menu item
            JMenuItem deleteMenuItem = new JMenuItem("Delete");
            deleteMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.requestDocumentVersionDelete(docName, verName);
                    viewModel.repaintView();
                }
            });
            this.add(deleteMenuItem);
            
            // Delete menu item
            JMenuItem importMenuItem = new JMenuItem("Import Entities");
            importMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.requestImport(docName, verName);
                }
            });
            this.add(importMenuItem);
            
            // Set as BaseLine menu item
            JMenuItem setBaseLineMenuItem = new JMenuItem("Set As BaseLine");
            setBaseLineMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.requestSetBaseLine(docName, verName);
                }
            });
            this.add(setBaseLineMenuItem);

            // Stop automatic tree selection on popup display
            this.addPopupMenuListener(new PopupMenuListener()
            {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e)
                {
                    popupShowing = true;
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
                {
                    popupShowing = false;
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e)
                {
                    popupShowing = false;
                }
            });
        }
    }
}
