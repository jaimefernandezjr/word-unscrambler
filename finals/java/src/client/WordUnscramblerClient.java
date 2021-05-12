import WordUnscramblerApp.WordUnscrambler;
import WordUnscramblerApp.WordUnscramblerHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WordUnscramblerClient {
    //TODO: gameWindow
    //TODO: mainMenuWindow

    public static WordUnscrambler wordUnscrambler;

    public static void main(String[] args) {
        WordUnscramblerClient client = new WordUnscramblerClient();
        client.clientNetworkSetup(args);
    }

    public void clientNetworkSetup(String[] args) {
        JFrame clientFrame = new JFrame("Client Corba Network Setup");
        clientFrame.setSize(400, 275);
        clientFrame.setResizable(false);
        clientFrame.setLocationRelativeTo(null);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.BLACK);
        clientFrame.add(mainPanel);

        JLabel rootNamingContextLabel = new JLabel("Root Naming Context: ");
        rootNamingContextLabel.setForeground(Color.WHITE);
        rootNamingContextLabel.setBounds(10, 50, 150, 25);
        mainPanel.add(rootNamingContextLabel);

        JTextField rootNamingContextField = new JTextField(20);
        rootNamingContextField.setBounds(200, 50, 150, 25);
        mainPanel.add(rootNamingContextField);

        JLabel objectReferenceLabel = new JLabel("Object Reference Name: ");
        objectReferenceLabel.setForeground(Color.WHITE);
        objectReferenceLabel.setBounds(10, 100, 50, 25);
        mainPanel.add(objectReferenceLabel);

        JTextField objectReferenceField = new JTextField(20);
        objectReferenceField.setBounds(200, 100, 150, 25);
        mainPanel.add(objectReferenceField);

        JButton runBtn = new JButton("RUN");
        runBtn.setBounds(225, 150, 100, 25);
        mainPanel.add(runBtn);

        runBtn.addActionListener(e -> {
            String rootNaming = rootNamingContextField.getText().trim();
            String objectReference = objectReferenceField.getText().trim();

            String errorDiagnosis = checkIfInputsAreValid(rootNaming, objectReference);
            if (!errorDiagnosis.equals("Okay")) {
                JOptionPane.showMessageDialog(clientFrame, errorDiagnosis);
            }
            try {
                ORB orb = ORB.init(args, null);
                org.omg.CORBA.Object objRef =
                        orb.resolve_initial_references(rootNaming);
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
                String name = objectReference;
                wordUnscrambler = WordUnscramblerHelper.narrow(ncRef.resolve_str(name));

                loginWindow();
                clientFrame.setState(JFrame.ICONIFIED);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        clientFrame.getRootPane().setDefaultButton(runBtn);
        clientFrame.setVisible(true);
    }

    public String checkIfInputsAreValid(String rootNaming, String objectReference) {
        if (rootNaming.equals("") || objectReference.equals("")) {
            return "The fields cannot be empty";
        }
        return "Okay";
    }

    public void loginWindow() {
        JFrame loginFrame = new JFrame("Word Unscrambler");
        loginFrame.setSize(600, 400);
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.BLUE.darker().darker());
        loginFrame.add(mainPanel);

        JLabel wordLabel = new JLabel("Word");
        wordLabel.setFont(new Font("Arial", Font.BOLD, 60));
        wordLabel.setBounds(220, 60, 1000, 60);
        wordLabel.setForeground(Color.YELLOW);
        mainPanel.add(wordLabel);

        JLabel unscramblerLabel = new JLabel("Unscrambler");
        unscramblerLabel.setFont(new Font("Arial", Font.BOLD, 62));
        unscramblerLabel.setBounds(110, 100, 1000, 62);
        unscramblerLabel.setForeground(Color.ORANGE);
        mainPanel.add(unscramblerLabel);

        JTextField playerField = new JTextField();
        String promptMessage = "  Player name";
        playerField.setBounds(165, 200, 265, 25);
        playerField.setText(promptMessage);
        playerField.setForeground(Color.LIGHT_GRAY);
        mainPanel.add(playerField);
        playerField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playerField.setText(" ");
                playerField.setForeground(Color.BLACK);
            }
        });

        JButton playBtn = new JButton("PLAY");
        playBtn.setBounds(245, 230, 100, 25);
        playBtn.setFont(new Font("Arial", Font.BOLD, 14));
        playBtn.setBackground(Color.GREEN.darker().darker());
        playBtn.setForeground(Color.WHITE);
        mainPanel.add(playBtn);

        playBtn.addActionListener(e -> {
            String playerName = playerField.getText().trim();

            String errorDiagnosis = checkIfNameIsValid(playerName);
            if (!errorDiagnosis.equals("Okay")){
                JOptionPane.showMessageDialog(loginFrame, errorDiagnosis);
            } else {
                wordUnscrambler.registerPlayer(playerName);
                gameWindow();
                loginFrame.dispose();
            }
        });


        loginFrame.setVisible(true);
    }

    public void gameWindow(){

    }

    public String checkIfNameIsValid(String name){
        try {
            boolean isRegistered = wordUnscrambler.checkIfActive(name);

            if (name.equals("")) {
                return "The field cannot be empty";
            } else if (isRegistered) {
                return "The name is taken. Please try another name.";
            } else {
                return "Okay";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "Okay";
    }

    public void mainMenuWindow(){
        JFrame menuFrame = new JFrame();
        menuFrame.setSize(600, 400);
        menuFrame.setResizable(false);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.BLUE.darker().darker());
        menuFrame.add(mainPanel);

        JLabel mainMenuLabel = new JLabel("Menu");
        mainMenuLabel.setForeground(Color.ORANGE);
        mainMenuLabel.setFont(new Font("Arial", Font.BOLD, 40));
        mainMenuLabel.setBounds(250, 70, 1000, 50);
        mainPanel.add(mainMenuLabel);

        JButton resumeBtn = new JButton("RESUME");
        resumeBtn.setBounds(250, 140, 100, 25);
        resumeBtn.setForeground(Color.WHITE);
        resumeBtn.setBackground(Color.GREEN.darker().darker());
        mainPanel.add(resumeBtn);

        JButton restartBtn = new JButton("RESTART");
        restartBtn.setBounds(250, 190, 100, 25);
        restartBtn.setForeground(Color.WHITE);
        restartBtn.setBackground(Color.GREEN.darker().darker());
        mainPanel.add(restartBtn);

        JButton quitBtn = new JButton("QUIT GAME");
        quitBtn.setBounds(250, 240, 100, 25);
        quitBtn.setForeground(Color.WHITE);
        quitBtn.setBackground(Color.RED.darker());
        mainPanel.add(quitBtn);

        resumeBtn.addActionListener(e -> {
            menuFrame.dispose();
        });

        restartBtn.addActionListener(e -> {
            //TODO: restore 5 hearts, pick a new word and reshuffle it.
            menuFrame.dispose();
        });

        quitBtn.addActionListener(e -> {
            //TODO: remove the player from the connected_clients.txt
            menuFrame.dispose();
            loginWindow();
        });









        menuFrame.setVisible(true);





    }
}