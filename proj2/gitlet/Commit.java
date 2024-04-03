package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    private Date timeStamp;
    private String message;
    private String firstParent;
    private String secondParent;
    private Map<String, String> blobs;//fileName -> fileHash
    private String hash;

    /* TODO: fill in the rest of this class. */

    public Commit() {
        timeStamp = new Date(0);
        message = "initial commit";
        firstParent = null;
        secondParent = null;
        blobs = new HashMap<>();
    }

    public Commit(Stage stageArea, String message) {
        Commit dadCommit = Commit.load();

        this.timeStamp = new Date();
        this.message = message;
        this.firstParent = readContentsAsString(Repository.HEAD_FILE);
        this.secondParent = null;
        this.blobs = new HashMap<>(dadCommit.getTracks());

        this.blobs.putAll(stageArea.getAddition());
        for (String name : stageArea.getRemoval()) {
            this.blobs.remove(name);
        }
    }

    public static Commit load() {
        return Utils.readObject(join(Repository.COMMITS_DIR, readContentsAsString(Repository.HEAD_FILE)), Commit.class);
    }

    public static Commit load(String fileHash) {
        return Utils.readObject(join(Repository.COMMITS_DIR, fileHash), Commit.class);
    }

    public void save(File file) {
        Utils.writeObject(file, this);
    }

    public boolean existSameFile(String fileName, String fileHash) {
        if (blobs.containsKey(fileName)) {
            return blobs.get(fileName).equals(fileHash);
        } else {
            return false;
        }
    }

    public boolean trackTheFile(String fileName) {
        return blobs.containsKey(fileName);
    }

    public Map<String, String> getTracks() {
        return blobs;
    }

    public String calHash() {
        String answer = sha1(getByteArray(timeStamp), message, firstParent, secondParent, getByteArray(blobs));
        this.hash = answer;
        return answer;
    }

    public String getDad() {
        return firstParent;
    }

    @Override
    public String toString() {//还没实现merge相关的
        String startString = "==\n";

        String commitString = String.format("commit %s\n", hash);

        SimpleDateFormat sdf = new SimpleDateFormat("E MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getDefault());
        String date = sdf.format(timeStamp);
        String dateString = String.format("Date: %s\n", date);

        String messageString = String.format("%s\n", message);

        return startString + commitString + dateString + messageString;
    }
}
