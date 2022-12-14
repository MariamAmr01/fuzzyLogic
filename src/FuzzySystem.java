import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class FuzzySystem {
    public Vector<Variable> variables;
    public Vector<Rule> rules;
    String name;
    String description;

    int index = -1;

    public FuzzySystem()
    {
        variables = new Vector<>();
        rules = new Vector<>();
    }

    public int finVar(String name)
    {
        for (int i = 0; i < variables.size(); i++) {
            if(variables.get(i).name.equals(name))
                return i;
        }
        return -1;
    }


    public Vector<Variable> getVariables() {
        Vector<Variable> vars = new Vector<>();

        for(int i=0; i<variables.size(); i++){
            if(variables.get(i).input){
                vars.add(variables.get(i));
            }
        }

        return vars;
    }

    public void fuzzification()
    {
        for (int i = 0; i < variables.size(); i++) {
            if(variables.get(i).input)
                variables.get(i).setMean();
        }
    }

    public void inference()
    {
        Vector<Rule> temp = new Vector<>();

        for (int i = 0; i < rules.size(); i++) {

            for (int k = 0; k < rules.size(); k++) {
                temp.add(new Rule(rules.get(k)));
            }

            int indx = temp.get(i).operators.indexOf("not");
            if(indx != -1)
            {
                temp.get(i).variableSet.get(indx).mean=1-temp.get(i).variableSet.get(indx).mean;
                temp.get(i).operators.remove(indx);
            }
            while(temp.get(i).operators.size()!=0)
            {
                int indxAndNot = temp.get(i).operators.indexOf("and_not");
                if(indxAndNot != -1)
                {
                    temp.get(i).variableSet.get(indxAndNot+1).mean=1-temp.get(i).variableSet.get(indxAndNot+1).mean;
                    temp.get(i).operators.set(indxAndNot,"and");
                    continue;
                }
                int indxOrNot = temp.get(i).operators.indexOf("or_not");
                if(indxOrNot != -1)
                {
                    temp.get(i).variableSet.get(indxOrNot+1).mean=1-temp.get(i).variableSet.get(indxOrNot+1).mean;
                    temp.get(i).operators.set(indxOrNot,"or");
                    continue;
                }

                int indxAnd = temp.get(i).operators.indexOf("and");
                if(indxAnd != -1)
                {
                    Set newSet = new Set(temp.get(i).variableSet.get(indxAnd));
                    newSet.mean=  Math.min(temp.get(i).variableSet.get(indxAnd).mean,temp.get(i).variableSet.get(indxAnd+1).mean);
                    temp.get(i).variableSet.set(indxAnd,newSet);
                    temp.get(i).variableSet.remove(indxAnd+1);
                    temp.get(i).operators.remove(indxAnd);
                    continue;
                }

                int indxOr = temp.get(i).operators.indexOf("or");
                if(indxOr != -1)
                {
                    Set newSet = new Set(temp.get(i).variableSet.get(indxOr));
                    newSet.mean=  Float.max(temp.get(i).variableSet.get(indxOr).mean,temp.get(i).variableSet.get(indxOr+1).mean);
                    temp.get(i).variableSet.set(indxOr,newSet);
                    temp.get(i).variableSet.remove(indxOr+1);
                    temp.get(i).operators.remove(indxOr);
                }

            }

            rules.get(i).outputSet.mean = Float.max(rules.get(i).outputSet.mean, temp.get(i).variableSet.get(0).mean);
        }

    }

    public void defuzzification(){
        float z = 0, m = 0;

        for (int i = 0; i < variables.size(); i++) {
            if(!variables.get(i).input){

                for (int j = 0; j < variables.get(i).sets.size(); j++) {
                    z += variables.get(i).sets.get(j).calculateC() * variables.get(i).sets.get(j).mean;
                    m += variables.get(i).sets.get(j).mean;

                }
                z = z/m;

                variables.get(i).crispVal = z;
                variables.get(i).input = true;
                index = i;
                run();
                break;

            }
        }

    }

    public void run(){
        fuzzification();
        inference();
        defuzzification();

    }

    public String printOutput(String filePath){

        Vector<Integer> indexSets = new Vector<>();
        if(index!= -1) {
            for (int j = 0; j < variables.get(index).sets.size(); j++) {
                if (variables.get(index).sets.get(j).setRange.contains(variables.get(index).crispVal)) {
                    indexSets.add(j);
                }
            }
        }
        else
        {
            return "Files are empty or no output variables";
        }

        int indexMin = 0;
        String str = "";
        float minDis = Float.MAX_VALUE;
        for (int i = 0; i < indexSets.size(); i++)
        {
            float dis = Math.abs(variables.get(index).sets.get(indexSets.get(i)).centroid - variables.get(index).crispVal);

            if(dis < minDis){
                indexMin = indexSets.get(i);
                minDis = dis;
            }
        }
        try {
            BufferedWriter fileWriter=  new BufferedWriter(new FileWriter(filePath, true));
            str = "The predicted " + variables.get(index).sets.get(indexMin).varName + " is " +
                    variables.get(index).sets.get(indexMin).setName + " (" + variables.get(index).crispVal + ")\n";
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
            return str;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return"";
    }
}
