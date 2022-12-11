import java.util.Vector;

public class Rule {

    public Vector<Set> variableSet;
    public Vector<String> operators;
    public Set outputSet;

    public Rule (Vector<Set> variableSet, Vector<String> operators, Set outputSet)
    {
        this.variableSet = variableSet;
        this.operators = operators;
        this.outputSet = outputSet;
    }
    public Rule()
    {

    }
    public Rule(Rule r) {
        this.variableSet = new Vector<>();
        for (int i = 0; i <  r.variableSet.size(); i++) {
            this.variableSet.add(new Set(r.variableSet.get(i)));
        }
        this.operators = r.operators;
        this.outputSet = new Set(r.outputSet);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "variableSet=" + variableSet +
                ", operators=" + operators +
                ", outputSet=" + outputSet +
                '}';
    }
}
