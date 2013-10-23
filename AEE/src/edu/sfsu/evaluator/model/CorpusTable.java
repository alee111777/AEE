package edu.sfsu.evaluator.model;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class CorpusTable extends JFrame {
   
        private JScrollPane scrollPane;
        
    public  CorpusTable(Vector<String> data) {

        Vector<String> columnNames = new Vector<String>();
        columnNames.add("Prescion");
        columnNames.add("Recall");
        columnNames.add("F-Measure");
        JTable table = new JTable(data, columnNames);
        table.setDefaultRenderer(Object.class, new TableCellRenderer(){
            private DefaultTableCellRenderer DEFAULT_RENDERER =  new DefaultTableCellRenderer();
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row%2 == 0){
                    c.setBackground(Color.WHITE);
                }
                else {
                    c.setBackground(Color.LIGHT_GRAY);
                }                        
                return c;
            }

        });
        
        
        scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setFillsViewportHeight(true);
        JFrame corpusJFrame = new JFrame();
        corpusJFrame.add(scrollPane);
        corpusJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
     }
    public void showCorpusTable() {
        JFrame corpusJFrame = new JFrame();
        corpusJFrame.add(scrollPane);
        corpusJFrame.setVisible(true);
        
    }
        
           
        }
        
        
        
    

