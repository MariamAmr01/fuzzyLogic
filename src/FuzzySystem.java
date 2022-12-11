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

            variables.get(i).setMean();


        }
    }


}
