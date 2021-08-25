import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class DataManager {

    public ArrayList<String> LoadNicknames() {
        ArrayList<String> result = new ArrayList<>();

        String dataFileName = "./data/nicknames.csv";
        File file = new File(dataFileName);
        FileReader fr = null;   //reads the file
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            String line = "";
            String partialLine;
            Boolean done = false;
            int iLine = 0;

            int countTokens = 0;
            br.readLine();

            while ((partialLine = br.readLine()) != null && !done) {
                String[] tokens = line.split("\\,", -1);

                for (int iToken = 0; iToken < tokens.length; iToken++){
                    result.add(tokens[iToken].toLowerCase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<ArrayList<String>> LoadEPAData()
    {
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        String dataFileName = "./data/epa2.csv";
        File file = new File(dataFileName);
        FileReader fr = null;   //reads the file
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            String line = "";
            String partialLine;
            Boolean done = false;
            int iLine = 0;

            int countTokens = 0;
            br.readLine();

            while ((partialLine = br.readLine()) != null && !done) {
                line += partialLine;
                //System.out.println(partialLine);
                String[] tokens = line.split("\\|", -1);

                countTokens = tokens.length;

                if (countTokens == 11)
                {
                    countTokens = 0;
                    line = "";
                    for (int iToken = 0; iToken < tokens.length; iToken++){
                        tokens[iToken] = tokens[iToken].replace("\"","");
                        tokens[iToken] = tokens[iToken].replace("\\n","");
                        //System.out.println(iLine + ", " + iToken + ": " + tokens[iToken]);
                    }
                    String date = tokens[0].toLowerCase();
                    String resident_name = tokens[1].toLowerCase();
                    String EPAID = tokens[2].toLowerCase();
                    String observer_name = tokens[3].toLowerCase();
                    String rating = tokens[4].toLowerCase();
                    String type = tokens[5].toLowerCase();
                    String context = tokens[6].toLowerCase();
                    String feedback = tokens[7].toLowerCase();
                    String professionalism_safety = tokens[8].toLowerCase();

                    ArrayList<String> sample = new ArrayList<>();
                    sample.add(resident_name);
                    sample.add(observer_name);
                    sample.add(feedback);
                    sample.add(professionalism_safety);
                    result.add(sample);

                    //System.out.println(date + "," + resident_name + "," + EPAID + "," + observer_name + "," + rating + "," + type + "," + context + "," + feedback + "," + professionalism_safety);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public DataManager(){

    }

}
