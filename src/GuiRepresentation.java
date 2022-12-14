import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class GuiRepresentation extends JFrame implements ActionListener
{

    FuzzySystem fuzzySystem;

    JButton browseVariables, browseSets, browseRules, saveOutputFile, addCrispValues;
    JLabel browseLabel, saveLabel, noteLabel;
    JFrame run;
    Vector<String> paths= new Vector<>();
    Vector<JTextField> textFields;
    Vector<Variable> vars;
    String out = "", path;
    boolean browse, save, variable, set, rule;

    public GuiRepresentation(FuzzySystem fuzzySystem)  {
        browse = false;
        save = false;
        variable = false;
        set = false;
        rule = false;
        initComponents();
        paths.add("");
        paths.add("");
        paths.add("");

        this.setSize(500, 400);
        this.setTitle("Fuzzy System");
        this.fuzzySystem = fuzzySystem;

    }

    private void initComponents() {
        this.setLayout(null);

        browseVariables = new JButton("Select variable file");
        browseSets = new JButton("Select sets file");
        browseRules = new JButton("Select rules file");

        saveOutputFile = new JButton("Save output file and RUN");

        browseVariables.setBounds(150, 50, 200, 40);
        browseSets.setBounds(150, 100, 200, 40);
        browseRules.setBounds(150, 150, 200, 40);

        saveOutputFile.setBounds(150, 200, 200, 40);

        browseLabel = new JLabel();
        saveLabel = new JLabel();
        noteLabel = new JLabel("Select ALL input files first to RUN");

        browseLabel.setBounds(150, 250, 1000, 40);
        saveLabel.setBounds(150, 300, 1000, 40);
        noteLabel.setBounds(150, 10, 1000, 40);

        browseVariables.addActionListener(this);
        browseSets.addActionListener(this);
        browseRules.addActionListener(this);

        saveOutputFile.addActionListener(this);
        saveOutputFile.setEnabled(false);

        this.add(browseVariables);
        this.add(browseSets);
        this.add(browseRules);
        this.add(noteLabel);

        this.add(saveOutputFile);

        this.add(browseLabel);
        this.add(saveLabel);

        browseLabel.setVisible(false);
        saveLabel.setVisible(false);

        this.pack();
        this.setVisible(true);
    }

    public void performAction(String fileName)
    {
        JFileChooser fileChooser = new JFileChooser();
        // Select text files only
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fileChooser.setFileFilter(filter);

        // Set Default directory to the project directory
        fileChooser.setCurrentDirectory(new File("."));
        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {
            browse = true;

            switch (fileName)
            {
                case "browseVariables":
                    variable = true;
                    paths.set(0,fileChooser.getSelectedFile().getAbsolutePath());
                    break;
                case "browseSets":
                    set = true;
                    paths.set(1,fileChooser.getSelectedFile().getAbsolutePath());
                    break;
                case "browseRules":
                    rule = true;
                    paths.set(2,fileChooser.getSelectedFile().getAbsolutePath());
                    break;
                default:
                    break;
            }
            browseLabel.setVisible(true);
            saveLabel.setVisible(false);

            if(variable && set && rule)
                saveOutputFile.setEnabled(true);
        }
        else {
            browse = false;
            browseLabel.setText("Please select the file to open");
            browseLabel.setVisible(true);
            saveLabel.setVisible(false);
        }
    }

    public void CreateRunFrame(){

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

    public void addCrispValuesAction(String path) throws IOException
    {
        out += '\n';
        for(int i=0; i< textFields.size(); i++){
            out += vars.get(i).name + ": " + textFields.get(i).getText() + "\n";
            vars.get(i).crispVal = Float.parseFloat(textFields.get(i).getText());
        }

        fuzzySystem.run();
        BufferedWriter fileWriter=  new BufferedWriter(new FileWriter(path, true));
        fileWriter.write(out);
        fileWriter.flush();
        fileWriter.close();
        out += fuzzySystem.printOutput(path);

        JOptionPane.showMessageDialog(null, out,"Output Result", JOptionPane.INFORMATION_MESSAGE);

        run.dispose();
        out= "";
    }
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == addCrispValues) {
            try {
                addCrispValuesAction(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        if (event.getSource() == browseVariables) {
            performAction("browseVariables");
        }
        if (event.getSource() == browseRules) {
            performAction("browseRules");
        }
        if (event.getSource() == browseSets) {
            performAction("browseSets");
        }


        else if (event.getSource() == saveOutputFile) {

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
            fileChooser.setFileFilter(filter);


            fileChooser.setCurrentDirectory(new File("."));
            int response = fileChooser.showSaveDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {
                save = true;
                path = fileChooser.getSelectedFile().getAbsolutePath();

                if (browse && save) {
                    browse = false;
                    save = false;
                    if (variable && rule && set) {
                        if(!Main.readVar(fuzzySystem, paths.get(0)))
                        {
                            JOptionPane.showMessageDialog(null, "Invalid input variable, Please follow the structure \"" +
                                            "variable’s name, type (IN/OUT) and range ([lower, upper])\"\n--> Example: proj_funding IN [0, 100]\n" +
                                            "Please correct the variable input file and re-upload all input files :)",
                                    "Variable Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if(!Main.readSets(fuzzySystem, paths.get(1)))
                        {
                            JOptionPane.showMessageDialog(null, "Invalid input set or wrong variable name, Please follow the structure \n\"" +
                                            "variable’s name\nfuzzy set name, type (TRI/TRAP) and values\nx\"\n--> Example:\nexp_level\nbeginner TRI 0 15 30\n" +
                                            "intermediate TRI 15 30 45\n" +
                                            "expert TRI 30 60 60\n" +
                                            "x\n" +
                                            "Please correct the set input file and re-upload all input files :)" +
                                            "\n\nNote: Don't forget the 'x' after finishing the fuzzy set AND Check the variable name,\n" +
                                            "the variable must be existed in variable input file.\nTRI MUST followed by 3 numbers\nTRAP MUST followed by 4 numbers" ,
                                    "Set Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if(!Main.readRule(fuzzySystem, paths.get(2))){
                            JOptionPane.showMessageDialog(null, "Invalid rule or wrong input/output variable/set name, Please follow the structure \"" +
                                            "IN_variable set operator IN_variable set => OUT_variable set\"\n--> Example:\nproj_funding high or exp_level expert => risk low\n" +
                                            "Please correct the rule input file and re-upload all input files :)" +
                                            "\n\nNote: the variable must be existed in variable input file, AND sets must be existed in set input file.",
                                    "Rule Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        CreateRunFrame();

                        variable = false;
                        rule = false;
                        set = false;
                        saveOutputFile.setEnabled(false);
                    }
                } else {
                    save = false;
                    saveLabel.setText("Please Select the place of the input file");
                    saveLabel.setVisible(true);
                }


            } else {
                save = false;
                saveLabel.setText("Please Select the place of the output file");
                saveLabel.setVisible(true);
            }

        }
    }

}
