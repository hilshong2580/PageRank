import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class PageRank {

    //default the Map of Graph, Initial, Result, inLink,
    //default the given variable of tau and lambda
    private Map<String, Set<String>> G = new HashMap<>();
    public static Map<String, Double> I = new HashMap<>();
    public static Map<String, Double> R = new HashMap<>();
    public static Map<String, Integer> inLink = new HashMap<>();
    public static Set<String> Q = new HashSet<>();
    public static double tau = 0.0001;
    public static double lambda = 0.15;

    public static void main(String[] args) throws IOException {
        PageRank pageRank = new PageRank(); //create a PageRank class to call each function
        pageRank.load("links.srt.gz"); //read the compressed file and save the source and target to Graph
        pageRank.setKeyIR();                //put all the source and target to Initial(with value) and Result Map
        pageRank.doWhileLoop();             //do the while loop when the converged is false
        pageRank.sortMostInLink();          // sort the InLink result and collect the top 75
        pageRank.sortTopPageRank();         // sort the R to get the top 75 pageRank result
        pageRank.writePageRank();           // output PageRank to text file with correct name
        pageRank.writeInLink();             // output InLink to text file with correct name
    }

    private void writePageRank(){
        try {
            FileWriter myWriter = new FileWriter("pagerank.txt");
            for (String str: R.keySet()) {
                //output the content align
                String temp = String.format("%-50s %-10s", str, R.get(str))+"\n";
                myWriter.write(temp);
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("error: PageRank");
            e.printStackTrace();
        }
    }

    private void writeInLink(){
        try {
            FileWriter myWriter = new FileWriter("inlinks.txt");
            for (String str: inLink.keySet()) {
                //output the content align
                myWriter.write(String.format("%-50s %-10s", str, inLink.get(str))+"\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("error: inLink");
            e.printStackTrace();
        }
    }

    private void sortTopPageRank(){
        LinkedHashMap<String, Double> reverse = new LinkedHashMap<>();
        R.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(75)
                .forEachOrdered(x -> reverse.put(x.getKey(), x.getValue()));
        R = reverse;
    }

    private void sortMostInLink(){
        LinkedHashMap<String, Integer> reverse = new LinkedHashMap<>();
        inLink.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(75)
                .forEachOrdered(x -> reverse.put(x.getKey(), x.getValue()));
        inLink = reverse;
    }

    private void doWhileLoop() {
        do {
            double toAdd = 0.0;

            //line 10-12
            //put the default pageRank into R for each kwy
            for (String r : R.keySet())
                R.put(r, lambda / G.size());


            //for each p in Graph in line 13 - 25
            for (String p : G.keySet()) {
                Q = G.get(p);

                if (Q.size() > 0)
                    for (String q : Q)
                        R.put(q, R.get(q) + (1 - lambda) * I.get(p) / Q.size());

                if (Q.size() == 0)
                    toAdd += (1 - lambda) * I.get(p) / G.size();
            }

            for (String key : R.keySet())
                R.put(key, R.get(key) + toAdd);

            if (convergedCheck())
                break;

            I.putAll(R);

        } while (true);
    }

    private void setKeyIR() {
        for (String key : G.keySet()) {
            I.put(key, 1.0 / G.size());
            R.put(key, 0.0);
        }
    }

    private boolean convergedCheck() {
        double norm = 0.0;
        double norm2 = 0.0;

        //System.out.println(I.keySet().size());
        for (String key : I.keySet()) {
            norm += Math.abs(I.get(key) - R.get(key));
            norm2 += Math.pow(I.get(key), 2);
        }
        System.out.println(norm);
        norm2 = Math.sqrt(norm);
        return norm < tau;
    }

    private void load(String inFile) {
        HashMap<String, Set<String>> tempG = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new GZIPInputStream(new FileInputStream(inFile)), "UTF-8"));
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
            //G.putAll(tempG);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}