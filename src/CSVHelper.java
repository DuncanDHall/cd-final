import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by duncan on 5/13/17.
 */
public class CSVHelper {

    public static void writeCSV(AODVEvent[] events) {
        String csvfile = "out.csv";
        try {
            FileWriter fw = new FileWriter(csvfile);

            for (AODVEvent event : events) {
                fw.write(event.toString() + "\n");
            }

            fw.flush();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
