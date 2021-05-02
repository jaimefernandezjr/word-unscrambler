package server;

import server.res.FileRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class CorbaImpl {
    private boolean isRequestingShuffle = false;
    private String mysteryWord = "";

    public void registerPlayer(String player){
        try(
                BufferedReader reader = new BufferedReader(new FileReader(FileRecord.CONNECTED_CLIENTS.getFilepath()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(FileRecord.CONNECTED_CLIENTS.getFilepath()))
        ){
            //registers the client/player to the database
            if(reader.readLine() == null){
                while(reader.readLine() != null)
                    reader.readLine();
                writer.write(player);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkIfActive(String player){
        //checks if the client is already connected to the server or not

        try (BufferedReader reader = new BufferedReader(new FileReader(FileRecord.CONNECTED_CLIENTS.getFilepath()))){
            String line = reader.readLine();
            while(line != null){
                if(line.trim().equals(player)){
                    return false;
                }
                line = reader.readLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }


    //get a random word from the words.txt
    public String getRandWord(){
        List<String> words = new ArrayList<>();
        Random random = new Random();

        try (BufferedReader reader = new BufferedReader(new FileReader(FileRecord.WORDS_DATABASE.getFilepath()))){
            //populate the words list with words from words.txt
            String line = reader.readLine();
            while(line != null){
                words.add(line.trim());
                line = reader.readLine();
            }
        } catch (Exception e){
           e.printStackTrace();
        }

        mysteryWord = words.get(random.nextInt(words.size()));
        //send to shuffleLetters() method
        return mysteryWord;
    }

    // will also receive the client requests to reshuffle letters
    //randomize the letters of the word
    //send to sendRandLetters()
    public String shuffleLetters(String mysteryWord){
        String shuffledWord;
        String chosenWord;
        //receives the word from the getWord() method
        if(!isRequestingShuffle) {
            chosenWord = getRandWord();
        } else {
            chosenWord = mysteryWord;
        }
        List<String> chosenWordArr = new ArrayList<>(Arrays.asList(chosenWord.split("")));
        do {
            //shuffles the word's letters
            Collections.shuffle(chosenWordArr);
                shuffledWord = "";
            for (String letter : chosenWordArr) {
                shuffledWord = shuffledWord.concat(letter);
            }
        } while (shuffledWord.equals(chosenWord));
        //^ ensure that the shuffled letters to be return is not the actual answer

        return shuffledWord;
    }
    public boolean checkAnswer(String guessWord){
        // receives the answer of the client
        // checks the answer of the client if it is right or wrong
        return guessWord.equals(mysteryWord);
        // if correct, the server will tell the client that he is correct and ask whether to play again or end the game
        // if not, deducts one chance from the player
        // if chances reaches 0, the game will be over and asks the player if he wants to play the game or end the game
    }

    //gets the request from client to reshuffle letters of the word
    public String requestShuffle(String w){
        isRequestingShuffle = true;
        String shuffledWord = shuffleLetters(w);
        isRequestingShuffle = false;

        return shuffledWord;
    }

    // Good Features to Add
    // GUI
    // Leaderboards
    // Trophy for leaderboards
}
