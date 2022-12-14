import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Pattern;

public class Main {
    public static boolean checkVriableInputPattern(String line){
        // Follow the pattern -> variable’s name, type (IN/OUT) and range ([lower, upper])
        // Regular expression : accept any alphabetic character and '_', space, IN or OUT, space, [, lower, ',' , upper, ]
        String varPattern = "[a-zA-z_]+\s+(IN|OUT)\s+\\[[0-9]+,\s+[0-9]+\\]";
        return Pattern.matches(varPattern, line);
    }

    public static boolean checkSetInputPattern(String line){
        // Follow the pattern -> fuzzy set name, type (TRI/TRAP) and values
        // Regular expression : accept any alphabetic character and '_', space, TRI or TRAP, space, values
        // TRAP --> values must be 4
        // TRI --> values must be 3
        String setPattern = "[a-zA-z_]+\s+((TRI\s+([0-9]+\s+){2}[0-9]+)|(TRAP\s+([0-9]+\s+){3}[0-9]+))";
        return Pattern.matches(setPattern, line);
    }

    public static boolean checkRuleInputPattern(String line){
        // Follow the pattern -> IN_variable set operator IN_variable set => OUT_variable set
        // Regular expression : accept variable name, set, space, operator, variable name, set, space, => , space, out variable name, set
        String rulePattern = "[a-zA-z_]+\s+[a-zA-z_]+\s+((or|ar_not|and|and_not)\s+[a-zA-z_]+\s+[a-zA-z_]+\s+)+=>\s+[a-zA-z_]+\s+[a-zA-z_]+";
        return Pattern.matches(rulePattern, line);
    }
    public static boolean readVar(FuzzySystem fuzzySystem, String path) {
        try {
            FileReader variables = new FileReader(path);
            BufferedReader reader1 = new BufferedReader(variables);

            String line;
            while (true) {
                line = reader1.readLine();
                if (line == null)
                    break;
                // Ignore empty lines
                if(line.isEmpty())
                    continue;
                if(!checkVriableInputPattern(line)){
                    return false;
                }
                String[] elements = line.split(" ");
                boolean input = false;
                if (elements[1].equals("IN")) {
                    input = true;
                }

                int start = Integer.parseInt(elements[2].substring(1, elements[2].length() - 1));
                int end = Integer.parseInt(elements[3].substring(0, elements[3].length() - 1));
                Range range = new Range(start, end);
                Variable variable = new Variable(elements[0], range, input);
                fuzzySystem.variables.add(variable);
            }
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return true;
    }

    public static boolean readSets(FuzzySystem fuzzySystem, String path) {
        try {
            FileReader sets = new FileReader(path);
            BufferedReader reader2 = new BufferedReader(sets);

            String line = "";
            while (true) {
                line = reader2.readLine();

                String varName = line;
                int index = fuzzySystem.finVar(varName);
                if (line == null)
                    break;
                // Ignore empty lines
                if(line.isEmpty())
                    continue;

                if (index == -1){
                    return false;
                }
                while (true) {

                    line = reader2.readLine();

                    if (line.equals("x")) break;

                    if(!checkSetInputPattern(line))
                        return false;

                    String[] elements = line.split(" ");

                    boolean tri = elements[1].equals("TRI");
                    Vector<Integer> values = new Vector<>();

                    for (int i = 2; i < elements.length; i++) {
                        values.add(Integer.parseInt(elements[i]));
                    }
                    Set set = new Set(elements[0], varName, tri, values);
                    if (index != -1) {
                        fuzzySystem.variables.get(index).sets.add(set);
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

        return true;
    }

    public static boolean readRule(FuzzySystem fuzzySystem, String path) {

        try {
            FileReader rules = new FileReader(path);
            BufferedReader reader3 = new BufferedReader(rules);

            String line;
            while (true) {
                line = reader3.readLine();
                if (line == null) break;
                // Ignore empty lines
                if(line.isEmpty())
                    continue;

                if(!checkRuleInputPattern(line)){
                    return false;
                }
                String[] elements = line.split("=>");
                String[] outputVar = elements[1].split(" ");

                int varIndx = fuzzySystem.finVar(outputVar[1]);

                if(varIndx == -1)
                    return false;

                int setIndx = fuzzySystem.variables.get(varIndx).findSet(outputVar[2]);

                if(setIndx == -1)
                    return false;

                Set outputSet = fuzzySystem.variables.get(varIndx).sets.get(setIndx);
                String[] variables = elements[0].split(" ");

                Vector<String> operator = new Vector<>();
                Vector<Set> variableSet = new Vector<>();

                for (int i = 0; i < variables.length; i+=2) {


                    if (variables[i].equals("and")
                            ||variables[i].equals("or")
                            ||variables[i].equals("and_not")
                            ||variables[i].equals("or_not")
                            || variables[i].equals("not")) {
                        operator.add(variables[i]);
                        i--;
                        continue;
                    }


                    int varIndx2 = fuzzySystem.finVar(variables[i]);
                    int setIndx2;
                    if(varIndx2==-1)
                    {
                        System.out.println("Invalid input");
                        return false;
                    }
                    setIndx2 = fuzzySystem.variables.get(varIndx2).findSet(variables[i+1]);

                    if(setIndx2==-1)
                    {
                        System.out.println("Invalid input");
                        return false;
                    }
                    Set varSet1 = fuzzySystem.variables.get(varIndx2).sets.get(setIndx2);
                    variableSet.add(varSet1);
                }
                Rule rule= new Rule(variableSet,operator,outputSet);
                fuzzySystem.rules.add(rule);

            }
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return true;

    }

    public static void main(String[] args) {
        new MainGUI();
    }

}