/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author eric
 */

/**
 * Evaluator initializes the EvaluatorController
 * 
 * -working on "set UI look and feel
 * @author anthony
 */
public class Evaluator
{

    public static void main(String[] args)
    {
        Runnable runner = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    /**
                     * Set UI look and feel.
                     */
                    // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e)
                {
                    System.err.println("Error loading look and feel");
                }
                // Create controller
                EvaluatorController controller = new EvaluatorController();
            }
        };
        SwingUtilities.invokeLater(runner);
    }
}
