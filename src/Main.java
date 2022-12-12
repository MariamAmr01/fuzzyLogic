import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Main {

    public static void readVar(FuzzySystem fuzzySystem) {
        try {
            FileReader variables = new FileReader("variables.txt");
            BufferedReader reader1 = new BufferedReader(variables);

            String line;
            while (true) {
                line = reader1.readLine();
                if (line == null) break;

                String[] elements = line.split(" ");
                int crispVal = 0;
                boolean input = false;
                if (elements[1].equals("IN")) {
                    input = true;
                    crispVal = Integer.parseInt(elements[4]);
                }

                int start = Integer.parseInt(elements[2].substring(1, elements[2].length() - 1));
                int end = Integer.parseInt(elements[3].substring(0, elements[3].length() - 1));
                Range range = new Range(start, end);
                Variable variable = new Variable(elements[0], range, input, crispVal);
                fuzzySystem.variables.add(variable);
            }
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    public static void readSets(FuzzySystem fuzzySystem) {
        try {
            FileReader sets = new FileReader("sets.txt");
            BufferedReader reader2 = new BufferedReader(sets);

            String line = "";
            while (true) {
                line = reader2.readLine();
                String varName = line;
                int index = fuzzySystem.finVar(varName);
                if (line == null) break;

                while (true) {

                    line = reader2.readLine();

                    if (line.equals("x")) break;

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

    }

    public static void readRule(FuzzySystem fuzzySystem) {

        try {
            FileReader rules = new FileReader("rules.txt");
            BufferedReader reader3 = new BufferedReader(rules);

            String line;
            while (true) {
                line = reader3.readLine();
                if (line == null) break;

                String[] elements = line.split("=>");
                String[] outputVar = elements[1].split(" ");

                int varIndx = fuzzySystem.finVar(outputVar[1]);
                int setIndx = fuzzySystem.variables.get(varIndx).findSet(outputVar[2]);


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
                        break;
                    }
                    setIndx2 = fuzzySystem.variables.get(varIndx2).findSet(variables[i+1]);

                    if(setIndx2==-1)
                    {
                        System.out.println("Invalid input");
                        break;
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

    }

    public static void main(String[] args) {

        FuzzySystem fuzzySystem = new FuzzySystem();
        readVar(fuzzySystem);
        readSets(fuzzySystem);
        readRule(fuzzySystem);

        fuzzySystem.run();
        fuzzySystem.printOutput();

        //System.out.println("---------------------");

//        for (int i = 0; i < fuzzySystem.variables.size(); i++) {
//
//            System.out.println(fuzzySystem.variables.get(i));
//        }
    }

}