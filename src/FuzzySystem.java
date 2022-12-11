import java.util.Vector;

public class FuzzySystem {
    public Vector<Variable> variables;
    public Vector<Rule> rules;
    String name;
    String description;

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

            Vector<Integer> changedIndex= new Vector<>();
            Vector<Float> changedValue= new Vector<>();

            int indx = temp.get(i).operators.indexOf("not");
            if(indx != -1)
            {
                changedValue.add(temp.get(i).variableSet.get(indx).mean);
                changedIndex.add(indx);

                temp.get(i).variableSet.get(indx).mean=1-temp.get(i).variableSet.get(indx).mean;
                temp.get(i).operators.remove(indx);
            }
            while(temp.get(i).operators.size()!=0)
            {
                int indxAndNot = temp.get(i).operators.indexOf("and_not");
                if(indxAndNot != -1)
                {
                    changedValue.add(temp.get(i).variableSet.get(indxAndNot+1).mean);
                    changedIndex.add(indxAndNot+1);
                    temp.get(i).variableSet.get(indxAndNot+1).mean=1-temp.get(i).variableSet.get(indxAndNot+1).mean;
                    temp.get(i).operators.set(indxAndNot,"and");
                    continue;
                }
                int indxOrNot = temp.get(i).operators.indexOf("or_not");
                if(indxOrNot != -1)
                {
                    changedValue.add(temp.get(i).variableSet.get(indxOrNot+1).mean);
                    changedIndex.add(indxOrNot+1);

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
            rules.get(i).outputSet.mean= temp.get(i).variableSet.get(0).mean;
        }
    }
}
