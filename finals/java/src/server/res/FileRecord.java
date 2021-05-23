package res;

import java.io.File;

public enum FileRecord {
    WORDS_DATABASE("res/words.txt"),
    CONNECTED_CLIENTS("res/connected_clients.txt");

    private final File filepath;

    FileRecord(String path){
        this.filepath = new File(path);
    }
    public File getFilepath(){
        return filepath;
    }
}
