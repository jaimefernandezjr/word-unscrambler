package res;

import java.io.File;

public enum FileRecord {
    WORDS_DATABASE("Server/res/words.txt"),
    CONNECTED_CLIENTS("Server/res/connected_clients");

    private final File filepath;

    FileRecord(String path){
        this.filepath = new File(path);
    }
    public File getFilepath(){
        return filepath;
    }
}
