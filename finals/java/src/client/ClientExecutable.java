package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

public class ClientExecutable extends Application {

    static Hello helloImpl;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/GameScreen.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        try {
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);
            // get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt instead of NamingContext. This is part
            // of the Interoperable naming Service.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            // resolve the Object Reference in Naming
            String name = "CORBAgrp7";
            helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
            System.out.println(helloImpl.sayHello());
            System.out.println("5 is " + (helloImpl.isEven(5) ? "even." :
                    "odd."));
            System.out.println("-10 is " + (helloImpl.isEven(-10) ?
                    "even." : "odd."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
