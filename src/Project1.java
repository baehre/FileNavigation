import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class Project1
{
    public static void main(String args[])
        throws IOException
    {
        try
        {
            //gets the files from the command line arguments
            RandomAccessFile gis = new RandomAccessFile(args[0], "r");
            RandomAccessFile commands = new RandomAccessFile(args[1], "r");
            //makes the file for output
            File file = new File("Results.txt");
            file.createNewFile();
            FileWriter results = new FileWriter(file);
            results.write("GIS data file contains the following records:"
                + System.getProperty("line.separator")
                + System.getProperty("line.separator"));

            FirstLineNums a = new FirstLineNums();

            ArrayList offsetValues = a.writeFileOffsets(gis, results);
            //puts the pointer to 0 for the gis
            gis.seek(0);
            ProcessCommands b = new ProcessCommands(offsetValues, gis, results);
            results.write(System.getProperty("line.separator"));
            b.process(commands);
            //flushes and closes the output file
            results.flush();
            results.close();

        }
        catch (FileNotFoundException e)
        {
            System.out.println("FAIL");
            e.printStackTrace();
        }
    }
}

//On my honor:
//
//- I have not discussed the Java language code in my program with
//anyone other than my instructor or the teaching assistants
//assigned to this course.
//
//- I have not used Java language code obtained from another student,
//or any other unauthorized source, either modified or unmodified.
//
//- If any Java language code or documentation used in my program
//was obtained from another source, such as a text book or course
//notes, that has been clearly noted with a proper citation in
//the comments of my program.
//
//- I have not designed this program in such a way as to defeat or
//interfere with the normal operation of the Curator System.
//
//<Andrew Baehre>
