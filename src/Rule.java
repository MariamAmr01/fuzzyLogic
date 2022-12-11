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

    @Override
    public String toString() {
        return "Rule{" +
                "variableSet=" + variableSet +
                ", operators=" + operators +
                ", outputSet=" + outputSet +
                '}';
    }
}
