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

//        double norm = 0.0;
//        double norm2 = 0.0;
//        for (String key: I.keySet()){
//            norm += Math.abs(I.get(key) - R.get(key));
//            norm2 += Math.pow(I.get(key),2);
//            norm2 = Math.sqrt(norm);
//        }
//        if(norm > tau){
//            for (String key : I.keySet()) {
//
//            }
//        }

        //line 10 - 12
        for (String r: R.keySet()) {
            R.put(r, lambda/G.size());
            if(c%1000 == 0)
                System.out.println(c+ " String r: R.keySet() ");
            c++;
        }

        for (String p: G.keySet()){
            Q = G.get(p);
            toAdd=0;
            if(c%1000 == 0)
                System.out.println(c+ " tring p: G.keySet() ");
            c++;

            for(String q: Q){
                R.put(q, R.get(q)+(1-lambda)*I.get(p) / Q.size());

                if(c%1000 == 0)
                    System.out.println(c+ " String q: Q ");
                c++;
            }

            if(Q.size() == 0){
                toAdd += (1-lambda)*(I.get(p)/G.size());
                    R.put(p, toAdd);
                    if(c%1000 == 0)
                        System.out.println(c+ " String key: R.keySet() ");
                    c++;

            }
        }
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
