import java.util.HashMap;

public class SymbolTable {
    private String name;
    private int scopeNumber;
    private final HashMap<String, String> table = new HashMap<>();
    private int maxKeyLen = 0;
    private int maxValueLen = 0;

    public void insert(String idefNames, String values){
        table.put(idefNames, values);
        maxKeyLen = Math.max(maxKeyLen, idefNames.length()+1);
        maxValueLen = Math.max(maxValueLen, values.length()+1);
    }

    public String lookup(String idefName){
        return table.get(idefName);
    }

    private String printItems(){
        String tableBorder = '+' + "-".repeat(maxKeyLen) + '+' + "-".repeat(maxValueLen) + '+' + '\n';
        StringBuilder tableString = new StringBuilder(tableBorder);
        tableString.append("|" + String.format("%-" + maxKeyLen + "s", "KEY") + "|" + String.format("%-" + maxValueLen + "s", "VALUE") + "|").append('\n');
        tableString.append(tableBorder);
        for(var entry: table.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            key = String.format("%-" + maxKeyLen + "s", key);
            value = String.format("%-" + maxValueLen + "s", value);
            tableString.append("|" + key + "|" + value + "|").append('\n');
        }
        tableString.append(tableBorder);

        return tableString.toString();
    }

    @Override
    public String toString() {
        return "=".repeat(maxKeyLen+maxValueLen+3) +
                "\nscope name: " + name + "\nscope number: " + scopeNumber + '\n' +
                printItems()
                ;
    }
}
