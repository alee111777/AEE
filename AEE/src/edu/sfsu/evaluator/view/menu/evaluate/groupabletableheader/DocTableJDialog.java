/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate.groupabletableheader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class DocTableJDialog extends JDialog
{

    private Vector<Vector<String>> corpusData;
    private Vector<Vector<String>> mentionData;
    private Vector<String> mentionNameList;
    private JButton mentionDataButton;
    private boolean isMentionDataVisible;
    private JTable corpusTable;
    private JScrollPane scroll;

    public DocTableJDialog(Vector<Vector<String>> newCorpusData,
                              Vector<Vector<String>> newMentionData,
                              Vector<String> newMentionNameList)
    {
        super();
        this.setTitle("Document Evaluation Data");
        this.setLayout(new BorderLayout());
        corpusData = newCorpusData;
        mentionData = newMentionData;
        mentionNameList = newMentionNameList;
        mentionDataButton = new JButton();
        mentionDataButton.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mentionDataButtonPressed(evt);
            }
        });
        
        showCorpusData();
        this.setVisible(true);
        
    }
    
    public void showCorpusData() {
        Vector<String> columnNameList = new Vector<String>();
        columnNameList.add("Version");
        columnNameList.add("Precision");
        columnNameList.add("Recall");
        columnNameList.add("F-Measure");
        
        corpusTable = new JTable(corpusData, columnNameList);
        corpusTable.setDefaultRenderer(Object.class, new TableCellRenderer()
        {
            private DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column)
            {

                Component c = DEFAULT_RENDERER.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (row % 2 == 0)
                {
                    c.setBackground(Color.WHITE);
                } else
                {
                    c.setBackground(Color.LIGHT_GRAY);
                }
                return c;
            }
        });
        
        // Finish off gui
        scroll = new JScrollPane(corpusTable);
        getContentPane().add(scroll, BorderLayout.CENTER);
        setSize(600, 300);
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                dispose();
            }
        });
        
        
        mentionDataButton.setText("Show Mention Data");
        getContentPane().add(mentionDataButton, BorderLayout.SOUTH);
        isMentionDataVisible = false;
        this.setVisible(true);
        corpusTable.revalidate();
        corpusTable.repaint();
    }

    public void showMentionData()
    {
        Vector<String> columnNameList = new Vector<String>();
        columnNameList.add("Version");
        columnNameList.add("Precision");
        columnNameList.add("Recall");
        columnNameList.add("F-Measure");
        for (int i = 0; i < mentionNameList.size(); i++)
        {
            columnNameList.add("P");
            columnNameList.add("R");
            columnNameList.add("F");
        }

        //merge corpus and mention data
        Vector<Vector<String>> data = corpusData;
        for (int rowNum = 0; rowNum < corpusData.size(); ++rowNum)
        {
            for (int col = 0; col < mentionData.get(rowNum).size(); ++col)
            {
                data.get(rowNum).add(mentionData.get(rowNum).get(col));
            }
        }

        DefaultTableModel dm = new DefaultTableModel();
        dm.setDataVector(data, columnNameList);


        // Setup corpusTable
        corpusTable = new JTable( /*dm, new GroupableTableColumnModel()*/);
        corpusTable.setDefaultRenderer(Object.class, new TableCellRenderer()
        {
            private DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column)
            {

                Component c = DEFAULT_RENDERER.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (row % 2 == 0)
                {
                    c.setBackground(Color.WHITE);
                } else
                {
                    c.setBackground(Color.LIGHT_GRAY);
                }
                return c;
            }
        });

        corpusTable.setColumnModel(new GroupableTableColumnModel());
        corpusTable.setTableHeader(new GroupableTableHeader(
                (GroupableTableColumnModel) corpusTable.getColumnModel()));
        corpusTable.setModel(dm);

        //setup column groups
        GroupableTableColumnModel cm = (GroupableTableColumnModel) corpusTable.
                getColumnModel();

        int col = 4; //start nesting header at column 4 (the first "P")
        ColumnGroup g_type = new ColumnGroup("Mention Type");
        for (int mentionIndex = 0; mentionIndex < mentionNameList.size(); ++mentionIndex)
        {
            ColumnGroup g_mention = new ColumnGroup(
                    new GroupableTableCellRenderer(),
                    mentionNameList.get(mentionIndex));

            g_type.add(g_mention);
            g_mention.add(cm.getColumn(col++));
            g_mention.add(cm.getColumn(col++));
            g_mention.add(cm.getColumn(col++));

        }

        GroupableTableHeader header = (GroupableTableHeader) corpusTable.
                getTableHeader();
        cm.addColumnGroup(g_type);

        //fix column min width
        for (int i = 0; i < 4; ++i)
        {
            cm.getColumn(i).setMinWidth(80);
        }

        // Finish off gui
        scroll = new JScrollPane(corpusTable);
        getContentPane().add(scroll, BorderLayout.CENTER);
        setSize(1000, 300);
        //this.repaint();
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                dispose();
            }
        });
        
        mentionDataButton.setText("Show Mention Data");
        getContentPane().add(mentionDataButton, BorderLayout.SOUTH);
        this.setVisible(true);
        corpusTable.revalidate();
        corpusTable.repaint();
    }

    /**
     * use this to test CorpusTableJDialog
     *
     * @param args
     */
    public static void main(String[] args)
    {
        Vector<Vector<String>> corpusData = new Vector<Vector<String>>(),
                mentionData = new Vector<Vector<String>>();
        Vector<String> mentionNameList = new Vector<String>();

        for (int i = 0; i < 10; i++)
        {
            Vector<String> row = new Vector<String>();
            row.add("vers" + String.valueOf(i));
            for (int j = 0; j < 3; j++)
            {
                row.add(String.valueOf(j));
            }

            corpusData.add(row);
        }

        for (int i = 0; i < 10; ++i)
        {
            Vector<String> row = new Vector<String>();
            for (int j = 0; j < 6; ++j)
            {
                row.add(String.valueOf(j));
            }

            mentionData.add(row);
        }

        mentionNameList.add("Protein");
        mentionNameList.add("Gene");

        new DocTableJDialog(corpusData, mentionData, mentionNameList);

    }
    
    public void mentionDataButtonPressed(java.awt.event.ActionEvent evt) {
        remove(scroll);
        remove(mentionDataButton);
        if (isMentionDataVisible)
        {
            showCorpusData();
            isMentionDataVisible = false;
            mentionDataButton.setText("Show Mention Data");

        } else
        {
            showMentionData();
            isMentionDataVisible = true;
            mentionDataButton.setText("Hide Mention Data");

        }
    }
}



    

