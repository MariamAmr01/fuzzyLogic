import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.*;
import java.util.Vector;
import java.util.regex.Pattern;

public class GuiMenu extends JFrame implements ActionListener {

    FuzzySystem fuzzySystem;
    String allVariables= "", allRules= "", allsets= "", out= "";
    boolean varNameAdded = false, variablesAdded= false, rulesAdded = false, setsAdded = false;
    JButton variables, sets, rules, addVariableButton, addRuleButton, submit, addSetButton, submitSets, runSimulation, addCrispValues;
    JFrame addVariable, addRule, addSet, run, output;
    JTextField variablesText, rulesText, setsText, variableName;
    Vector<JTextField> textFields;
    Vector<Variable> vars;

    public GuiMenu(FuzzySystem fuzzySystem) throws IOException {
        initComponents();

        this.setSize(500, 400);
        this.setTitle("Fuzzy System");
        this.fuzzySystem = fuzzySystem;

        new FileWriter("out.txt", false).close();
        new FileWriter("variables.txt", false).close();
        new FileWriter("sets.txt", false).close();
        new FileWriter("rules.txt", false).close();

    }

    private void initComponents() {
        this.setLayout(null);

        variables = new JButton("Add variables");
        sets = new JButton("Add fuzzy sets");
        rules = new JButton("Add rules");
        runSimulation = new JButton("Run the simulation");

        variables.setBounds(150, 50, 200, 40);
        sets.setBounds(150, 100, 200, 40);
        rules.setBounds(150, 150, 200, 40);
        runSimulation.setBounds(150, 200, 200, 40);

        variables.addActionListener(this);
        sets.addActionListener(this);
        rules.addActionListener(this);
        runSimulation.addActionListener(this);

        runSimulation.setEnabled(false);

        this.add(variables);
        this.add(sets);
        this.add(rules);
        this.add(runSimulation);

        this.pack();
        this.setVisible(true);
    }

    public void addVariableAction()
    {
        addVariable = new JFrame();
        addVariable.setTitle("Add variable");
        addVariable.setSize(600, 300);

        JLabel label1 = new JLabel("Enter the variable’s name, type (IN/OUT) and range [lower, upper]");
        JLabel label2 = new JLabel("Don't forget to press add after you finish");

        label1.setBounds(50, 20, 500, 30);
        label2.setBounds(50, 35, 500, 30);

        variablesText = new JTextField();
        variablesText.setBounds(50, 100, 350, 40);

        addVariable.add(label1);
        addVariable.add(label2);
        addVariable.add(variablesText);
        addVariable.setLayout(null);
        addVariable.setVisible(true);

        addVariableButton = new JButton("Add");

        addVariableButton.setBounds(50, 150, 100, 30);
        addVariable.add(addVariableButton);
        addVariableButton.addActionListener(this);
    }

    public void addSetAction()
    {
        addSet = new JFrame();
        addSet.setTitle("Add fuzzy sets to an existing variable");
        addSet.setSize(600, 300);

        JLabel label1 = new JLabel("Enter the variable’s name:");
        variableName = new JTextField();
        JLabel label2 = new JLabel("Enter the fuzzy set name, type (TRI/TRAP) and values:");

        label1.setBounds(50, 10, 500, 30);
        variableName.setBounds(50, 40, 350, 40);
        label2.setBounds(50, 75, 500, 30);

        setsText = new JTextField();
        setsText.setBounds(50, 100, 350, 40);

        addSet.add(label1);
        addSet.add(variableName);
        addSet.add(label2);
        addSet.add(setsText);
        addSet.setLayout(null);
        addSet.setVisible(true);

        addSetButton = new JButton("Add");
        submitSets = new JButton("Finish");

        addSetButton.setBounds(50, 150, 100, 30);
        submitSets.setBounds(50, 190, 100, 30);
        addSet.add(addSetButton);
        addSet.add(submitSets);
        addSetButton.addActionListener(this);
        submitSets.addActionListener(this);
        setsText.setEditable(false);
    }

    public void addRuleAction()
    {
        addRule = new JFrame();
        addRule.setTitle("Add rules");
        addRule.setSize(600, 300);

        JLabel label2 = new JLabel("Enter the rules in this format:");
        JLabel label3 = new JLabel("IN_variable set operator IN_variable set => OUT_variable set");
        JLabel label1 = new JLabel("Please enter one rule per time and PRESS finish to add all rules");

        label2.setBounds(50, 20, 500, 30);
        label3.setBounds(50, 35, 500, 30);
        label1.setBounds(50, 50, 500, 30);

        rulesText = new JTextField();
        rulesText.setBounds(50, 100, 350, 40);

        addRule.add(label2);
        addRule.add(label3);
        addRule.add(label1);
        addRule.add(rulesText);
        addRule.setLayout(null);
        addRule.setVisible(true);

        addRuleButton = new JButton("Add");
        submit = new JButton("Finish");

        addRuleButton.setBounds(50, 150, 100, 30);
        submit.setBounds(50, 190, 100, 30);
        addRule.add(addRuleButton);
        addRule.add(submit);
        addRuleButton.addActionListener(this);
        submit.addActionListener(this);
    }

    public void addVariableInputAction() throws IOException
    {
        String text = variablesText.getText();
        String newText = "";
        int index = text.indexOf(']');

        if(index == text.length()-1 || text.charAt(index+1)!=' '){
            newText = variablesText.getText().replace("]", "]\n");
        }else{
            newText = variablesText.getText().replace("] ", "]\n");
        }

        String[] vars = newText.split("\n");
        for (int i = 0; i < vars.length; i++) {

            if(!Main.checkVriableInputPattern(vars[i])){
                JOptionPane.showMessageDialog(null, "Invalid input variables, Please follow the structure for all variables\n\"" +
                                "variable’s name, type (IN/OUT) and range ([lower, upper])\"\n--> Example:\nproj_funding IN [0, 100] exp_level IN [0, 60] risk OUT [0, 100]\n" +
                                "Please correct the variable input and click Add again :)" +
                                "\n\nNote: spaces are important for variable info,\nand for all variables you can either separate variables with a single space or do not add space between them",
                        "Variables Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        allVariables += newText;

        BufferedWriter writer1 = new BufferedWriter(new FileWriter("variables.txt", true));
        writer1.write(allVariables);
        writer1.flush();
        writer1.close();

        allVariables="";
        addVariable.dispose();

        variablesAdded = true;
        checkRunSimulation();
    }

    public void addSetVariable()
    {
        String text = "";
        if(!varNameAdded){

            text = variableName.getText() + "\n";
            allsets+=text;
            variableName.setEditable(false);
            varNameAdded = true;
            setsText.setEditable(true);

        }else{

            if(Main.checkSetInputPattern(setsText.getText()))
            {
                text = setsText.getText() + "\n";
                allsets+=text;
                setsText.setText("");
            }
            else
            {

                JOptionPane.showMessageDialog(null, "Invalid input set, Please follow the structure \n\"" +
                                "fuzzy set name, type (TRI/TRAP) and values\nx\"\n--> Example:\nbeginner TRI 0 15 30\n" +
                                "Please correct the set input and click Add :)" +
                                "\n\nNote: " +
                                "TRI MUST followed by 3 numbers\nTRAP MUST followed by 4 numbers" ,
                        "Set Input Error", JOptionPane.ERROR_MESSAGE);
            }

        }

    }

    public void submitSetsAction() throws IOException
    {

        allsets += "x\n";
        BufferedWriter writer1 = new BufferedWriter(new FileWriter("sets.txt", true));
        writer1.write(allsets);
        writer1.flush();
        writer1.close();

        addSet.dispose();
        allsets= "";
        varNameAdded = false;
        setsAdded = true;
        checkRunSimulation();
    }

    public void addRuleInputAction()
    {
        if(!Main.checkRuleInputPattern(rulesText.getText())){
            JOptionPane.showMessageDialog(null, "Invalid rule or wrong input/output variable/set name, Please follow the structure \"" +
                            "IN_variable set operator IN_variable set => OUT_variable set\"\n--> Example:\nproj_funding high or exp_level expert => risk low\n" +
                            "Please correct the rule input and click Add :)" +
                            "\n\nNote: Don't forget the operator, the variable must be existed in variable input file, AND sets must be existed in set input file.",
                    "Rule Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String text = rulesText.getText() + "\n";
        allRules+=text;
        rulesText.setText("");
    }

    public void submitAction() throws IOException
    {
        BufferedWriter writer1 = new BufferedWriter(new FileWriter("rules.txt", true));
        writer1.write(allRules);
        writer1.flush();
        writer1.close();

        addRule.dispose();
        allRules= "";
        rulesAdded = true;
        checkRunSimulation();
    }

    public void checkRunSimulation(){
        if(variablesAdded && setsAdded && rulesAdded){
            runSimulation.setEnabled(true);
        }
    }

    public void run(){
        String name = fuzzySystem.name;
        String descrip = fuzzySystem.description;

        fuzzySystem = new FuzzySystem();

        fuzzySystem.name = name;
        fuzzySystem.description = descrip;

        Main.readVar(fuzzySystem, "variables.txt");
        // Invalid Variable name
        if(!Main.readSets(fuzzySystem, "sets.txt")){
            JOptionPane.showMessageDialog(null, "Wrong variable name\nPlease enter the correct variable name and click Add again :)\n\"" +
                        "\n\nNote:" +
                        "the variable must be existed (Entered at Add variable first)" ,
                "Wrong Variable Name Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Main.readRule(fuzzySystem, "rules.txt");

        run = new JFrame();
        textFields = new Vector<>();
        run.setTitle("Run the simulation on crisp values");
        run.setSize(600, 300);

        int labelBoundaries = 10;
        int textFieldBoundaries = 40;

        vars = fuzzySystem.getVariables();
        for(int i=0; i< vars.size(); i++){
            JLabel label1 = new JLabel("Enter the Crisp value of "+ vars.get(i).name);
            label1.setBounds(50, labelBoundaries, 500, 30);
            labelBoundaries+= 65;
            JTextField txt = new JTextField();
            txt.setBounds(50, textFieldBoundaries, 350, 40);
            textFieldBoundaries+=60;
            textFields.add(txt);
            run.add(label1);
            run.add(txt);

        }

        run.setLayout(null);
        run.setVisible(true);

        addCrispValues = new JButton("Add");
        addCrispValues.setBounds(50, textFieldBoundaries-10, 100, 30);
        run.add(addCrispValues);
        addCrispValues.addActionListener(this);

    }

    public void addCrispValuesAction() throws IOException
    {
        out += '\n';
        for(int i=0; i< textFields.size(); i++){
            out += vars.get(i).name + ": " + textFields.get(i).getText() + "\n";
            vars.get(i).crispVal = Float.parseFloat(textFields.get(i).getText());
        }

        fuzzySystem.run();
        BufferedWriter fileWriter=  new BufferedWriter(new FileWriter("out.txt", true));
        fileWriter.write(out);
        fileWriter.flush();
        fileWriter.close();

        out +=  fuzzySystem.printOutput("out.txt");

        JOptionPane.showMessageDialog(null, out,"Output Result", JOptionPane.INFORMATION_MESSAGE);
        run.dispose();
        out= "";
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == variables) {
            addVariableAction();
        }
        if (event.getSource() == addVariableButton) {
            try {
                addVariableInputAction();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (event.getSource() == rules) {
            addRuleAction();
        }
        if (event.getSource() == addRuleButton) {
            addRuleInputAction();
        }
        if (event.getSource() == submit) {
            try {
                submitAction();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (event.getSource() == sets) {
            addSetAction();
        }
        if (event.getSource() == addSetButton) {
            addSetVariable();
        }
        if (event.getSource() == submitSets) {
            try {
                submitSetsAction();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (event.getSource() == runSimulation) {
            run();
        }
        if (event.getSource() == addCrispValues) {
            try {
                addCrispValuesAction();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
