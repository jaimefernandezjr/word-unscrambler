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
        serverFrame.setSize(400, 200);
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

        JLabel objectReferenceLabel = new JLabel("Object Reference Name: ");
        objectReferenceLabel.setBounds(10, 70, 150, 25);
        objectReferenceLabel.setForeground(Color.WHITE);
        mainPanel.add(objectReferenceLabel);

        JTextField objectReferenceField = new JTextField();
        objectReferenceField.setBounds(200, 70, 150, 25);
        mainPanel.add(objectReferenceField);

        JButton runBtn = new JButton("RUN");
        runBtn.setBounds(225, 115, 100, 25);
        mainPanel.add(runBtn);

        runBtn.addActionListener(e -> {
            String objectReference = objectReferenceField.getText().trim();

            if (objectReference.equals("")) {
                JOptionPane.showMessageDialog(serverFrame, "The field can't be empty.");
            } else {
                try {
                    ORB orb = ORB.init(args, null);
                    POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
                    rootpoa.the_POAManager().activate();
                    WordUnscramblerImpl helloImpl = new WordUnscramblerImpl();
                    org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
                    WordUnscrambler href = WordUnscramblerHelper.narrow(ref);
                    org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
                    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

                    String name = objectReference;
                    NameComponent path[] = ncRef.to_name(name);
                    ncRef.rebind(path, href);
                    JOptionPane.showMessageDialog(serverFrame, "WordUnscrambler server is running...");
                    serverFrame.setVisible(false);
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

    public static void main(String args[]) {
        try {
            WordUnscramblerServer server = new WordUnscramblerServer();
            server.serverNetworkSetup(args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}