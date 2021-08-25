import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	// write your code here
        DataManager dm = new DataManager();
        ArrayList<ArrayList<String>> samples = dm.LoadEPAData();
        ArrayList<String> nicknames = dm.LoadNicknames();
        SimpleAnonymizer sa = new SimpleAnonymizer(nicknames);

        System.out.println(samples.size());

        for (int iSample = 0; iSample < samples.size(); iSample++){
            sa.Anonymize(samples.get(iSample));
        }
    }
}
