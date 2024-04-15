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
            String file = "src/Project2Dataset/F"+i+".txt";
            //System.out.println(file);
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            String str = new String(bytes);
            for (int j = 0; j<100; j++){
                int start = j*40+33;
                //System.out.println(str.substring(start, start+4));
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

                arr[ranNum-1] = val;
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
            System.out.println("Program is ready and waiting for user command");
            String input = scan.nextLine();
            if (input.equals("CREATE INDEX ON Project2Dataset (RandomV)")){
                ind = new Index();
                indexExists = true;
                System.out.println("The hash-based and array-based indexes are built successfully");
            } else if (input.contains("=") && !input.contains("!=")) {
                // Equality case
                String[] parts = input.split("=");
                int v = Integer.parseInt(parts[1].trim());
                Instant start = Instant.now();
                int accessed=0;
                if (indexExists) {
                    HashSet<Integer> hs = new HashSet<>();
                    System.out.println("Hash-Based Index scan: ");
                    String recs = ind.map.get(v);
                    getVals(hs, recs);
                    accessed=hs.size();
                } else {
                    System.out.println("Table scan: ");
                    for (int i = 1; i<100; i++){
                        String file = "src/Project2Dataset/F"+i+".txt";
                        byte[] bytes = Files.readAllBytes(Paths.get(file));
                        String str = new String(bytes);
                        for (int j = 0; j<100; j++){
                            int cur = j*40;
                            int ranNum = Integer.parseInt(str.substring(cur+33, cur+37));
                            if (ranNum==v){
                                System.out.println(str.substring(cur, cur+40));
                            }

                        }
                    }
                    accessed=99;
                }
                Duration tot = Duration.between(start, Instant.now());
                System.out.println("Time for retrieval: "+tot.getNano()/1000000+" ms");
                System.out.println("Number of files accessed: "+accessed);


            } else if (input.contains(">") && input.contains("<")) {
                // Range case
                // Extract lower bound
                int v1 = Integer.parseInt(input.split(">")[1].split("AND")[0].trim());

                // Extract upper bound
                int v2 = Integer.parseInt(input.split("<")[1].trim());
                Instant start = Instant.now();
                int accessed=0;

                if (indexExists) {
                    System.out.println("Array-Based Index scan: ");
                    HashSet<Integer> hs = new HashSet<>();
                    for (int j = v1; j<v2-1; j++){
                        //array indexes are -1 their actual value, so start at lower bound
                        String recs = ind.arr[j];
                        getVals(hs, recs);
                    }

                    accessed=hs.size();
                } else {
                    System.out.println("Table scan: ");
                    for (int i = 1; i<100; i++){
                        String file = "src/Project2Dataset/F"+i+".txt";
                        byte[] bytes = Files.readAllBytes(Paths.get(file));
                        String str = new String(bytes);
                        for (int j = 0; j<100; j++){
                            int cur = j*40;
                            int ranNum = Integer.parseInt(str.substring(cur+33, cur+37));
                            if (ranNum>v1 && ranNum<v2){
                                System.out.println(str.substring(cur, cur+40));
                            }

                        }
                    }
                    accessed=99;
                }
                Duration tot = Duration.between(start, Instant.now());
                System.out.println("Time for retrieval: "+tot.getNano()/1000000+" ms");
                System.out.println("Number of files accessed: "+accessed);

            } else if (input.contains("!=")) {
                // Inequality case
                int v = Integer.parseInt(input.split("!=")[1].trim());
                System.out.println("Table scan: ");
                Instant start = Instant.now();
                for (int i = 1; i<100; i++){
                    String file = "src/Project2Dataset/F"+i+".txt";
                    byte[] bytes = Files.readAllBytes(Paths.get(file));
                    String str = new String(bytes);
                    for (int j = 0; j<100; j++){
                        int cur = j*40;
                        int ranNum = Integer.parseInt(str.substring(cur+33, cur+37));
                        if (ranNum!= v){
                            System.out.println(str.substring(cur, cur+40));
                        }
                    }
                }

                Duration tot = Duration.between(start, Instant.now());
                System.out.println("Time for retrieval: "+tot.getNano()/1000000+" ms");
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
            String file = "src/Project2Dataset/F"+f+".txt";

            int r = Integer.parseInt(recs.substring(i+2, i+4));

            byte[] bytes = Files.readAllBytes(Paths.get(file));
            String str = new String(bytes);

            System.out.println(str.substring(r*40,(r+1)*40));
        }
    }
}




