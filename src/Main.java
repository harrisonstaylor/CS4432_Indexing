import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

class Index {
    public HashMap<Integer, String> map;
    String[] arr = new String[5000];


    public Index () throws IOException {
        Arrays.fill(arr, "");
        map = new HashMap<>();

        for (int i = 1; i<100; i++){
            String file = "Project2Dataset/F"+i+".txt";
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            String str = new String(bytes);
            for (int j = 0; j<100; j++){
                int start = j*40+34;
                int ranNum = Integer.parseInt(str.substring(start, start+4));
                //Retrieve preexisting string or empty
                String val = map.getOrDefault(ranNum, "");

                // i-1 for 0 index for consistent length
                String f = String.valueOf(i-1);
                if (i-1<10){f = "0"+f;}

                String recStr = String.valueOf(j);
                if (j<10){recStr="0"+recStr;}
                //Add all strings together, format is Map(Random Number, FileNumber + RecordNumber looped
                val = val+f+recStr;

                arr[ranNum] = val;
                map.put(ranNum, val);

            }
        }
    }
}






public class Main {






    public static void main(String[] args) throws IOException {
        boolean indexExists = false;
        Index ind = null;
        Scanner scan = new Scanner(System.in);
        while (true){
            String input = scan.nextLine();
            if (input.equals("CREATE INDEX ON Project2Dataset (RandomV)")){
                ind = new Index();
                indexExists = true;
            } else if (input.contains("=")) {
                // Equality case
                System.out.println("Equality query: " + input);
                String[] parts = input.split("=");
                int v = Integer.parseInt(parts[1].trim());
                Instant start = Instant.now();
                int accessed=0;
                if (indexExists) {
                    HashSet<Integer> hs = new HashSet<>();
                    System.out.println("Index scan");
                    String recs = ind.map.get(v);
                    getVals(hs, recs);
                    accessed=hs.size();
                } else {
                    System.out.println("Table scan");
                    for (int i = 1; i<100; i++){
                        String file = "Project2Dataset/F"+i+".txt";
                        byte[] bytes = Files.readAllBytes(Paths.get(file));
                        String str = new String(bytes);
                        for (int j = 0; j<100; j++){
                            int cur = j*40;
                            int ranNum = Integer.parseInt(str.substring(cur+34, cur+38));
                            if (ranNum==v){
                                System.out.println(str.substring(cur, cur+40));
                            }

                        }
                    }
                    accessed=99;
                }
                Duration tot = Duration.between(start, Instant.now());
                System.out.println("Time for retrieval: "+tot.getSeconds());
                System.out.println("Number of files accessed: "+accessed);


            } else if (input.contains(">") && input.contains("<")) {
                // Range case
                System.out.println("Range query: " + input);
                int v1 = Integer.parseInt(input.split(">")[1].split("<")[0].trim());
                int v2 = Integer.parseInt(input.split("<")[1].trim());
                Instant start = Instant.now();
                int accessed=0;

                if (indexExists) {
                    System.out.println("Index scan");
                    HashSet<Integer> hs = new HashSet<>();
                    for (int j = v1; j<v2; j++){

                        String recs = ind.arr[j];
                        getVals(hs, recs);
                    }

                    accessed=hs.size();
                } else {
                    System.out.println("Table scan");
                    for (int i = 1; i<100; i++){
                        String file = "Project2Dataset/F"+i+".txt";
                        byte[] bytes = Files.readAllBytes(Paths.get(file));
                        String str = new String(bytes);
                        for (int j = 0; j<100; j++){
                            int cur = j*40;
                            int ranNum = Integer.parseInt(str.substring(cur+34, cur+38));
                            if (ranNum>v1 && ranNum<v2){
                                System.out.println(str.substring(cur, cur+40));
                            }

                        }
                    }
                    accessed=99;
                }
                Duration tot = Duration.between(start, Instant.now());
                System.out.println("Time for retrieval: "+tot.getSeconds());
                System.out.println("Number of files accessed: "+accessed);

            } else if (input.contains("!=")) {
                // Inequality case
                System.out.println("Inequality query: " + input);
                int v = Integer.parseInt(input.split("!=")[1].trim());
                System.out.println("Table scan");
                Instant start = Instant.now();
                for (int i = 1; i<100; i++){
                    String file = "Project2Dataset/F"+i+".txt";
                    byte[] bytes = Files.readAllBytes(Paths.get(file));
                    String str = new String(bytes);
                    for (int j = 0; j<100; j++){
                        int cur = j*40;
                        int ranNum = Integer.parseInt(str.substring(cur+34, cur+38));
                        if (ranNum!= v){
                            System.out.println(str.substring(cur, cur+40));
                        }
                    }
                }

                Duration tot = Duration.between(start, Instant.now());
                System.out.println("Time for retrieval: "+tot.getSeconds());
                System.out.println("Number of files accessed: 99");
            } else {
                // Invalid input
                System.out.println("Invalid query format.");
            }
        }

    }

    private static void getVals(HashSet<Integer> hs, String recs) throws IOException {
        for (int i = 0; i<recs.length(); i+=4){
            int f = Integer.parseInt(recs.substring(i, i+2))+1;
            hs.add(f);
            String file = "Project2Dataset/F"+f+".txt";

            int r = Integer.parseInt(recs.substring(i+2, i+4));

            byte[] bytes = Files.readAllBytes(Paths.get(file));
            String str = new String(bytes);

            System.out.println(str.substring(r*40,(r+1)*40));
        }
    }
}




