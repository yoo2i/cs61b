package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static gitlet.Utils.join;

public class Blob implements Serializable {
    private byte[] content;
    private String hash;

    public Blob(byte[] content, String hash) {
        this.content = content;
        this.hash = hash;
    }

    public void save(String fileHash) {
        File file = join(Repository.STAGE_DIR, fileHash);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Utils.writeObject(file, this);
    }

    public static Blob load(String fileHash) {
        File source = Utils.join(Repository.BLOBS_DIR, fileHash);
        return Utils.readObject(source, Blob.class);
    }

    public byte[] getContent() {
        return content;
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
