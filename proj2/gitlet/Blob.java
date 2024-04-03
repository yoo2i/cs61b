package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private byte[] content;
    private String hash;

    public Blob(byte[] content, String hash) {
        this.content = content;
        this.hash = hash;
    }

    public void save(File file) {
        Utils.writeObject(file, this);
    }

    public static void remove(String fileHash) {
        File target = Utils.join(Repository.STAGE_DIR, fileHash);
        target.delete();
    }

    public String calHash() {
        String answer = Utils.sha1(content);
        this.hash = answer;
        return answer;
    }

    /*public void saveHash(String hash) {
        this.hash = hash;
    }*/
}
