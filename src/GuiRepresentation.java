import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

public class GuiRepresentation extends JFrame implements ActionListener
{

    FuzzySystem fuzzySystem = new FuzzySystem();

    JButton browseVariables;
    JButton browseSets;
    JButton browseRules;

    JButton saveOutputFile;

    JLabel browseLabel;
    JLabel saveLabel;

    Vector<String> paths= new Vector<>();
    boolean browse, save, variable, set, rule;

    public GuiRepresentation()  {
        browse = false;
        save = false;
        variable = false;
        set = false;
        rule = false;
        initComponents();
        paths.add("");
        paths.add("");
        paths.add("");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 400);
        this.setTitle("Fuzzy System");

    }

    private void initComponents() {
        this.setLayout(null);

        browseVariables = new JButton("Select variable file");
        browseSets = new JButton("Select sets file");
        browseRules = new JButton("Select rules file");

        saveOutputFile = new JButton("Save output file");

        browseVariables.setBounds(150, 50, 200, 40);
        browseSets.setBounds(150, 100, 200, 40);
        browseRules.setBounds(150, 150, 200, 40);

        saveOutputFile.setBounds(150, 200, 200, 40);

        browseLabel = new JLabel();
        saveLabel = new JLabel();

        browseLabel.setBounds(150, 250, 1000, 40);
        saveLabel.setBounds(150, 300, 1000, 40);


        browseVariables.addActionListener(this);
        browseSets.addActionListener(this);
        browseRules.addActionListener(this);

        saveOutputFile.addActionListener(this);

        this.add(browseVariables);
        this.add(browseSets);
        this.add(browseRules);

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
        fileChooser.setMultiSelectionEnabled(true);

        // Set Default dirctory to the project dirctory
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

        }
        else {
            browse = false;
            browseLabel.setText("Please select the file to open");
            browseLabel.setVisible(true);
            saveLabel.setVisible(false);
        }
    }

    public void actionPerformed(ActionEvent event) {


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
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                saveLabel.setText("File Saved Successfully");
                saveLabel.setVisible(true);
                    if (browse && save) {
                        browse = false;
                        save = false;
                        if(variable&&rule&&set)
                        {
                            Main.readVar(fuzzySystem, paths.get(0));
                            Main.readSets(fuzzySystem, paths.get(1));
                            Main.readRule(fuzzySystem, paths.get(2));
                            fuzzySystem.run();
                            fuzzySystem.printOutput(path);
                        }
                    }
                    else{
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
