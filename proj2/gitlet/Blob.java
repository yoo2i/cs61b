package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private String hash;
    private byte[] content;

    public Blob(String hash, byte[] content) {
        this.hash = hash;
        this.content = content;
    }

    public static void remove(String fileHash) {
        File target = Utils.join(Repository.STAGE_DIR, fileHash);
        target.delete();
    }
}
