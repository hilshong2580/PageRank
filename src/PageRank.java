import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PageRank {
    public static void main(String[] args) throws IOException {

        int count = 0;
        HashMap<String,ArrayList<String>> multiMap = new HashMap<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("links.srt"), "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null)
        {
            ArrayList<String> values = new ArrayList<String>();

            count++;
            if(count==100)
                break;

            String[] getName = line.split("\t");

            if(multiMap.containsKey(getName[0])){
                multiMap.get(getName[0]).add(getName[1]);
            }
            else {
                values.add(getName[1]);

                multiMap.put(getName[0], values);
            }
            //System.out.println(line);
        }
        multiMap.get("!Hero_d070").forEach(System.out::println);

    }
}
