/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.tree;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import edu.sfsu.evaluator.model.Entity;
import edu.sfsu.evaluator.model.ComplexEntity;
import edu.sfsu.evaluator.model.ComplexEntityRule;
import edu.sfsu.evaluator.model.ComplexEntityRuleNode;
import edu.sfsu.evaluator.view.EvaluatorView;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 *
 * @author eric
 */
public class EvaluatorEntityTree extends JTree implements EvaluatorView
{

    private EvaluatorViewModel viewModel;
    private EvaluatorController controller;
    private DefaultMutableTreeNode complexEntityNode;
    private DefaultMutableTreeNode annotationNode;
    private DefaultTreeModel model;
    public static final String ROOT = "root node";
    private boolean docLoaded = false;
    private String docName = null;
    private String verName = null;
    protected String COMPLEX_ENTITIES = "Complex Entities";
    protected String ANNOTATIONS = "Annotations";
    private MouseListener mouseListener = null;

    public EvaluatorEntityTree(
            EvaluatorViewModel viewModel,
            EvaluatorController controller)
    {
        this.viewModel = viewModel;
        this.controller = controller;

        model = (DefaultTreeModel) this.getModel();

        complexEntityNode = new DefaultMutableTreeNode(COMPLEX_ENTITIES);
        annotationNode = new DefaultMutableTreeNode(ANNOTATIONS);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(ROOT);

        root.add(annotationNode);
        root.add(complexEntityNode);

        this.setCellRenderer(new AnnotationTreeRenderer());

        model.setRoot(root);
    }

    /**
     * Construct tree.
     * <p/>
     * @param docName
     * @param verName
     */
    @Override
    public void display(String docName, String verName)
    {
        this.docName = docName;
        this.verName = verName;
        docLoaded = true;

        repaintView();
    }

    /**
     * Empty tree and remove mouse listeners.
     */
    private void emptyTree()
    {
        for (int i = 0;
                i < annotationNode.getChildCount();
                i++)
        {
            ((DefaultMutableTreeNode) annotationNode.getChildAt(i)).
                    removeAllChildren();
        }
        for (int i = 0;
                i < complexEntityNode.getChildCount();
                i++)
        {
            ((DefaultMutableTreeNode) complexEntityNode.getChildAt(i))
                    .removeAllChildren();
        }
        docLoaded = false;
        model.reload();
    }

    /**
     * Is this document being displayed?
     * <p/>
     * @param docName
     * @return
     */
    public boolean isDisplaying(String docName)
    {
        if (!docLoaded)
        {
            return false;
        }
        return (this.docName.compareTo(docName) == 0);
    }

    /**
     * Is this document version being displayed?
     * <p/>
     * @param docName
     * @param verName
     * @return
     */
    public boolean isDisplaying(String docName, String verName)
    {
        if (!docLoaded)
        {
            return false;
        }
        return ((this.docName.compareTo(docName) == 0)
                && (this.verName.compareTo(verName) == 0));
    }

    public void mouseListenerMouseClicked(MouseEvent e)
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
            String branchName =
                    (String) ((DefaultMutableTreeNode) path.getPath()[1]).
                    getUserObject();
            if (branchName.compareTo(ANNOTATIONS) == 0)
            {
            }
        } else if (col == 3)
        {
            String branchName = (String) ((DefaultMutableTreeNode) path.
                    getPath()[1]).getUserObject();
            if (branchName.compareTo(ANNOTATIONS) == 0)
            {
                SimpleEntry<String, Color> annoEntry =
                        (SimpleEntry) ((DefaultMutableTreeNode) path.getPath()[2]).
                        getUserObject();
                String entityType = annoEntry.getKey();
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    controller.setEntityHighlight(entityType, !viewModel.isEntityTypeHighlighted(entityType));
                    viewModel.repaintView();
                } else if (e.getButton() == MouseEvent.BUTTON3)
                {
                    AnnotationPopup annoPopup = new AnnotationPopup(entityType);
                    annoPopup.show(this, x, y);
                }
            } else if (branchName.compareTo(COMPLEX_ENTITIES) == 0)
            {
                ComplexEntityRule entityRule = (ComplexEntityRule) ((DefaultMutableTreeNode) path.
                        getPath()[2]).getUserObject();
                String entityRuleName = entityRule.getComplexEntityType();
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    controller.setComplexEntityRuleHighlight(entityRuleName,
                                                             !viewModel.isComplexEntityTypeHighlighted(entityRuleName));
                    viewModel.repaintView();
                } else if (e.getButton() == MouseEvent.BUTTON3)
                {
                    ComplexEntityPopup entityPopup =
                            new ComplexEntityPopup(entityRule);
                    entityPopup.show(this, x, y);
                }
            }
        } else if (col == 4)
        {
            String branchName = (String) ((DefaultMutableTreeNode) path.
                    getPath()[1]).getUserObject();
            if (branchName.compareTo(ANNOTATIONS) == 0)
            {
                Entity annotation =
                        (Entity) ((DefaultMutableTreeNode) path.getPath()[3]).
                        getUserObject();
                controller.displayEntity(docName, verName, annotation);
            }
        } else if (col == 5)
        {
            String branchName = (String) ((DefaultMutableTreeNode) path.
                    getPath()[1]).getUserObject();
            if (branchName.compareTo(COMPLEX_ENTITIES) == 0)
            {
                Entity annotation =
                        (Entity) ((DefaultMutableTreeNode) path.getPath()[4]).
                        getUserObject();
                controller.displayEntity(docName, verName, annotation);
            }
        }
    }

    private void repaintAnnotations()
    {
        HashMap<String, Color> labels = viewModel.getEntityTypes();
        ArrayList<String> labelNames = new ArrayList(labels.keySet());
        Collections.sort(labelNames);

        // THIS ALGORITHM ASSUMES THAT TREE NODES ARE SORTED ALPHABETICALLY
        // Update annotation types (labels)
        boolean changed = false; // To determine if model must be reloaded
        int i = 0;
        for (String newLabelName
                : labelNames)
        {
            // Go until entityType is inserted or matched
            boolean insertedOrMatched = false;
            do
            {
                if (i >= annotationNode.getChildCount())
                {
                    SimpleEntry<String, Color> entry =
                            new SimpleEntry(newLabelName, labels.get(
                            newLabelName));
                    DefaultMutableTreeNode newLabelNode =
                            new DefaultMutableTreeNode(entry);
                    model.insertNodeInto(newLabelNode, annotationNode, i);
                    i++;
                    insertedOrMatched = true; // Inserted
                    changed = true;
                } else
                {
                    DefaultMutableTreeNode oldLabelNode =
                            (DefaultMutableTreeNode) annotationNode.
                            getChildAt(i);
                    SimpleEntry<String, Color> oldLabelEntry =
                            (SimpleEntry) oldLabelNode.getUserObject();
                    int compare = newLabelName.compareTo(oldLabelEntry.getKey());
                    if (compare < 0)
                    {
                        SimpleEntry<String, Color> entry =
                                new SimpleEntry(newLabelName, labels.get(
                                newLabelName));
                        DefaultMutableTreeNode newLabelNode =
                                new DefaultMutableTreeNode(entry);
                        model.insertNodeInto(newLabelNode, annotationNode, i);
                        i++;
                        insertedOrMatched = true; // Inserted
                        changed = true;
                    } else if (compare > 0)
                    {
                        model.removeNodeFromParent(oldLabelNode);
                        changed = true;
                    } else
                    {
                        Color newColor = labels.get(newLabelName);
                        if (!oldLabelEntry.getValue().equals(newColor))
                        {
                            oldLabelEntry.setValue(newColor);
                            changed = true;
                        }
                        i++;
                        insertedOrMatched = true; // Matched
                    }
                }
            } while (!insertedOrMatched);
        }
        while (i < annotationNode.getChildCount())
        {
            model.removeNodeFromParent(
                    (DefaultMutableTreeNode) annotationNode.getChildAt(i));
            changed = true;
        }
        if (changed)
        {
            model.reload(annotationNode);
        }

        // If document is not loaded then don't add individual annotations
        if (!docLoaded)
        {
            return;
        }

        ArrayList<Entity> annotations;
        try
        {
            annotations = viewModel.getEntities(docName, verName);
        } catch (Exception e)
        {
            controller.showErrorMessage("Could not load annotations");
            return;
        }
        // Make another pass through to add annotations
        for (i = 0;
                i < annotationNode.getChildCount();
                i++)
        {
            DefaultMutableTreeNode aNode =
                    (DefaultMutableTreeNode) annotationNode.getChildAt(i);
            String labelName =
                    ((SimpleEntry<String, Color>) aNode.getUserObject()).
                    getKey();
            ArrayList<Entity> toAdd = new ArrayList();
            for (Entity a
                    : annotations)
            {
                if (labelName.compareTo(a.getEntityType()) == 0)
                {
                    toAdd.add(a);
                }
            }
            Collections.sort(toAdd, Entity.AnnotationComparator);
            // To determine if model must be reloaded
            changed = false;
            int j = 0;
            for (Entity newAnno
                    : toAdd)
            {
                boolean insertedOrMatched = false;
                // Go until this annotation is inserted or matched
                do
                {
                    if (j >= aNode.getChildCount())
                    {
                        DefaultMutableTreeNode newAnnoNode =
                                new DefaultMutableTreeNode(newAnno);
                        model.insertNodeInto(newAnnoNode, aNode, j);
                        j++;
                        insertedOrMatched = true; // Inserted
                        changed = true;
                    } else
                    {
                        DefaultMutableTreeNode oldAnnoNode =
                                (DefaultMutableTreeNode) aNode.getChildAt(j);
                        Entity oldAnno =
                                (Entity) oldAnnoNode.getUserObject();
                        int compare = newAnno.compareTo(oldAnno);
                        if (compare < 0)
                        {
                            DefaultMutableTreeNode newAnnoNode =
                                    new DefaultMutableTreeNode(newAnno);
                            model.insertNodeInto(newAnnoNode, aNode, j);
                            j++;
                            insertedOrMatched = true; // Inserted
                            changed = true;
                        } else if (compare > 0)
                        {
                            model.removeNodeFromParent(oldAnnoNode);
                            changed = true;
                        } else
                        {
                            j++;
                            insertedOrMatched = true; // Matched
                        }
                    }
                } while (!insertedOrMatched);
            }
            while (j < aNode.getChildCount())
            {
                model.removeNodeFromParent(
                        (DefaultMutableTreeNode) aNode.getChildAt(j));
                changed = true;
            }
            if (changed)
            {
                model.reload(aNode);
            }
        }
    }

    private void repaintComplexEntities()
    {
        ArrayList<ComplexEntityRule> entityRules =
                viewModel.getComplexEntityRules();
        Collections.sort(entityRules,
                         ComplexEntityRule.ComplexEntityRuleComparator);

        boolean changed = false; // To determine if model must be reloaded
        int i = 0;
        for (ComplexEntityRule newEntityRule
                : entityRules)
        {
            // Go until entity rule has been inserted or matched
            boolean insertedOrMatched = false;
            do
            {
                if (i >= complexEntityNode.getChildCount())
                {
                    model.insertNodeInto(
                            new DefaultMutableTreeNode(newEntityRule),
                            complexEntityNode, i);
                    i++;
                    insertedOrMatched = true; // Inserted
                    changed = true;
                } else
                {
                    DefaultMutableTreeNode oldEntityNode =
                            (DefaultMutableTreeNode) complexEntityNode.
                            getChildAt(i);
                    ComplexEntityRule oldEntityRule =
                            (ComplexEntityRule) oldEntityNode.getUserObject();
                    int compare = newEntityRule.compareTo(oldEntityRule);
                    if (compare < 0)
                    {
                        model.insertNodeInto(
                                new DefaultMutableTreeNode(newEntityRule),
                                complexEntityNode, i);
                        i++;
                        insertedOrMatched = true; // Inserted
                        changed = true;
                    } else if (compare > 0)
                    {
                        model.removeNodeFromParent(oldEntityNode);
                        changed = true;
                    } else
                    {
                        i++;
                        insertedOrMatched = true; // Matched
                    }
                }
            } while (!insertedOrMatched);
        }
        // Remove all remaining nodes
        while (i < complexEntityNode.getChildCount())
        {
            model.removeNodeFromParent(
                    (DefaultMutableTreeNode) complexEntityNode.getChildAt(i));
            changed = true;
        }
        if (changed)
        {
            model.reload(complexEntityNode);
        }

        if (!docLoaded)
        {
            return;
        }

        ArrayList<ComplexEntity> complexEntities;
        try
        {
            complexEntities = viewModel.getComplexEntities(docName, verName);
        } catch (Exception e)
        {
            controller.showErrorMessage("Could not load complexEntities");
            return;
        }

        // Make another pass through to add annotations
        for (i = 0;
                i < complexEntityNode.getChildCount();
                i++)
        {
            DefaultMutableTreeNode ceNode =
                    (DefaultMutableTreeNode) this.complexEntityNode.
                    getChildAt(i);
            String entityName =
                    ((ComplexEntityRule) ceNode.getUserObject()).getComplexEntityType();
            ArrayList<ComplexEntity> toAdd = new ArrayList();
            for (ComplexEntity ce
                    : complexEntities)
            {
                if (entityName.compareTo(ce.getComplexEntityType()) == 0)
                {
                    toAdd.add(ce);
                }
            }
            Collections.sort(toAdd, ComplexEntity.ComplexEntityComparator);
            changed = false; // To determine if model must be reloaded
            int j = 0;
            for (ComplexEntity newEntity
                    : toAdd)
            {
                boolean insertedOrMatched = false;
                // Go until this annotation is inserted or matched
                do
                {
                    if (j >= ceNode.getChildCount())
                    {
                        DefaultMutableTreeNode newEntityNode =
                                new DefaultMutableTreeNode(newEntity);
                        // Add annotations as children of entity node
                        ArrayList<Entity> entityAnnos =
                                newEntity.getSubEntities();
                        for (int k = 0;
                                k < entityAnnos.size();
                                k++)
                        {
                            newEntityNode.insert(
                                    new DefaultMutableTreeNode(
                                    entityAnnos.get(k)), k);
                        }
                        model.insertNodeInto(newEntityNode, ceNode, j);

                        j++;
                        insertedOrMatched = true; // Inserted
                        changed = true;
                    } else
                    {
                        DefaultMutableTreeNode oldEntityNode =
                                (DefaultMutableTreeNode) ceNode.getChildAt(j);
                        ComplexEntity oldEntity =
                                (ComplexEntity) oldEntityNode.getUserObject();
                        int compare = newEntity.compareTo(oldEntity);
                        if (compare < 0)
                        {
                            DefaultMutableTreeNode newEntityNode =
                                    new DefaultMutableTreeNode(newEntity);
                            model.insertNodeInto(newEntityNode, ceNode, j);
                            // Add annotations as children of entity node
                            ArrayList<Entity> entityAnnos =
                                    newEntity.getSubEntities();
                            for (int k = 0;
                                    k < entityAnnos.size();
                                    k++)
                            {
                                newEntityNode.insert(
                                        new DefaultMutableTreeNode(
                                        entityAnnos.get(k)), k);
                            }
                            j++;
                            insertedOrMatched = true; // Inserted
                            changed = true;
                        } else if (compare > 0)
                        {
                            model.removeNodeFromParent(oldEntityNode);
                            changed = true;
                        } else
                        {
                            j++;
                            insertedOrMatched = true; // Matched
                        }
                    }
                } while (!insertedOrMatched);
            }
            while (j < ceNode.getChildCount())
            {
                model.removeNodeFromParent(
                        (DefaultMutableTreeNode) ceNode.getChildAt(i));
                changed = true;
            }
            if (changed)
            {
                model.reload(ceNode);
            }
        }
    }

    @Override
    public void repaintView()
    {
        // Check if workspace set
        if (!viewModel.isModelSet())
        {
            return;
        }

        // Check if document and version still exist
        if (docLoaded && (!viewModel.containsDocumentVersion(docName, verName)))
        {
            emptyTree(); // if not empty tree
        }

        // If all check out update trees
        repaintAnnotations();
        repaintComplexEntities();
        this.repaint(); // Repaint highlights
    }

    @Override
    public void updateDocuentRenamed(String oldDocName, String newDocName)
    {
        if (isDisplaying(oldDocName))
        {
            this.docName = newDocName;
        }
    }

    @Override
    public void updateDocumentVersionRenamed(
            String docName,
            String oldVerName,
            String newVerName)
    {
        if (isDisplaying(docName, oldVerName))
        {
            this.verName = newVerName;
        }
    }

    @Override
    public void updateModelSet()
    {
        emptyTree();

        repaintAnnotations();
        repaintComplexEntities();

        if (mouseListener == null)
        {
            mouseListener = new MouseListener()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    mouseListenerMouseClicked(e);
                }

                @Override
                public void mousePressed(MouseEvent e)
                {
                }

                @Override
                public void mouseReleased(MouseEvent e)
                {
                }

                @Override
                public void mouseEntered(MouseEvent e)
                {
                }

                @Override
                public void mouseExited(MouseEvent e)
                {
                }
            };
            this.addMouseListener(mouseListener);
        }
    }

    private class AnnotationPopup extends JPopupMenu
    {

        public AnnotationPopup(final String annotationType)
        {
            // Change newColor
            JMenuItem changeColorMenuItem = new JMenuItem("Change color");
            changeColorMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Color currentColor =
                            viewModel.getEntityTypes().get(annotationType);
                    Color newColor =
                            JColorChooser.showDialog(
                            controller.getEvaluatorFrame(),
                            "Choose new color", currentColor);
                    if (newColor == null)
                    {
                        return;
                    }
                    if (newColor.equals(currentColor))
                    {
                        return;
                    }
                    controller.requestEntityTypeColorChange(
                            annotationType, newColor);
                    viewModel.repaintView();
                }
            });
            this.add(changeColorMenuItem);

            // Delete
            JMenuItem deleteMenuItem = new JMenuItem("Delete");
            deleteMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String message = String.format(
                            "Are you absolutely sure you want to delete "
                            + "annotation type '%s'?", annotationType);
                    int result = JOptionPane.showConfirmDialog(
                            controller.getEvaluatorFrame(), message,
                            "Please Confirm", JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.OK_OPTION)
                    {
                        controller.requestEntityTypeDelete(annotationType);
                        viewModel.repaintView();
                    }
                }
            });
            this.add(deleteMenuItem);
        }
    }

    private class ComplexEntityPopup extends JPopupMenu
    {

        public ComplexEntityPopup(final ComplexEntityRule complexEntityRule)
        {
            // Change newColor
            JMenuItem changeColorMenuItem = new JMenuItem("Change color");
            changeColorMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Color currentColor = complexEntityRule.getColor();
                    Color newColor =
                            JColorChooser.showDialog(
                            controller.getEvaluatorFrame(),
                            "Choose new color", currentColor);
                    if (newColor == null)
                    {
                        return;
                    }
                    if (newColor.equals(currentColor))
                    {
                        return;
                    }
                    controller.requestComplexEntityRuleColorChanged(
                            complexEntityRule.getComplexEntityType(), newColor);
                    viewModel.repaintView();
                }
            });
            this.add(changeColorMenuItem);

            // Delete
            JMenuItem deleteMenuItem = new JMenuItem("Delete");
            deleteMenuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String message = String.format(
                            "Are you absolutely sure you want to delete "
                            + "complex entity type '%s'?",
                            complexEntityRule.getComplexEntityType());
                    int result = JOptionPane.showConfirmDialog(
                            controller.getEvaluatorFrame(), message,
                            "Please Confirm", JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.OK_OPTION)
                    {
                        controller.requestComplexEntityRuleDelete(
                                complexEntityRule.getComplexEntityType());
                        viewModel.repaintView();
                    }
                }
            });
            this.add(deleteMenuItem);
        }
    }

    private class AnnotationTreeRenderer implements TreeCellRenderer
    {

        private DefaultTreeCellRenderer annotationRenderer = new DefaultTreeCellRenderer();
        Color selectionBorderColor, selectionForeground, selectionBackground,
                textForeground, textBackground;
        Font normalFont, labelFont;

        public AnnotationTreeRenderer()
        {

            Font fontValue;
            fontValue = UIManager.getFont("Tree.font");
            if (fontValue == null)
            {
                fontValue = annotationRenderer.getFont();
            }
            normalFont = new Font(fontValue.getName(), fontValue.getStyle(),
                                  fontValue.getSize());
            labelFont = new Font(fontValue.getName(), Font.BOLD,
                                 fontValue.getSize());
            selectionBorderColor = UIManager.getColor(
                    "Tree.selectionBorderColor");
            selectionForeground = UIManager.getColor("Tree.selectionForeground");
            selectionBackground = UIManager.getColor("Tree.selectionBackground");
            textForeground = UIManager.getColor("Tree.textForeground");
            textBackground = UIManager.getColor("Tree.textBackground");
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean selected,
                                                      boolean expanded,
                                                      boolean leaf, int row,
                                                      boolean hasFocus)
        {
            if (value instanceof DefaultMutableTreeNode)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObject = node.getUserObject();

                /* Render String */
                if (userObject instanceof String)
                {
                    if (((String) userObject).compareTo(
                            EvaluatorEntityTree.ROOT) == 0)
                    {
                        JLabel render =
                                (JLabel) annotationRenderer.
                                getTreeCellRendererComponent(tree, "", selected,
                                                             expanded, leaf, row,
                                                             hasFocus);
                        render.setIcon(new ImageIcon());
                        return render;
                    } else
                    {
                        JLabel render =
                                (JLabel) annotationRenderer.
                                getTreeCellRendererComponent(tree, userObject,
                                                             selected, expanded,
                                                             leaf, row, hasFocus);
                        render.setFont(labelFont);
                        render.setIcon(new ImageIcon());
                        return render;
                    }
                }

                if (userObject instanceof Entity)
                {
                    Entity a = (Entity) userObject;
                    String text = a.toString();
                    JLabel render =
                            (JLabel) annotationRenderer.
                            getTreeCellRendererComponent(
                            tree, text, selected, expanded, leaf, row, hasFocus);
                    return render;
                }

                if (userObject instanceof ComplexEntityRule)
                {
                    ComplexEntityRule entityRule = (ComplexEntityRule) userObject;
                    String title = String.format("%s - ", entityRule.getComplexEntityType());
                    for (ComplexEntityRuleNode ruleNode
                            : entityRule.getRuleNodes())
                    {
                        if (ruleNode.isOptional())
                        {
                            title += String.format("[%s] ", ruleNode.
                                    getAnnotationType());
                        } else
                        {
                            title += String.format("{%s} ", ruleNode.
                                    getAnnotationType());
                        }
                    }
                    JLabel render =
                            (JLabel) annotationRenderer.
                            getTreeCellRendererComponent(tree, title, selected,
                                                         expanded, leaf, row,
                                                         hasFocus);
                    if (viewModel.isComplexEntityTypeHighlighted(entityRule.getComplexEntityType()))
                    {
                        render.setForeground(entityRule.getColor());
                    }
                    return render;
                }

                if (userObject instanceof ComplexEntity)
                {
                    ComplexEntity complexEntity =
                            (ComplexEntity) userObject;
                    Integer start =
                            complexEntity.getSubEntities().get(0).getStart();
                    JLabel render =
                            (JLabel) annotationRenderer.
                            getTreeCellRendererComponent(tree, start.toString(),
                                                         selected, expanded,
                                                         leaf, row, hasFocus);
                    return render;
                }

                /* Render annotation type */
                if (userObject instanceof SimpleEntry)
                {
                    SimpleEntry<String, Color> label =
                            (SimpleEntry) userObject;
                    String text = label.getKey();
                    Color color = label.getValue();

                    JLabel render =
                            (JLabel) annotationRenderer.
                            getTreeCellRendererComponent(
                            tree, text, selected, expanded, leaf, row, hasFocus);

                    if (selected)
                    {
                        render.setBackground(selectionBackground);
                    } else
                    {
                        render.setBackground(textBackground);
                    }

                    if (viewModel.isEntityTypeHighlighted(text))
                    {
                        render.setForeground(color);
                        render.setFont(labelFont);
                    } else
                    {
                        render.setForeground(textForeground);
                        render.setFont(normalFont);
                    }

                    return render;
                }
            }
            Component defaultRender =
                    annotationRenderer.getTreeCellRendererComponent(
                    tree, value, selected, expanded, leaf, row, hasFocus);
            defaultRender.setFont(normalFont);
            if (selected)
            {
                defaultRender.setBackground(selectionBackground);
            } else
            {
                defaultRender.setBackground(textBackground);
            }

            return defaultRender;
        }
    }
}
