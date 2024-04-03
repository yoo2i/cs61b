package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private byte[] content;

    public Blob(byte[] content) {
        this.content = content;
    }

    public void save(File file) {
        Utils.writeObject(file, this);
    }

    public static void remove(String fileHash) {
        File target = Utils.join(Repository.STAGE_DIR, fileHash);
        target.delete();
    }
}
