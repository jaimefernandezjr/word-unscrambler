import WordUnscramblerApp.WordUnscrambler;
import WordUnscramblerApp.WordUnscramblerHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import javax.swing.*;
import javax.swing.border.Border;
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
        mainPanel.setBackground(new Color(111, 45, 152));
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
        JFrame gameFrame = new JFrame("Word Unscrambler");
        gameFrame.setSize(600, 400);
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(111, 45, 152));
        gameFrame.add(mainPanel);

        JLabel scrambleLabel = new JLabel("Word Unscrambling Game!");
        scrambleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scrambleLabel.setBounds(175, 20, 1000, 60);
        scrambleLabel.setForeground(Color.YELLOW);
        mainPanel.add(scrambleLabel);

        JLabel words = new JLabel("sample word",SwingConstants.CENTER);
        words.setBounds(165, 70, 265, 40);
        words.setFont(new Font("Arial", Font.BOLD, 14));
        Border wordborder = BorderFactory.createLineBorder(Color.white,1);
        words.setBorder(wordborder);
        words.setForeground(Color.white);
        mainPanel.add(words);

        JLabel answerLabel = new JLabel("What's the word?");
        answerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        answerLabel.setBounds(210, 110, 1000, 60);
        answerLabel.setForeground(Color.YELLOW);
        mainPanel.add(answerLabel);

        JTextField answerField = new JTextField();
        answerField.setBounds(130, 155, 230, 30);
        answerField.setForeground(Color.black);
        mainPanel.add(answerField);

        JButton checkBttn = new JButton("CHECK");
        checkBttn.setFont(new Font("Arial", Font.BOLD, 14));
        checkBttn.setBounds(370, 155, 100,30);
        checkBttn.setBackground(Color.GREEN.darker().darker());
        checkBttn.setForeground(Color.WHITE);
        mainPanel.add(checkBttn);

        JLabel correctLabel = new JLabel(" CORRECT ");
        correctLabel.setFont(new Font("Arial", Font.BOLD, 20));
        correctLabel.setBounds(150, 205, 120, 55);
        Border correctBorder = BorderFactory.createLineBorder(Color.green.darker(), 5);
        correctLabel.setBorder(correctBorder);
        correctLabel.setForeground(Color.white);
        mainPanel.add(correctLabel);

        JLabel wrongLabel = new JLabel("   WRONG");
        wrongLabel.setFont(new Font("Arial", Font.BOLD, 20));
        wrongLabel.setBounds(300, 205, 120, 55);
        Border wrongBorder = BorderFactory.createLineBorder(Color.red, 5);
        wrongLabel.setBorder(wrongBorder);
        wrongLabel.setForeground(Color.white);
        mainPanel.add(wrongLabel);

        JButton startBttn = new JButton("START");
        startBttn.setFont(new Font("Arial", Font.BOLD, 14));
        startBttn.setBounds(120, 290, 150,30);
        startBttn.setBackground(new Color(83,162,190));
        startBttn.setForeground(Color.WHITE);
        mainPanel.add(startBttn);

        JButton resetBttn = new JButton("RESET");
        resetBttn.setFont(new Font("Arial", Font.BOLD, 14));
        resetBttn.setBounds(300, 290, 150,30);
        resetBttn.setBackground(new Color(83,162,190));
        resetBttn.setForeground(Color.WHITE);
        mainPanel.add(resetBttn);

        resetBttn.addActionListener( e->{
            words.setText("");
            answerField.setText("");
        });

        gameFrame.setVisible(true);

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

    public void gameOverWindow(String msg){
        //this is the parameter that will be flashed on the screen ("you lose")("you win")
        String winLoseMsg = "You Win!";

        JFrame gameoverFrame = new JFrame("Word Unscrambler");
        gameoverFrame.setSize(300, 200);
        gameoverFrame.setResizable(false);
        gameoverFrame.setLocationRelativeTo(null);
        gameoverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.BLUE.darker().darker());
        gameoverFrame.add(mainPanel);

        JLabel winLoseLabel = new JLabel(winLoseMsg);
        winLoseLabel.setBounds(85, 40, 1000, 30);
        winLoseLabel.setFont(new Font("Consolas", Font.BOLD, 26));
        if(winLoseMsg.equals("You Win!")){
            winLoseLabel.setForeground(Color.GREEN.darker());
        } else {
            winLoseLabel.setForeground(Color.RED.darker());
        }
        mainPanel.add(winLoseLabel);

        JButton playAgainBtn = new JButton("PLAY AGAIN");
        playAgainBtn.setBounds(30, 90, 110, 25);
        playAgainBtn.setBackground(Color.GREEN.darker().darker());
        playAgainBtn.setForeground(Color.WHITE);
        mainPanel.add(playAgainBtn);

        JButton exitBtn = new JButton("EXIT");
        exitBtn.setBackground(Color.RED.darker().darker());
        exitBtn.setForeground(Color.white);
        exitBtn.setBounds(150, 90, 110, 25);
        mainPanel.add(exitBtn);

        playAgainBtn.addActionListener(e -> {
            //TODO: restarts the game
            gameoverFrame.dispose();
        });

        exitBtn.addActionListener(e -> {
            //TODO: go back to login window
            gameoverFrame.dispose();
        });

        gameoverFrame.setVisible(true);
    }
}