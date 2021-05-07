package WordUnscramblerApp;


/**
* WordUnscramblerApp/WordUnscramblerPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from WordUnscrambler.idl
* Thursday, May 6, 2021 11:33:10 AM SGT
*/

public abstract class WordUnscramblerPOA extends org.omg.PortableServer.Servant
 implements WordUnscramblerApp.WordUnscramblerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("registerPlayer", new java.lang.Integer (0));
    _methods.put ("checkIfActive", new java.lang.Integer (1));
    _methods.put ("getRandWord", new java.lang.Integer (2));
    _methods.put ("shuffleLetters", new java.lang.Integer (3));
    _methods.put ("checkAnswer", new java.lang.Integer (4));
    _methods.put ("requestShuffle", new java.lang.Integer (5));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // WordUnscramblerApp/WordUnscrambler/registerPlayer
       {
         String player = in.read_string ();
         this.registerPlayer (player);
         out = $rh.createReply();
         break;
       }

       case 1:  // WordUnscramblerApp/WordUnscrambler/checkIfActive
       {
         String player = in.read_string ();
         boolean $result = false;
         $result = this.checkIfActive (player);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 2:  // WordUnscramblerApp/WordUnscrambler/getRandWord
       {
         String $result = null;
         $result = this.getRandWord ();
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // WordUnscramblerApp/WordUnscrambler/shuffleLetters
       {
         String mysteryWord = in.read_string ();
         String $result = null;
         $result = this.shuffleLetters (mysteryWord);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // WordUnscramblerApp/WordUnscrambler/checkAnswer
       {
         String guessWord = in.read_string ();
         boolean $result = false;
         $result = this.checkAnswer (guessWord);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 5:  // WordUnscramblerApp/WordUnscrambler/requestShuffle
       {
         String w = in.read_string ();
         String $result = null;
         $result = this.requestShuffle (w);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:WordUnscramblerApp/WordUnscrambler:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public WordUnscrambler _this() 
  {
    return WordUnscramblerHelper.narrow(
    super._this_object());
  }

  public WordUnscrambler _this(org.omg.CORBA.ORB orb) 
  {
    return WordUnscramblerHelper.narrow(
    super._this_object(orb));
  }


} // class WordUnscramblerPOA