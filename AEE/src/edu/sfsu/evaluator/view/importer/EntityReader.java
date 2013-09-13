/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.importer;

import edu.sfsu.evaluator.model.Entity;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Read entities from IBM internal format. Speak to Su Yan for details.
 * @author eric
 */
public class EntityReader
{

    public static ArrayList<Entity> readEntities(String filePath,
                                                    String docText, String label)
            throws FileNotFoundException
    {
        String text = edu.sfsu.util.FileUtilities.getTextFromFile(filePath);
        ArrayList<Entity> annotations = new ArrayList();

        String annotationRegex = "\\{\"locations\"\\:\\[((?:\\[\\d+,\\d+\\],?)+)\\],\"match\"\\:\"([\\w ]+)\"";
        String indiciesRegex = "\\[(\\d+),(\\d+)\\]";

        Pattern annotationPattern = Pattern.compile(annotationRegex);
        Pattern indiciesPattern = Pattern.compile(indiciesRegex);

        Matcher annotationMatcher = annotationPattern.matcher(text);
        while (annotationMatcher.find())
        {
            String annotationString = annotationMatcher.group();
            String annoText = annotationMatcher.group(2);
            String indicies = annotationMatcher.group(1);

            Matcher indiciesMatcher = indiciesPattern.matcher(indicies);
            while (indiciesMatcher.find())
            {
                int start;
                int end;
                try
                {
                    start = Integer.parseInt(indiciesMatcher.group(1));
                    end = Integer.parseInt(indiciesMatcher.group(2));
                    String annotationText = docText.substring(start, end);
                    if (annoText.compareTo(annotationText) != 0)
                    {
                        System.err.println("Mismatched text");
                        continue;
                    }
                    Entity a = new Entity(annotationText, label, start, end);
                    annotations.add(a);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return annotations;
    }
}
