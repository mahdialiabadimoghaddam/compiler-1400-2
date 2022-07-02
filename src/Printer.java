import java.util.HashMap;

public class Printer {
    public static void printHashMap(HashMap<String, String> symbolTable){
        int maxKeyLen = 0;
        int maxValueLen = 0;
        for (var entry : symbolTable.entrySet()) {
            maxKeyLen = Math.max(maxKeyLen, entry.getKey().length()+1);
            maxValueLen = Math.max(maxValueLen, entry.getValue().length()+1);
        }

        String tableBorder = "+" + "-".repeat(maxKeyLen) + "+" + "-".repeat(maxValueLen) + "+";
        System.out.println(tableBorder);
        System.out.println("|" + String.format("%-" + maxKeyLen + "s", "KEY") + "|" + String.format("%-" + maxValueLen + "s", "VALUE") + "|");
        System.out.println(tableBorder);
        for(var entry: symbolTable.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            key = String.format("%-" + maxKeyLen + "s", key);
            value = String.format("%-" + maxValueLen + "s", value);
            System.out.println("|" + key + "|" + value + "|");
        }
        System.out.println(tableBorder);
    }
}
