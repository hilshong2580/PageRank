import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;



public class PageRank {

    public static Map<String,Set<String>> G = new HashMap<>();
    public static Map<String,Double> I = new HashMap<>();
    public static Map<String,Double> R = new HashMap<>();
    public static Set<String> Q = new HashSet<>();
    public static double tau = 0.0001;
    public static double lambda = 0.15;
    public static double toAdd = 0.0;

    public static void main(String[] args) throws IOException {
        PageRank pageRank = new PageRank();
        pageRank.load("links.srt.gz");
        for (String key : G.keySet())
            I.put(key, 1.0/G.size());
        R = I;
        int c = 0;
        int d = 0;

        do{
            //print the number of while loop time
            System.out.println(d);
            d++;

            //reset toAdd value
            toAdd=0;

            //line 10-12
            //put the default pageRank into R for each kwy
            for (String r: R.keySet()) {
                R.put(r, lambda/G.size());
            }

            //for each p in Graph in line 13 - 25
            for (String p: G.keySet()){
                Q = G.get(p);

                for(String q: Q){
                    R.put(q, R.get(q)+(1-lambda)* I.get(p) / Q.size());
                }

                if(Q.size() == 0){
                    toAdd += (1-lambda)*I.get(p)/G.size();
                }
            }

            for(String key: R.keySet()){
                R.put(key, R.get(key)+ toAdd);
            }

            for (String r: R.keySet()) {
                if(c%100 == 0)
                    System.out.println(R.get(r));
                c++;
            }

        }while(!pageRank.convergedCheck());
        System.out.println(R.size());
    }

    private boolean convergedCheck(){
        double norm = 0.0;
        double norm2 = 0.0;
        for (String key: I.keySet()){
            norm += Math.abs(I.get(key) - R.get(key));
            norm2 += Math.pow(I.get(key),2);
        }
        norm2 = Math.sqrt(norm);
        return norm<tau;
    }


    private void load(String inFile){
        HashMap<String,Set<String>> tempG= new HashMap<>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new GZIPInputStream(new FileInputStream(inFile)), "UTF-8"));
            String str;
            while((str = br.readLine()) != null){
                String[] token = str.split("\t");
                if(token.length == 2) {
                    String source = token[0];
                    String target = token[1];
                    if (!G.containsKey(source))
                        G.put(source, new HashSet<String>());
                    G.get(source).add(target);
                    tempG.put(target, new HashSet<String>());
                }
            }
            G.putAll(tempG);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
