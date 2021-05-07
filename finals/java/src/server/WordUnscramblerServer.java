import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import WordUnscramblerApp.WordUnscrambler;
import WordUnscramblerApp.WordUnscramblerHelper;

public class WordUnscramblerServer {
    public static void main(String args[]) {
        try {
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);
            // get reference to rootpoa & activate the POAManager
            POA rootpoa =
                    POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            // create servant and register it with the ORB
            WordUnscramblerImpl helloImpl = new WordUnscramblerImpl();
            // get object reference from the servant
            org.omg.CORBA.Object ref =
                    rootpoa.servant_to_reference(helloImpl);
            WordUnscrambler href = WordUnscramblerHelper.narrow(ref);
            // get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            // bind the Object Reference in Naming
            String name = "CORBAgrp7";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);
            System.out.println("Word Unscrambler server ready and waiting ...");
            // wait for invocations from clients
            while(true) {
                orb.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Word Unscrambler server Exiting...");
    }
}
