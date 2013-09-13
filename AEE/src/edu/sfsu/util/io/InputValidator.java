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
    private final static String WHITE_LIST =
            "abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789" +
            " ";


    /**
     * Ensures that user input is only alpha numeric, has no leading or trailing
     * space, contains at least one character, and only has no sequences of
     * spaces longer than one.
     * @param userInput
     * @return
     */
    public static boolean isValidUserInput(String userInput)
    {
        boolean lastCharWasSpace = true;
        for (int i = 0; i < userInput.length(); i++)
        {
            String s = userInput.substring(i, i + 1);
            if (s.compareTo(" ") == 0)
            {
                if (lastCharWasSpace)
                {
                    return false;
                } else
                {
                    lastCharWasSpace = true;
                }
            } else
            {
                if (!WHITE_LIST.contains(s))
                {
                    return false;
                }
            }
        }
        // If the last char was a space the string will be rejected.
        return !lastCharWasSpace;
    }
}
