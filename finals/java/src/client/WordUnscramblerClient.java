import WordUnscramblerApp.WordUnscrambler;
import WordUnscramblerApp.WordUnscramblerHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import javax.swing.*;
import java.awt.*;

public class WordUnscramblerClient{

    public static WordUnscrambler wordUnscrambler;

    public void clientNetworkSetup(String[] args){
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
            if(!errorDiagnosis.equals("Okay")){
                JOptionPane.showMessageDialog(clientFrame, errorDiagnosis);
            }
            try {
                ORB orb = ORB.init(args, null);
                org.omg.CORBA.Object objRef =
                        orb.resolve_initial_references("NameService");
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
                String name = "Hello";
                wordUnscrambler = WordUnscramblerHelper.narrow(ncRef.resolve_str(name));
//                System.out.println(wordUnscrambler);
//                System.out.println("5 is " + (helloImpl.isEven(5) ? "even." :
//                        "odd."));
//                System.out.println("-10 is " + (helloImpl.isEven(-10) ?
//                        "even." : "odd."));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        clientFrame.getRootPane().setDefaultButton(runBtn);
        clientFrame.setVisible(true);
    }

    public String checkIfInputsAreValid(String rootNaming, String objectReference){
        if(rootNaming.equals("") || objectReference.equals("")){
            return "The fields cannot be empty";
        }
        return "Okay";
    }

    public static void main(String[] args) {
        WordUnscramblerClient client = new WordUnscramblerClient();
        client.clientNetworkSetup(args);
    }
}