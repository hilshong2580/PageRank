import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;


public class PageRank {

    //default the Map of Graph, Initial, Result, inLink,
    //default the given variable of tau and lambda
    public static  Map<String, Set<String>> G = new HashMap<>();
    public static Map<String, Double> I = new HashMap<>();
    public static Map<String, Double> R = new HashMap<>();
    public static Map<String, Integer> inLink = new HashMap<>();
    public static Set<String> Q = new HashSet<>();
    public static double tau = 0.0001;
    public static double lambda = 0.15;

    public static void main(String[] args) {
        PageRank pageRank = new PageRank(); //create a PageRank class to call each function
        pageRank.load(); //read the compressed file and save the source and target to Graph
        pageRank.setKeyIR();                //put all the source and target to Initial(with value) and Result Map
        pageRank.doWhileLoop();             //do the while loop when the converged is false
        pageRank.sortMostInLink();          // sort the InLink result and collect the top 75
        pageRank.sortTopPageRank();         // sort the R to get the top 75 pageRank result
        pageRank.writePageRank();           // output PageRank to text file with correct name
        pageRank.writeInLink();             // output InLink to text file with correct name
    }

    //read the file from compressed file
    private void load() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new GZIPInputStream(new FileInputStream("links.srt.gz")), StandardCharsets.UTF_8));
            String str;
            while ((str = br.readLine()) != null) {
                String[] token = str.split("\t");
                if (token.length == 2) {
                    String source = token[0];
                    String target = token[1];
                    if (!G.containsKey(source))
                        G.put(source, new HashSet<String>());
                    G.get(source).add(target);
                    if (!G.containsKey(target)) {
                        G.put(target, new HashSet<String>());
                    }
                    if(!inLink.containsKey(target))
                        inLink.put(target, 0);
                    inLink.put(target, inLink.get(target)+1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //set the I and R key and the value
    private void setKeyIR() {
        for (String key : G.keySet()) {
            I.put(key, 1.0 / G.size());
            R.put(key, 0.0);
        }
    }

    //keep run the while loop if convergedCheck return false, until true
    private void doWhileLoop() {
        do {
            double toAdd = 0.0;


            //line 10-12
            //put the default pageRank into R for each kwy

            R.replaceAll((r, v) -> lambda / G.size());

            //for each p in Graph in line 13 - 25
            for (String p : G.keySet()) {
                Q = G.get(p);

                //update the R key's value if Q's size is not zero
                if (Q.size() > 0)
                    for (String q : Q)
                        R.put(q, R.get(q) + (1 - lambda) * I.get(p) / Q.size());
                else
                    toAdd += (1 - lambda) * I.get(p) / G.size();
            }

            //update the R key's value for Q's size is zero
            for (String key : R.keySet())
                R.put(key, R.get(key) + toAdd);

            if (convergedCheck()) //break the loop if true
                break;

            I.putAll(R); //update the I map
        } while (convergedCheck());
    }

    //check the converged by calculate the norm value from I and R
    private boolean convergedCheck() {
        //create the norm to compare with tau
        //norm2 is not used, It appeared from prof lecture
        //double norm2 = 0.0;

        double norm = 0.0;
        for (String key : I.keySet()) {
            norm += Math.abs(I.get(key) - R.get(key));
            //norm2 += Math.pow(I.get(key), 2);
        }
        //norm2 = Math.sqrt(norm);
        return norm < tau;
    }

    //sort the R Map to get top 75 PageRank
    private void sortTopPageRank(){
        LinkedHashMap<String, Double> reverse = new LinkedHashMap<>();
        R.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(75)
                .forEachOrdered(x -> reverse.put(x.getKey(), x.getValue()));
        R = reverse;
    }

    //sort the inLink Map to get top 75 frequency
    private void sortMostInLink(){
        LinkedHashMap<String, Integer> reverse = new LinkedHashMap<>();
        inLink.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(75)
                .forEachOrdered(x -> reverse.put(x.getKey(), x.getValue()));
        inLink = reverse;
    }

    //output file to pagerank.txt
    private void writePageRank(){
        int index = 1;
        try {
            FileWriter myWriter = new FileWriter("pagerank.txt");
            for (String str: R.keySet()) {
                //output the content align
                String temp = String.format("%-5s %-50s %-10s",index, str, R.get(str))+"\n";
                myWriter.write(temp);
                index++;
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("error: PageRank");
            e.printStackTrace();
        }
    }

    //output file to inlinks.txt
    private void writeInLink(){
        int index = 1;
        try {
            FileWriter myWriter = new FileWriter("inlinks.txt");
            for (String str: inLink.keySet()) {
                //output the content align
                myWriter.write(String.format("%-5s %-50s %-10s",index, str, inLink.get(str))+"\n");
                index++;
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("error: inLink");
            e.printStackTrace();
        }
    }

}