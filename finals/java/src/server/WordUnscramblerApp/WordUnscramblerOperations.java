package WordUnscramblerApp;


/**
* WordUnscramblerApp/WordUnscramblerOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from WordUnscrambler.idl
* Thursday, May 6, 2021 11:33:10 AM SGT
*/

public interface WordUnscramblerOperations 
{
  void registerPlayer (String player);
  boolean checkIfActive (String player);
  String getRandWord ();
  String shuffleLetters (String mysteryWord);
  boolean checkAnswer (String guessWord);
  String requestShuffle (String w);
} // interface WordUnscramblerOperations
