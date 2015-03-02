import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FirstLineNums
{

    public FirstLineNums()
    {

    }


    public ArrayList writeFileOffsets(RandomAccessFile file, FileWriter results)
    {
        try
        {
            ArrayList offsetValues = new ArrayList();
            String[] values;
            int offset = 0;
            String line = file.readLine();
            String nextLine = file.readLine();
            while (nextLine != null)
            {
                offset = offset + line.length() + 1;
                offsetValues.add(offset);
                results.write(offset + "   ");
                values = nextLine.split("\\|");
                results.write(values[0] + System.getProperty("line.separator"));
                line = nextLine;
                nextLine = file.readLine();
            }
            return offsetValues;
        }
        catch (IOException e)
        {
            System.out.println("WRITEFILEOFFSETS FAIL");
            e.printStackTrace();
        }
        return null;
    }
}