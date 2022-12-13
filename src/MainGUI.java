import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class MainGUI  extends JFrame implements ActionListener {

    JButton createFuzzySystem, close, fileButton, menuButton, finishInfo;
    JFrame chooseMenuOrFile, dataFuzzySystem;
    JTextField systemName, systemDescription;

    FuzzySystem fuzzySystem;

    public MainGUI()  {

        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 300);
        this.setTitle("Fuzzy System");

    }
    private void initComponents() {
        this.setLayout(null);

        createFuzzySystem = new JButton("Create Fuzzy System");
        close = new JButton("Close");


        createFuzzySystem.setBounds(150, 50, 200, 40);
        close.setBounds(150, 100, 200, 40);

        createFuzzySystem.addActionListener(this);
        close.addActionListener(this);

        this.add(createFuzzySystem);
        this.add(close);

        this.pack();
        this.setVisible(true);
    }

    public void createFuzzySystem(){
        dataFuzzySystem = new JFrame();
        dataFuzzySystem.setTitle("System info");
        dataFuzzySystem.setSize(600, 300);

        JLabel label1 = new JLabel("Enter the system’s name:");
        systemName = new JTextField();
        JLabel label2 = new JLabel("Enter the fuzzy system description");

        label1.setBounds(50, 10, 500, 30);
        systemName.setBounds(50, 40, 350, 40);
        label2.setBounds(50, 75, 500, 30);

        systemDescription = new JTextField();
        systemDescription.setBounds(50, 100, 350, 40);

        dataFuzzySystem.add(label1);
        dataFuzzySystem.add(systemName);
        dataFuzzySystem.add(label2);
        dataFuzzySystem.add(systemDescription);
        dataFuzzySystem.setLayout(null);
        dataFuzzySystem.setVisible(true);

        finishInfo = new JButton("Finish");

        finishInfo.setBounds(50, 150, 100, 30);

        dataFuzzySystem.add(finishInfo);

        finishInfo.addActionListener(this);
    }
    public void setInfo(){
        fuzzySystem = new FuzzySystem();
        fuzzySystem.name = systemName.getText();
        fuzzySystem.description = systemDescription.getText();
    }

    public void choose(){
        chooseMenuOrFile = new JFrame("Choose Upload Files/ Menu");
        chooseMenuOrFile.setSize(500, 300);

        fileButton = new JButton("Upload Files");
        menuButton = new JButton("Go to Menu");


        fileButton.setBounds(150, 50, 200, 40);
        menuButton.setBounds(150, 100, 200, 40);

        fileButton.addActionListener(this);
        menuButton.addActionListener(this);

        chooseMenuOrFile.add(menuButton);
        chooseMenuOrFile.add(fileButton);

        chooseMenuOrFile.setLayout(null);
        chooseMenuOrFile.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == createFuzzySystem) {
            createFuzzySystem();
        }
        if (event.getSource() == close) {
            System.exit(0);
        }
        if(event.getSource() == finishInfo){

            setInfo();
            dataFuzzySystem.dispose();
            choose();
        }
        if(event.getSource() == menuButton){
            chooseMenuOrFile.dispose();
            try {
                new GuiMenu(fuzzySystem);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(event.getSource() == fileButton){
            chooseMenuOrFile.dispose();
            new GuiRepresentation(fuzzySystem);
        }
    }
}
