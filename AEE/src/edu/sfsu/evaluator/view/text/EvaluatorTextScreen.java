/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.text;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import edu.sfsu.evaluator.model.Entity;
import edu.sfsu.evaluator.model.ComplexEntity;
import edu.sfsu.evaluator.model.ComplexEntityRule;
import edu.sfsu.evaluator.model.ComplexEntityRuleNode;
import edu.sfsu.evaluator.view.EvaluatorView;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author eric
 */
public class EvaluatorTextScreen extends JTextPane implements EvaluatorView
{

    private static Color overlapColor = Color.GRAY;
    private static final HashSet<Character> WHITE_LIST;
    private Caret caret;
    private MouseListener mouseListener = null;

    static
    {
        WHITE_LIST = new HashSet();
        char[] capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] lower = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (int i = 0;
                i < capitals.length;
                i++)
        {
            WHITE_LIST.add(Character.valueOf(capitals[i]));
        }
        for (int i = 0;
                i < lower.length;
                i++)
        {
            WHITE_LIST.add(Character.valueOf(lower[i]));
        }
    }
    private EvaluatorViewModel viewModel;
    private EvaluatorController controller;
    private StyledDocument doc;
    private String docName = null;
    private String verName = null;
    private boolean docLoaded = false;
    private boolean autoHighlight = true;
    private boolean autoAddAll = true;
    private boolean complexEntityMode = false;
    private ArrayList<Entity> complexEntityAnnotations;
    private ArrayList<String> validNextTypes;
    private int nextIndex;
    private ComplexEntityRule complexEntityRule;

    public EvaluatorTextScreen(
            EvaluatorViewModel viewModel,
            EvaluatorController controller)
    {
        this.viewModel = viewModel;
        this.controller = controller;
        complexEntityAnnotations = new ArrayList();
        doc = this.getStyledDocument();
        caret = this.getCaret();
        mouseListener = new TextScreenMouseListener();
        validNextTypes = new ArrayList();
        this.addMouseListener(mouseListener);
    }

    private void addAll(String text, String label)
    {
        if (!autoAddAll)
        {
            return;
        }
        try
        {
            if (text.length() == 0)
            {
                return;
            }

            ArrayList<Entity> similarAnnotations = new ArrayList();
            ArrayList<Entity> annosToAdd = new ArrayList();
            String docText = viewModel.getDocumentText(docName);
            int i = docText.indexOf(text, 0);
            while (i > 0)
            {
                int start = i;
                int end = i + text.length();
                similarAnnotations.add(new Entity(text, label, start, end));
                i = docText.indexOf(text, end);
            }
            ArrayList<Entity> docAnnotations =
                    viewModel.getEntities(docName, verName);
            for (Entity a
                    : similarAnnotations)
            {
                if (!docAnnotations.contains(a))
                {
                    annosToAdd.add(a);
                }
            }
            if (annosToAdd.isEmpty())
            {
                return;
            }


            for (Entity a
                    : annosToAdd)
            {
                controller.requestAddEntity(docName, verName, a);
            }
            viewModel.repaintView();

        } catch (Exception e)
        {
        }
    }

    private void addComplexEntity()
    {
        if (complexEntityAnnotations.size() < 2)
        {
            System.err.println("Complex entities must be larger than 2");
        } else
        {
            ComplexEntity complexEntity =
                    new ComplexEntity(complexEntityRule.getComplexEntityType(),
                                      complexEntityAnnotations);
            controller.requestComplexEntityAdd(docName, verName,
                                               complexEntity);
            viewModel.repaintView();
        }
        exitComplexEntityMode();
    }

    /**
     * Display document within a text window.
     * <p/>
     * @param docName
     * @param verName
     */
    @Override
    public void display(String docName, String verName)
    {
        try
        {
            doc.remove(0, doc.getLength());
            String docText = viewModel.getDocumentText(docName);
            doc.insertString(0, docText, new SimpleAttributeSet());
            this.docName = docName;
            this.verName = verName;
            docLoaded = true;
            caret.setDot(0);
            paintEntities();
        } catch (Exception e)
        {
            String message =
                    String.format("Error displaying document: %s --> %s",
                                  docName, verName);
            controller.showErrorMessage(verName);
        }
    }

    /**
     * Display a specific annotation
     */
    public void display(String docName, String verName, Entity annotation)
    {
        if (isDisplaying(docName, verName))
        {
            caret.setDot(annotation.getStart());
        }
    }

    /**
     * Empty text screen;
     */
    public void emptyScreen()
    {
        docLoaded = false;
        try
        {
            doc.remove(0, doc.getLength());
        } catch (Exception e)
        {
            String message = "Error emptying screen";
            controller.showErrorMessage(verName);
        }
    }

    /**
     *
     * @param complexEntityRule
     * @param start
     * @param i Index of last complex entity rule node
     */
    private void enterComplexEntityMode(
            ComplexEntityRule complexEntityRule,
            Entity start,
            int index)
    {
        this.nextIndex = index + 1;
        this.complexEntityRule = complexEntityRule;
        complexEntityAnnotations.clear();
        complexEntityAnnotations.add(start);
        validNextTypes.clear();
        ArrayList<ComplexEntityRuleNode> ruleNodes =
                complexEntityRule.getRuleNodes();
        for (int i = nextIndex;
                i < ruleNodes.size();
                i++)
        {
            ComplexEntityRuleNode ruleNode = ruleNodes.get(i);
            validNextTypes.add(ruleNode.getAnnotationType());
            if (!ruleNode.isOptional())
            {
                break;
            }
        }
        complexEntityMode = true;


        viewModel.repaintView();
    }

    private void exitComplexEntityMode()
    {
        complexEntityMode = false;
        this.repaint();
        viewModel.repaintView();
    }

    private void extendComplexEntity(Entity a)
    {
        ArrayList<ComplexEntityRuleNode> ruleNodes =
                complexEntityRule.getRuleNodes();
        for (int i = nextIndex;
                i < ruleNodes.size();
                i++)
        {
            ComplexEntityRuleNode ruleNode = ruleNodes.get(i);
            if (ruleNode.getAnnotationType().compareTo(a.getEntityType()) == 0)
            {
                complexEntityAnnotations.add(a);
                if (i == ruleNodes.size() - 1)
                {
                    addComplexEntity();
                } else
                {
                    nextIndex = i + 1;
                    for (int j = nextIndex;
                            j < ruleNodes.size();
                            j++)
                    {
                        ruleNode = ruleNodes.get(j);
                        validNextTypes.clear();
                        validNextTypes.add(ruleNode.getAnnotationType());
                        if (!ruleNode.isOptional())
                        {
                            break;
                        }
                    }
                    viewModel.repaintView();
                }
                return;
            }
        }
        exitComplexEntityMode();
        System.err.println("Error with extending complex entity");
    }

    /**
     * Generate annotation popup.
     * <p/>
     * @param text
     * @param start
     * @param end
     * @return
     */
    private JPopupMenu generateAnnoPopup(final String text, int start, int end)
    {
        JPopupMenu annoPopup = new JPopupMenu();
        HashMap<String, Color> labels = viewModel.getEntityTypes();
        for (final String label
                : labels.keySet())
        {
            // Var must be final to be used in action listener
            final Entity annotation =
                    new Entity(text, label, start, end);
            final String finalDocName = docName;
            final String finalVerName = verName;
            JMenuItem menuItem = new JMenuItem(label);
            Color color = labels.get(label);
            menuItem.setForeground(color);
            ActionListener annoListener = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.requestAddEntity(finalDocName,
                                                finalVerName, annotation);
                    viewModel.repaintView();
                    addAll(text, label);
                }
            };
            menuItem.addActionListener(annoListener);
            annoPopup.add(menuItem);
        }
        return annoPopup;
    }

    private JPopupMenu generateComplexEntityPopup(ArrayList<Entity> annotations)
    {
        JPopupMenu complexEntityPopup = new JPopupMenu();
        for (final Entity annotation
                : annotations)
        {
            String message = String.format("Add to Complex Entity: '%s -> %s'",
                                           annotation.getText(), annotation.getEntityType());
            JMenuItem menuItem = new JMenuItem(message);
            ActionListener extendComplexEntityMenuItem = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    extendComplexEntity(annotation);
                }
            };
            menuItem.addActionListener(extendComplexEntityMenuItem);
            complexEntityPopup.add(menuItem);
        }

        return complexEntityPopup;
    }

    /**
     * Generate delete popup.
     * <p/>
     * @param annotations
     * @return
     */
    private JPopupMenu generateSelectPopup(ArrayList<Entity> annotations)
    {
        JPopupMenu selectPopup = new JPopupMenu();
        final String finalDocName = docName;
        final String finalVerName = verName;

        ArrayList<ComplexEntityRule> entityRules =
                viewModel.getComplexEntityRules();

        /* Create complex entities */
        for (final Entity annotation
                : annotations)
        {
            for (final ComplexEntityRule entityRule
                    : entityRules)
            {
                ArrayList<ComplexEntityRuleNode> ruleNodes =
                        entityRule.getRuleNodes();
                for (int i = 0;
                        i < ruleNodes.size();
                        i++)
                {
                    ComplexEntityRuleNode ruleNode = ruleNodes.get(i);
                    if (ruleNode.getAnnotationType().compareTo(
                            annotation.getEntityType()) == 0)
                    {
                        String message = String.format(
                                "Create [%s]: '%s -> %s'",
                                entityRule.getComplexEntityType(), annotation.getText(),
                                annotation.getEntityType());
                        JMenuItem menuItem = new JMenuItem(message);
                        final int index = i;
                        ActionListener addComplexEntityListener = new ActionListener()
                        {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                enterComplexEntityMode(entityRule, annotation,
                                                       index);
                            }
                        };
                        menuItem.addActionListener(addComplexEntityListener);
                        selectPopup.add(menuItem);
                    }
                    if (!ruleNode.isOptional())
                    {
                        i = ruleNodes.size();
                    }
                }
            }
        }
        if (selectPopup.getSubElements().length > 0)
        {
            selectPopup.addSeparator();
        }

        /* Delete individual annotation */
        for (Entity annotation
                : annotations)
        {
            // Var must be final to be used in action listener
            final Entity finalAnnotation = annotation;
            String message = String.format("Delete: '%s -> %s'",
                                           annotation.getText(), annotation.getEntityType());
            JMenuItem menuItem = new JMenuItem(message);
            ActionListener deleteListener = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.requestAnnotationDelete(finalDocName,
                                                       finalVerName,
                                                       finalAnnotation);
                    viewModel.repaintView();
                }
            };
            menuItem.addActionListener(deleteListener);
            selectPopup.add(menuItem);
        }
        selectPopup.addSeparator();

        /* Delete all annotations */
        for (Entity annotation
                : annotations)
        {
            final Entity finalAnnotation = annotation;
            String message = String.format("Delete all: '%s -> %s'",
                                           annotation.getText(), annotation.getEntityType());
            JMenuItem menuItem = new JMenuItem(message);
            ActionListener deleteAllListener = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    String label = finalAnnotation.getEntityType();
                    String text = finalAnnotation.getText();
                    ArrayList<Entity> annotations;
                    try
                    {
                        annotations = viewModel.getEntities(
                                finalDocName, finalVerName);
                    } catch (Exception ex)
                    {
                        return;
                    }
                    for (Entity a
                            : annotations)
                    {
                        if ((a.getEntityType().compareTo(label) == 0)
                                && (a.getText().compareTo(text) == 0))
                        {
                            controller.requestAnnotationDelete(finalDocName,
                                                               finalVerName, a);
                        }
                    }
                    viewModel.repaintView();
                }
            };
            menuItem.addActionListener(deleteAllListener);
            selectPopup.add(menuItem);
        }
        return selectPopup;
    }

    /**
     * Get all annotations which overlap with point 'dot'.
     * <p/>
     * @param dot
     * @return
     */
    private ArrayList<Entity> getSelectedAnnotations(int dot)
    {
        ArrayList<Entity> annotations;
        ArrayList<Entity> overlappingAnnos = new ArrayList();
        try
        {
            annotations = viewModel.getEntities(docName, verName);
        } catch (Exception e)
        {
            String message = String.format(
                    "Could not load annotations for document: %s [%s]",
                    docName, verName);
            controller.showErrorMessage(message);
            return new ArrayList();
        }
        for (Entity annotation
                : annotations)
        {
            int start = annotation.getStart();
            int end = annotation.getEnd();
            if ((start <= dot) && (dot <= end))
            {
                overlappingAnnos.add(annotation);
            }
        }
        return overlappingAnnos;
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

    /**
     * Mouse button has been released.
     * <p/>
     * @param e
     */
    private void mouseButtonReleased(MouseEvent e)
    {
        if (!docLoaded)
        {
            return;
        }
        int dot = caret.getDot();
        int mark = caret.getMark();
        String docText;
        try
        {
            docText = viewModel.getDocumentText(docName);
        } catch (Exception ex)
        {
            controller.showErrorMessage(
                    String.format("Could not load document %s", docName));
            return;
        }
        if (complexEntityMode)
        {
            if (e.getButton() != MouseEvent.BUTTON1)
            {
                exitComplexEntityMode();
                return;
            }

            ArrayList<Entity> selectedAnnos = getSelectedAnnotations(dot);
            ArrayList<Entity> okAnnos = new ArrayList();
            for (Entity a
                    : selectedAnnos)
            {
                for (String annotationType
                        : validNextTypes)
                {
                    if (a.getEntityType().compareTo(annotationType) == 0)
                    {
                        okAnnos.add(a);
                        break;
                    }
                }
            }

            ArrayList<ComplexEntityRuleNode> ruleNodes = complexEntityRule.getRuleNodes();

            /* If annotations is empty find out if the rest of the complex
             * entity rules are optional */
            if (okAnnos.isEmpty())
            {

                for (int i = nextIndex;
                        i < ruleNodes.size();
                        i++)
                {
                    if (!ruleNodes.get(i).isOptional())
                    {
                        exitComplexEntityMode();
                        return;
                    }
                }
                addComplexEntity();
                return;
            }
            try
            {
                Rectangle popupLocation = this.modelToView(dot);
                JPopupMenu complexEntityPopup = this.generateComplexEntityPopup(
                        okAnnos);
                complexEntityPopup.show(this, popupLocation.x, popupLocation.y);
            } catch (BadLocationException exception)
            {
                System.out.println("Wait... really?");
            }
            return;
        }

        if (e.getButton() == MouseEvent.BUTTON1)
        {
            if (dot == mark)
            {
                ArrayList<Entity> selectedAnnos = getSelectedAnnotations(dot);
                if (!selectedAnnos.isEmpty())
                {
                    try
                    {
                        Rectangle popupLocation = this.modelToView(mark + 1);
                        JPopupMenu deletePopup =
                                this.generateSelectPopup(selectedAnnos);
                        deletePopup.show(this, popupLocation.x, popupLocation.y);
                    } catch (Exception ex)
                    {
                        controller.showErrorMessage(
                                "Error displaying delete popup");
                    }
                    return;
                }
            } else if (dot > mark)
            {
                int tmp = dot;
                dot = mark;
                mark = tmp;
            }
            if (autoHighlight)
            {
                // Auto highlight outwards
                while (dot > 0)
                {
                    if (WHITE_LIST.contains(docText.charAt(dot - 1)))
                    {
                        dot--;
                    } else
                    {
                        break;
                    }
                }
                while (mark < docText.length() - 1)
                {
                    if (WHITE_LIST.contains(docText.charAt(mark)))
                    {
                        mark++;
                    } else
                    {
                        break;
                    }
                }
                caret.setDot(dot);
                caret.moveDot(mark);
            }
            String text = docText.substring(dot, mark);
            boolean noWhiteChars = true;
            for (int i = 0;
                    i < text.length();
                    i++)
            {
                if (WHITE_LIST.contains(text.charAt(i)))
                {
                    noWhiteChars = false;
                    break;
                }
            }
            if (noWhiteChars)
            {
                return;
            }
            try
            {
                text = docText.substring(dot, mark);
                Rectangle popupLocation = this.modelToView(mark + 1);
                JPopupMenu annoPopup = this.generateAnnoPopup(text, dot, mark);
                annoPopup.show(this, popupLocation.x, popupLocation.y);
            } catch (BadLocationException exception)
            {
                System.out.println("Wait... really?");
            }
        }
    }

    private void mouseCursorExited(MouseEvent e)
    {
        if (complexEntityMode)
        {
            // exitComplexEntityMode();
        }
    }

    private void paintEntities()
    {
        ArrayList<Entity> annotations;
        try
        {
            annotations = viewModel.getEntities(docName, verName);
        } catch (Exception e)
        {
            controller.showErrorMessage(e);
            return;
        }
        /*
         if (complexEntityMode)
         {
         ArrayList<Annotation> validNextAnnotations = new ArrayList();
         for (Entity a : annotations)
         {
         for (String annoType : validNextTypes)
         {
         if (a.getComplexEntityType().compareTo(annoType) == 0)
         {
         validNextAnnotations.add(a);
         break;
         }
         }
         }
         for (Entity a : complexEntityAnnotations)
         {
         if (!validNextAnnotations.contains(a))
         {

         validNextAnnotations.add(a);
         }
         }
         paintEntities(validNextAnnotations);

         } else
         */

        ArrayList<Entity> highlightedAnnotations = new ArrayList();
        // Only use labels which are highlighted.
        for (Entity a
                : annotations)
        {
            if (viewModel.isEntityTypeHighlighted(a.getEntityType()))
            {
                highlightedAnnotations.add(a);
            }
        }
        paintAnnotations(highlightedAnnotations);

    }

    /**
     * Paint all annotations
     */
    private void paintAnnotations(ArrayList<Entity> annotations)
    {

        Collections.sort(annotations, Entity.AnnotationComparator);
        HashMap<String, Color> labels = viewModel.getEntityTypes();


        doc.setCharacterAttributes(0, doc.getLength(), new SimpleAttributeSet(),
                                   true);
        if (annotations.isEmpty())
        {
            return;
        }

        Entity currentAnno = annotations.get(0);
        int currentMark = currentAnno.getStart();
        // Algorithm to paint highlights
        for (int i = 0;
                i < annotations.size();
                i++)
        {
            Entity nextAnno;
            if (i >= annotations.size() - 1)
            {
                paintHighlight(currentMark, currentAnno.getEnd(),
                               labels.get(currentAnno.getEntityType()));
                break;
            } else
            {
                nextAnno = annotations.get(i + 1);
            }
            if (currentAnno.getEnd() <= nextAnno.getStart())
            {
                paintHighlight(currentMark, currentAnno.getEnd(),
                               labels.get(currentAnno.getEntityType()));
                currentAnno = nextAnno;
                currentMark = nextAnno.getStart();
            } else if (currentAnno.getEnd() <= nextAnno.getEnd())
            {
                paintHighlight(currentMark, nextAnno.getStart(),
                               labels.get(currentAnno.getEntityType()));
                paintHighlight(nextAnno.getStart(), currentAnno.getEnd(),
                               overlapColor);
                currentMark = currentAnno.getEnd();
                currentAnno = nextAnno;
            } else
            { /* currentAnno.getEnd() > nextAnno.getEnd() */
                paintHighlight(currentMark, nextAnno.getStart(),
                               labels.get(currentAnno.getEntityType()));
                paintHighlight(nextAnno.getStart(), nextAnno.getEnd(),
                               overlapColor);
                currentMark = nextAnno.getEnd();
            }
        }
        this.getCaret().setDot(this.getCaret().getDot());
    }

    private void paintComplexEntities(Graphics g)
    {
        try
        {
            ArrayList<ComplexEntity> complexEntities = viewModel.
                    getComplexEntities(docName, verName);
            for (ComplexEntity complexEntity
                    : complexEntities)
            {
                if (viewModel.isComplexEntityTypeHighlighted(complexEntity.getComplexEntityType()))
                {
                    paintComplexEntity(g, complexEntity);
                }
            }
        } catch (Exception e)
        {
            controller.showErrorMessage(e);
        }

    }

    private void paintComplexEntity(Graphics g, ComplexEntity complexEntity)
            throws BadLocationException
    {
        ArrayList<ComplexEntityRule> entityRules =
                viewModel.getComplexEntityRules();
        Color color = null;
        for (ComplexEntityRule entityRule
                : entityRules)
        {
            if (complexEntity.getComplexEntityType()
                    .compareTo(entityRule.getComplexEntityType()) == 0)
            {
                color = entityRule.getColor();
                break;
            }
        }
        if (color != null)
        {
            g.setColor(color);
        }
        ArrayList<Entity> annotations = complexEntity.getSubEntities();
        int x1, y1, x2, y2;
        for (int i = 0;
                i < annotations.size() - 1;
                i++)
        {
            Entity a1 = annotations.get(i);
            Entity a2 = annotations.get(i + 1);
            int midway = this.getFont().getSize() / 2;
            int mid1 = (a1.getStart() + a1.getEnd()) / 2;
            int mid2 = (a2.getStart() + a2.getEnd()) / 2;
            Rectangle r1 = this.getUI().modelToView(this, a1.getStart());
            Rectangle r2 = this.getUI().modelToView(this, a2.getStart());
            x1 = r1.x;
            y1 = r1.y + midway;
            x2 = r2.x;
            y2 = r2.y + midway;

            int r = 1;
            int d = r * 2;

            g.drawOval(x1 - r, y1 - r, d, d);
            g.drawOval(x2 - r, y2 - r, d, d);

            int n = 5;
            int minYOffset = midway;
            if (y1 == y2)
            {

                int y3 = y1 + (midway * 2);
                g.drawLine(x1, y1, x1 - n, y1);
                g.drawLine(x1 - n, y1, x1 - n, y3);
                g.drawLine(x1 - n, y3, x2 - n, y3);
                g.drawLine(x2 - n, y3, x2 - n, y2);
                g.drawLine(x2 - n, y2, x2, y2);

            } else
            {
                if (x1 < x2)
                {
                    g.drawLine(x1, y1, x1 - n, y1);
                    g.drawLine(x1 - n, y1, x1 - n, y2);
                    g.drawLine(x1 - n, y2, x2, y2);
                } else
                {
                    g.drawLine(x1, y1, x2 - n, y1);
                    g.drawLine(x2 - n, y1, x2 - n, y2);
                    g.drawLine(x2 - n, y2, x2, y2);
                }
            }

            //g.drawLine(x1, y1, x2, y2);
        }

    }

    private void paintComplexEntityMode(Graphics g)
    {
        try
        {
            if (complexEntityAnnotations.size() > 1)
            {
                paintComplexEntity(g, new ComplexEntity(
                        complexEntityRule.getComplexEntityType(),
                        complexEntityAnnotations));

            }
            int x1, y1, x2, y2;
            Entity a = complexEntityAnnotations.get(complexEntityAnnotations.
                    size() - 1);
            int mid = (a.getStart() + a.getEnd()) / 2;
            Rectangle r = this.getUI().modelToView(this, mid);
            int midway = this.getFont().getSize() / 2;
            x1 = r.x;
            y1 = r.y + midway;

            Point p = this.getMousePosition();
            x2 = p.x;
            y2 = p.y;

            g.drawLine(x1, y1, x2, y2);

            this.repaint();
        } catch (Exception e)
        {
            System.err.println("oops");
        }
    }

    @Override
    public void paint(Graphics g)
    {
        if (!viewModel.isModelSet())
        {
            return;
        }
        // Check if document and version still exist
        if (!viewModel.containsDocumentVersion(docName, verName))
        {
            emptyScreen(); // if not empty screen
            return;
        }
        super.paint(g);
        if (!docLoaded)
        {
            return;
        }
        if (complexEntityMode)
        {
            paintComplexEntityMode(g);
        }
        paintComplexEntities(g);

    }

    /**
     * Paint a specific portion of the text window a specified color.
     * <p/>
     * @param start
     * @param end
     * @param color
     */
    private void paintHighlight(int start, int end, Color color)
    {
        if (start >= end)
        {
            System.out.println("Bad start end.");
            return;
        }

        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setBackground(sas, color);
        doc.setCharacterAttributes(start, end - start, sas, true);
    }

    @Override
    public void repaintView()
    {
        // Check if workspace set
        if (!viewModel.isModelSet())
        {
            return;
        }

        // If document not loaded don't do anything
        if (!docLoaded)
        {
            return;
        }

        // Check if document and version still exist
        if (!viewModel.containsDocumentVersion(docName, verName))
        {
            emptyScreen(); // if not empty screen
            return;
        }

        // If all checks out paint everything!
        paintEntities();

    }

    public void setAutoAddAll(boolean b)
    {
        autoAddAll = b;
    }

    public void setAutoHighlight(boolean b)
    {
        autoHighlight = b;
    }

    @Override
    public void updateDocuentRenamed(String oldDocName, String newDocName)
    {
        if (isDisplaying(oldDocName))
        {
            docName = newDocName;
        }
    }

    @Override
    public void updateDocumentVersionRenamed(String docName, String oldVerName,
                                             String newVerName)
    {
        if (isDisplaying(docName, oldVerName))
        {
            verName = newVerName;
        }
    }

    @Override
    public void updateModelSet()
    {
        emptyScreen();
    }

    private class TextScreenMouseListener implements MouseListener
    {

        @Override
        public void mouseClicked(MouseEvent e)
        {
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            mouseButtonReleased(e);
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            mouseCursorExited(e);
        }
    }
}
