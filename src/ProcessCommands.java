import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.RandomAccessFile;

public class ProcessCommands
{
    private int              count;
    private ArrayList        offsetValues;
    private int              highest;
    private RandomAccessFile info;
    private FileWriter       results;


    public ProcessCommands(
        ArrayList offsets,
        RandomAccessFile gis,
        FileWriter writeTo)
        throws IOException
    {
        //gets the info needed for this class
        offsetValues = offsets;
        info = gis;
        results = writeTo;
        highest = (int)offsetValues.get(offsetValues.size() - 1);
    }


    public void process(RandomAccessFile file)
    {
        try
        {
            //the count for the command
            count = 1;
            String[] values;
            String line = file.readLine();
            String command;
            while (line != null)
            {
                //ignores comment lines.
                if (line.charAt(0) == ';')
                {
                    line = file.readLine();
                    continue;
                }
                else
                {
                    //skips all the white space
                    values = line.split("\\s+");
                    //passes the relevant value to process based on the command
                    if (values[0].contains("show_name"))
                    {
                        processName(Integer.parseInt(values[1]), line);
                    }
                    else if (values[0].contains("show_latitude"))
                    {
                        processLatitude(Integer.parseInt(values[1]), line);
                    }
                    else if (values[0].contains("show_longitude"))
                    {
                        processLongitude(Integer.parseInt(values[1]), line);
                    }
                    else if (values[0].contains("show_elevation"))
                    {
                        processElevation(Integer.parseInt(values[1]), line);
                    }
                    else if (values[0].contains("quit"))
                    {
                        processQuit(line);
                    }
                    else
                    {
                        System.out.println("COMMANDS SCREWED UP");
                    }
                    count++;
                }
                line = file.readLine();
            }
        }
        catch (IOException e)
        {
            System.out.println("PROCESS FAIL");
            e.printStackTrace();
        }
    }


    public void processName(int value, String line)
    {
        //checks if the value is in the array list with all the offsets
        if (inArrayList(value))
        {
            try
            {
                results.write(count + ": " + line
                    + System.getProperty("line.separator"));
                info.seek(value);
                //when I was testing this got rid of some extra return line character
                //half the time and did pretty much nothing the rest.
                info.readChar();
                String correctLine = info.readLine();
                System.out.println(correctLine);
                String[] correctValues = correctLine.split("\\|");
                results.write(correctValues[1]
                    + System.getProperty("line.separator"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("NOT IN ARRAY NAME");
            try
            {
                results.write(count + ": " + line
                    + System.getProperty("line.separator"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            //handles if the offset isn't in the right place
            handleBadOffset(value);
        }
    }


    public void processLatitude(int value, String line)
    {
        if (inArrayList(value))
        {
            try
            {
                results.write(count + ": " + line
                    + System.getProperty("line.separator"));
                info.seek(value);
                info.readChar();
                String correctLine = info.readLine();
                System.out.println(correctLine);
                String[] correctValues = correctLine.split("\\|");
                fix(correctValues[7]);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("NOT IN ARRAY LATITUDE");
            try
            {
                results.write(count + ": " + line
                    + System.getProperty("line.separator"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            handleBadOffset(value);
        }
    }


    public void processLongitude(int value, String line)
    {
        if (inArrayList(value))
        {
            try
            {
                results.write(count + ": " + line
                    + System.getProperty("line.separator"));
                info.seek(value);
                info.readChar();
                String correctLine = info.readLine();
                System.out.println(correctLine);
                String[] correctValues = correctLine.split("\\|");
                fix(correctValues[8]);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("NOT IN ARRAY LONGITUDE");
            try
            {
                results.write(count + ": " + line
                    + System.getProperty("line.separator"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            handleBadOffset(value);
        }
    }


    public void processElevation(int value, String line)
    {
        if (inArrayList(value))
        {
            try
            {
                results.write(count + ": " + line
                    + System.getProperty("line.separator"));
                info.seek(value);
                info.readChar();
                String correctLine = info.readLine();
                System.out.println(correctLine);
                String[] correctValues = correctLine.split("\\|");
                System.out.println(correctValues.length);
                if (correctValues[16] == "")
                {
                    results.write("Elevation not given"
                        + System.getProperty("Line.separator"));
                }
                else
                {
                    results.write(correctValues[16]
                        + System.getProperty("line.separator"));
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("NOT IN ARRAY ELEVATION");
            try
            {
                results.write(count + ": " + line
                    + System.getProperty("line.separator"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            handleBadOffset(value);
        }
    }


    public void processQuit(String line)
    {
        try
        {
            results.write(count + ": " + line
                + System.getProperty("line.separator"));
            results.write("   Exiting");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public boolean inArrayList(int x)
    {
        for (int i = 0; i < offsetValues.size(); i++)
        {
            if ((int)offsetValues.get(i) == x)
            {
                return true;
            }
        }
        return false;
    }


    public void handleBadOffset(int x)
    {
        if (x < 0)
        {
            try
            {
                results.write("Offset is not positive"
                    + System.getProperty("line.separator"));
            }
            catch (IOException e)
            {
                System.out.println("OFFSET NOT POSITIVE");
                e.printStackTrace();
            }
        }
        else if (x > highest)
        {
            try
            {
                results.write("Offset too large"
                    + System.getProperty("line.separator"));
            }
            catch (IOException e)
            {
                System.out.println("OFFSET TOO LARGE");
                e.printStackTrace();
            }
        }
        else if (!inArrayList(x))
        {
            try
            {
                results.write("Unaligned offset"
                    + System.getProperty("line.separator"));
            }
            catch (IOException e)
            {
                System.out.println("UNALIGNED OFFSET");
                e.printStackTrace();
            }
        }
    }


    public void fix(String value)
    {
        int length = value.length();
        int first;
        int second;
        int third;
        char direction;
        if (length == 8)
        {
            first = Integer.parseInt(value.substring(0, 3));
            second = Integer.parseInt(value.substring(3, 5));
            third = Integer.parseInt(value.substring(5, 7));
            direction = value.charAt(length - 1);
        }
        else
        {
            first = Integer.parseInt(value.substring(0, 2));
            second = Integer.parseInt(value.substring(2, 4));
            third = Integer.parseInt(value.substring(4, 6));
            direction = value.charAt(length - 1);
        }

        try
        {
            results.write(first + "d " + second + "m " + third + "s ");
            if (direction == 'W')
            {
                results.write("West" + System.getProperty("line.separator"));
            }
            else if (direction == 'N')
            {
                results.write("North" + System.getProperty("line.separator"));
            }
            else if (direction == 'S')
            {
                results.write("South" + System.getProperty("line.separator"));
            }
            else if (direction == 'E')
            {
                results.write("East" + System.getProperty("line.separator"));
            }
        }
        catch (IOException e)
        {
            System.out.println("FIX WRONG");
            e.printStackTrace();
        }

    }
}