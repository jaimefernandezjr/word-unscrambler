package WordUnscramblerApp;

/**
* WordUnscramblerApp/WordUnscramblerHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from WordUnscrambler.idl
* Monday, May 24, 2021 10:20:58 PM SGT
*/

public final class WordUnscramblerHolder implements org.omg.CORBA.portable.Streamable
{
  public WordUnscramblerApp.WordUnscrambler value = null;

  public WordUnscramblerHolder ()
  {
  }

  public WordUnscramblerHolder (WordUnscramblerApp.WordUnscrambler initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = WordUnscramblerApp.WordUnscramblerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    WordUnscramblerApp.WordUnscramblerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return WordUnscramblerApp.WordUnscramblerHelper.type ();
  }

}
