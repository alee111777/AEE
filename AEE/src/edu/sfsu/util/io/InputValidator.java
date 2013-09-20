/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.util.io;

/**
 * Module for checking user input.
 * @author Eric Chiang
 */
public class InputValidator
{
    /**
     * String of valid characters for input comparison
     */
    private final static String WHITE_LIST =
            "abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789" +
            " ";


    /**
     * Ensures that user input is only alpha numeric, has no leading or trailing
     * space, contains at least one character, and has no sequences of spaces
     * longer than one.
     * @param userInput String
     * @return boolean
     */
    public static boolean isValidUserInput(String userInput)
    {
        boolean lastCharWasSpace = false;
        for (int i = 0; i < userInput.length(); i++)
        {
            String s = userInput.substring(i, i + 1);
            
            //check for spaces, make sure space is valid.
            if (s.compareTo(" ") == 0)
            {
                if (lastCharWasSpace || i == userInput.length() - 1 ||
                        i == 0)
                {
                    return false;
                } else
                {
                    lastCharWasSpace = true;
                }
            } else
            {
                lastCharWasSpace = false;
                if (!WHITE_LIST.contains(s))
                {
                    return false;
                }
            }
        }
  
        //userInput survived the test, return true.
        return true;
    }
}
