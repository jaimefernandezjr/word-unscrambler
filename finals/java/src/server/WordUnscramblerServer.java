import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import javax.swing.*;
import java.awt.*;
import WordUnscramblerApp.*;

public class WordUnscramblerServer {
    JFrame serverFrame;

    public void serverNetworkSetup(String[] args){
        serverFrame = new JFrame("Server Corba Network Setup");
        serverFrame.setSize(400, 300);
        serverFrame.setResizable(false);
        serverFrame.setLocationRelativeTo(null);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.BLACK);
        serverFrame.add(mainPanel);

        JLabel titleLabel = new JLabel("Server Corba Network Setup");
        titleLabel.setBounds(68, 15, 1000, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel);

        JLabel rootpoaLabel = new JLabel("Root POA Reference: ");
        rootpoaLabel.setForeground(Color.WHITE);
        rootpoaLabel.setBounds(10, 70, 150, 25);
        mainPanel.add(rootpoaLabel);

        JTextField rootpoaField = new JTextField(20);
        rootpoaField.setBounds(200, 70, 150, 25);
        mainPanel.add(rootpoaField);

        JLabel rootNamingLabel = new JLabel("Root Naming Context: ");
        rootNamingLabel.setForeground(Color.WHITE);
        rootNamingLabel.setBounds(10, 120, 150, 25);
        mainPanel.add(rootNamingLabel);

        JTextField rootNamingField = new JTextField(20);
        rootNamingField.setBounds(200, 120, 150, 25);
        mainPanel.add(rootNamingField);

        JLabel objectReferenceLabel = new JLabel("Object Reference Name: ");
        objectReferenceLabel.setBounds(10, 170, 150, 25);
        objectReferenceLabel.setForeground(Color.WHITE);
        mainPanel.add(objectReferenceLabel);

        JTextField objectReferenceField = new JTextField();
        objectReferenceField.setBounds(200, 170, 150, 25);
        mainPanel.add(objectReferenceField);

        JButton runBtn = new JButton("RUN");
        runBtn.setBounds(225, 220, 100, 25);
        mainPanel.add(runBtn);

        runBtn.addActionListener(e -> {
            String rootPoa = rootpoaField.getText().trim();
            String rootNaming = rootNamingField.getText().trim();
            String objectReference = objectReferenceField.getText().trim();

            String errorDiagnosis = checkIfInputsAreValid(rootPoa, rootNaming, objectReference);

            if (!errorDiagnosis.equals("Okay")) {
                JOptionPane.showMessageDialog(serverFrame, errorDiagnosis);
            } else {
                try {
                    ORB orb = ORB.init(args, null);
                    POA rootpoa = POAHelper.narrow(orb.resolve_initial_references(rootPoa));
                    rootpoa.the_POAManager().activate();
                    WordUnscramblerImpl helloImpl = new WordUnscramblerImpl();
                    org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
                    WordUnscrambler href = WordUnscramblerHelper.narrow(ref);
                    org.omg.CORBA.Object objRef = orb.resolve_initial_references(rootNaming);
                    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

                    String name = objectReference;
                    NameComponent path[] = ncRef.to_name(name);
                    ncRef.rebind(path, href);
                    JOptionPane.showMessageDialog(serverFrame, "WordUnscrambler Server is running...");
                    serverFrame.setState(JFrame.ICONIFIED);
                    while (true) {
                        orb.run();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        serverFrame.getRootPane().setDefaultButton(runBtn);
        serverFrame.setVisible(true);
    }

    public String checkIfInputsAreValid(String rootpoa, String rootNaming, String objectReference){
        if(rootpoa.equals("") || rootNaming.equals("") || objectReference.equals("")){
            return "The fields cannot be empty";
        }
        return "Okay";
    }

    public static void main(String args[]) {
        try {
            WordUnscramblerServer server = new WordUnscramblerServer();
            server.serverNetworkSetup(args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}   