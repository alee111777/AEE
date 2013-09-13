/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author eric
 */
public class FileUtilities
{

    public static String getTextFromFile(String path) throws
            FileNotFoundException
    {
        String fileText = null;
        BufferedReader br = new BufferedReader(new java.io.FileReader(path));
        try
        {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null)
            {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            fileText = sb.toString();
        } catch (IOException e)
        {
            System.err.println("[ERROR] " + e.getMessage());
        } finally
        {
            try
            {
                br.close();
            } catch (IOException e)
            {
                System.err.println("[ERROR] " + e.getMessage());
            }
        }
        return fileText;
    }

     /**
     * Merge two paths intelligently.
     * <p/>
     * Examples:
     * <p/>
     * <t/>'usr/bin' + '/java' = '/usr/bin/java'
     * <p/>
     * <t/>'usr/bin/' + 'java' = '/usr/bin/java'
     * <p/>
     * @param path1
     * @param path2
     * @return
     */
    public static String mergePath(String path1, String path2)
    {
        if (path1.length() <= 0)
        {
            return path2;
        }
        String fileSepKey = "file.separator";
        String fileSeparator = System.getProperty(fileSepKey);
        if (fileSeparator == null || fileSeparator.length() == 0)
        {
            System.err.printf("Bad system property: '%s'\n", fileSepKey);
            return null;
        }
        // if the last char is the file separator
        if (path1.lastIndexOf(fileSeparator)
                == path1.length() - fileSeparator.length())
        {
            return path1 + path2;
        } else
        {
            return path1 + fileSeparator + path2;
        }
    }
}
