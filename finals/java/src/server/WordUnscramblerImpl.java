import java.io.*;
import java.util.*;
import WordUnscramblerApp.WordUnscramblerPOA;
import res.FileRecord;

public class WordUnscramblerImpl extends WordUnscramblerPOA {
    BufferedReader clientsReader;
    BufferedReader wordsReader;
    BufferedWriter clientsWriter;

    WordUnscramblerImpl() throws Exception{
        clientsReader = new BufferedReader(new FileReader(FileRecord.CONNECTED_CLIENTS.getFilepath()));
        wordsReader = new BufferedReader(new FileReader(FileRecord.WORDS_DATABASE.getFilepath()));
        clientsWriter = new BufferedWriter(new FileWriter(FileRecord.CONNECTED_CLIENTS.getFilepath(), true));
    }

    public boolean registerPlayer(String player) {
        try {
            //checks if the player is already existing in the database
            String line = clientsReader.readLine();

            while (line != null) {
                if (line.equals(player)) {
                    return false;
                }
                line = clientsReader.readLine();
            }

            //if not existing, registers the client/player to the database
            if (clientsReader.readLine() == null) {
                while (clientsReader.readLine() != null)
                    clientsReader.readLine();
                clientsWriter.write(player + "\n");

                clientsReader.close();
                clientsWriter.close();

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //get a random word from the words.txt
    public String getShuffledMysteryWord(String player) {

        String mysteryWord = generateMysteryWord();

        //put the player name and his mystery word in the database
        String playerXmysteryWord = player + "&&" + mysteryWord;

        try {
            //put all the clients in an ArrayList
            List<String> clientsList = new ArrayList<>();
            String line = clientsReader.readLine();
            while (line != null) {
                clientsList.add(line);

                line = clientsReader.readLine();
            }

            //replaces the player line with playerXmysteryWord
            for (String c : clientsList) {

                if (c.contains("&&")) {
                    String[] details = c.split("&&");
                    if (details[0].equals(player)) {
                        clientsList.remove(c);
                        clientsList.add(playerXmysteryWord);
                        break;
                    }
                } else {
                    if (c.equals(player)) {
                        clientsList.remove(c);
                        clientsList.add(playerXmysteryWord);
                        break;
                    }
                }
            }

            //replace the file in the database with the clientsList array
            //delete the file
            clientsReader.close();
            clientsWriter.close();
            String fileLoc = String.valueOf(FileRecord.CONNECTED_CLIENTS.getFilepath());
            File clientsFile = new File(fileLoc);
            if (clientsFile.delete())
                System.out.println("Deleted the file: " + clientsFile.getName());
            else
                System.out.println("Failed to delete the file.");

            //create the same file that is empty
            File newClientsFile = new File(fileLoc);
            if (newClientsFile.createNewFile())
                System.out.println("Created the file: " + newClientsFile.getName());
            else
                System.out.println("File already exists.");

            clientsReader = new BufferedReader(new FileReader(fileLoc));
            clientsWriter = new BufferedWriter(new FileWriter(fileLoc));
            //put the clientsList's content to the newClientsFile
            for (String c : clientsList) {
                if (clientsReader.readLine() == null) {
                    while (clientsReader.readLine() != null) {
                        clientsReader.readLine();
                    }
                    clientsWriter.write(c);
                    clientsWriter.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //return the shuffled mystery word
        return shuffleLetters(player, mysteryWord);
    }

    public String shuffleLetters(String player, String guessWord) {
        String shuffledWord = "";
        String mysteryWord = "";

        try {
            //obtains the mysteryWord of the player
            String line = clientsReader.readLine();
            while (line != null) {
                String[] details = line.split("&&");
                if (player.equals(details[0])) {
                    mysteryWord = details[1];
                    break;
                }
                line = clientsReader.readLine();
            }

            List<String> mysteryWordArr = new ArrayList<>(Arrays.asList(guessWord.split("")));
            do {
                //shuffles the word's letters
                Collections.shuffle(mysteryWordArr);
                shuffledWord = "";
                for (String letter : mysteryWordArr) {
                    shuffledWord = shuffledWord.concat(letter);
                }
            } while (shuffledWord.equals(guessWord) || shuffledWord.equals(mysteryWord));
            //^ ensure that the shuffled letters to be return is not the actual answer nor the guessWord

        } catch (Exception e) {
            e.printStackTrace();
        }
        return shuffledWord;
    }

    public boolean checkAnswer(String player, String guessWord) {
        //checks the answer if it is correct or not
        String mysteryWord = "";
        try {
            String line = clientsReader.readLine();
            while (line != null) {
                String[] details = line.split("");
                if (player.equals(details[0])) {
                    mysteryWord = details[1];
                }
                line = clientsReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return guessWord.equals(mysteryWord);
    }

    public String generateMysteryWord() {
        List<String> words = new ArrayList<>();
        Random random = new Random();

        try {
            //populate the words list with words from words.txt
            String line = wordsReader.readLine();
            while (line != null) {
                words.add(line.trim());
                line = wordsReader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return words.get(random.nextInt(words.size()));
    }
}
