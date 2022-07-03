import java.util.HashMap;

public class SymbolTable {
    private String name;
    private int scopeNumber;
    private final HashMap<String, String> table = new HashMap<>();

    public void insert(String idefNames, String values){
        table.put(idefNames, values);
    }

    public String lookup(String idefName){
        return table.get(idefName);
    }

    private String printItems(){
        return "";
    }

    @Override
    public String toString() {
        return "------------- " + name + " : " + scopeNumber + " -------------\n" +
                printItems() +
                "-----------------------------------------\n";
    }
}
