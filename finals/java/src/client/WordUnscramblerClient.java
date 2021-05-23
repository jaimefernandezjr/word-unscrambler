import WordUnscramblerApp.WordUnscrambler;
import WordUnscramblerApp.WordUnscramblerHelper;
import jdk.nashorn.internal.scripts.JO;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;

public class WordUnscramblerClient {

    public static WordUnscrambler wordUnscrambler;

    public static void main(String[] args) {
        WordUnscramblerClient client = new WordUnscramblerClient();
        client.clientNetworkSetup(args);
//        client.loginWindow();
//        client.gameWindow();
//        client.mainMenuWindow();
//        client.gameOverWindow("You win!");
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
        objectReferenceLabel.setBounds(10, 100, 150, 25);
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
                org.omg.CORBA.Object objRef = orb.resolve_initial_references(rootNaming);
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
                String name = objectReference;
                wordUnscrambler = WordUnscramblerHelper.narrow(ncRef.resolve_str(name));

                loginWindow();
                clientFrame.setVisible(false);
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
                playerField.setText("");
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
            String playerName = playerField.getText();

            String errorDiagnosis = checkIfNameIsValid(playerName);

            if (!errorDiagnosis.equals("Okay")){
                JOptionPane.showMessageDialog(loginFrame, errorDiagnosis);
            } else {
                boolean isRegistered = wordUnscrambler.registerPlayer(playerName);
                if(isRegistered){
                    loginFrame.dispose();
                    gameWindow(playerName);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "This name is already taken. Please enter another name");
                }
            }
        });


        loginFrame.setVisible(true);
    }

    public void gameWindow(String name){
        AtomicInteger lives = new AtomicInteger(5);

        JFrame gameFrame = new JFrame("Word Unscrambler");
        gameFrame.setSize(600, 400);
        gameFrame.setResizable(false);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(111, 45, 152));
        gameFrame.add(mainPanel);

        JLabel livesLabel = new JLabel("Lives: " + lives);
        livesLabel.setBounds(5, 5, 500, 30);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        livesLabel.setForeground(Color.BLACK);
        mainPanel.add(livesLabel);

        JLabel scrambleLabel = new JLabel("Word Unscrambling Game!");
        scrambleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scrambleLabel.setBounds(175, 20, 1000, 60);
        scrambleLabel.setForeground(Color.YELLOW);
        mainPanel.add(scrambleLabel);

        String shuffledMysteryWord = wordUnscrambler.getShuffledMysteryWord(name);

        JLabel shuffledWordLabel = new JLabel(shuffledMysteryWord,SwingConstants.CENTER);
        shuffledWordLabel.setBounds(165, 70, 265, 40);
        shuffledWordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        Border wordborder = BorderFactory.createLineBorder(Color.white,1);
        shuffledWordLabel.setBorder(wordborder);
        shuffledWordLabel.setForeground(Color.white);
        mainPanel.add(shuffledWordLabel);

        JLabel answerLabel = new JLabel("What's the word?");
        answerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        answerLabel.setBounds(210, 130, 1000, 60);
        answerLabel.setForeground(Color.YELLOW);
        mainPanel.add(answerLabel);

        JTextField answerField = new JTextField();
        answerField.setHorizontalAlignment(SwingConstants.CENTER);
        answerField.setBounds(160, 180, 265, 30);
        answerField.setForeground(Color.black);
        mainPanel.add(answerField);

        JButton shuffleBtn = new JButton("SHUFFLE");
        shuffleBtn.setFont(new Font("Arial", Font.BOLD, 14));
        shuffleBtn.setBounds(370, 240, 100,30);
        shuffleBtn.setBackground(new Color(83,162,190));
        shuffleBtn.setForeground(Color.WHITE);
        mainPanel.add(shuffleBtn);

        JButton clearBttn = new JButton("CLEAR");
        clearBttn.setFont(new Font("Arial", Font.BOLD, 14));
        clearBttn.setBounds(120, 240, 100,30);
        clearBttn.setBackground(new Color(83,162,190));
        clearBttn.setForeground(Color.WHITE);
        mainPanel.add(clearBttn);

        JButton enterBtn = new JButton("ENTER");
        enterBtn.setFont(new Font("Arial", Font.BOLD, 14));
        enterBtn.setBounds(245, 240, 100,30);
        enterBtn.setBackground(Color.GREEN.darker().darker());
        enterBtn.setForeground(Color.WHITE);
        mainPanel.add(enterBtn);

        JButton mainMenuBttn = new JButton("MAIN MENU");
        mainMenuBttn.setFont(new Font("Arial", Font.BOLD, 14));
        mainMenuBttn.setBounds(230, 300, 130,30);
        mainMenuBttn.setBackground(Color.orange);
        mainMenuBttn.setForeground(Color.black);
        mainPanel.add(mainMenuBttn);


        shuffleBtn.addActionListener(e -> {
            shuffledWordLabel.setText(wordUnscrambler.shuffleLetters(name, shuffledMysteryWord));
            gameFrame.setVisible(false);
            gameFrame.setVisible(true);
        });

        clearBttn.addActionListener(e ->{
            answerField.setText("");
        });

        enterBtn.addActionListener(e -> {
            boolean isCorrectAnswer = wordUnscrambler.checkAnswer(name, answerField.getText().trim());
            if(isCorrectAnswer){
                gameOverWindow("You Win!");
            } else {
                lives.getAndDecrement();
                int chances = Integer.parseInt(String.valueOf(lives));
                if(chances <= 0){
                    gameFrame.dispose();
                    lives.set(5);
                    gameOverWindow("You Lose!");
                }
            }
        });

        mainMenuBttn.addActionListener( e->{
            mainMenuWindow(name);
        });
        gameFrame.setVisible(true);

    }

    public String checkIfNameIsValid(String name){
        try {
            if (name.equals("")) {
                return "The field cannot be empty";
            } else {
                return "Okay";
            }
        } catch (Exception e){
            e.printStackTrace();
            return "There is an error";
        }
    }

    public void mainMenuWindow(String name){
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

        resumeBtn.addActionListener(e -> menuFrame.dispose());

        restartBtn.addActionListener(e -> {
            menuFrame.dispose();
            gameWindow(name);
        });

        quitBtn.addActionListener(e -> {
            //TODO: remove the player from the connected_clients.txt
            boolean isRemovePlayer = wordUnscrambler.removePlayer(name);
            menuFrame.dispose();
            loginWindow();
        });

        menuFrame.setVisible(true);
    }

    public void gameOverWindow(String winLoseMsg){

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