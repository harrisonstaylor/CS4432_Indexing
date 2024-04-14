import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

class index {
    public HashMap<Integer, String> map;

    public index () throws IOException {
        map = new HashMap<>();
        for (int i = 1; i<100; i++){
            String file = "F"+i+".txt";
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            String str = new String(bytes);
            for (int j = 0; j<100; j++){
                int start = j*40+34;
                int ranNum = Integer.parseInt(str.substring(start, start+4));
                //Retrieve preexisting string or empty
                String val = map.getOrDefault(ranNum, "");

                // i-1 for 0 index for consistent length
                String f = String.valueOf(i-1);
                if (i-1<10){ f = "0"+f;}

                String recStr = String.valueOf(j);
                if (j<10){recStr="0"+recStr;}
                //Add all strings together, format is Map(Random Number, FileNumber + RecordNumber looped
                val = val+f+recStr;

                map.put(ranNum, val);

            }
        }
    }
}






public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}




