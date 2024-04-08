package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  @author yoo2i
 */
public class Commit implements Serializable {
    /**
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

    public Commit() {
        timeStamp = new Date(0);
        message = "initial commit";
        firstParent = "";
        secondParent = "";
        blobs = new HashMap<>();
    }

    public Commit(Stage stageArea, String message) {
        Commit dadCommit = Commit.load();

        this.timeStamp = new Date();
        this.message = message;
        this.firstParent = readContentsAsString(Repository.HEAD_FILE);
        this.secondParent = "";
        this.blobs = new HashMap<>(dadCommit.getTracks());

        this.blobs.putAll(stageArea.getAddition());
        for (String name : stageArea.getRemoval()) {
            this.blobs.remove(name);
        }
    }

    public Commit(Stage stageArea, String message, String secondParent) {
        Commit dadCommit = Commit.load();

        this.timeStamp = new Date();
        this.message = message;
        this.firstParent = readContentsAsString(Repository.HEAD_FILE);
        this.secondParent = secondParent;
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
    public String getSecondParent() {
        return secondParent;
    }

    public String getMessage() {
        return message;
    }

    public String getHash() {
        return hash;
    }

    public String getFileHash(String fileName) {
        return blobs.get(fileName);
    }

    @Override
    public String toString() {//还没实现merge相关的
        String startString = "===\n";

        String commitString;
        if (secondParent.isEmpty()) {
            commitString = String.format("commit %s\n", hash);
        } else {
            commitString = String.format("commit %s\nMerge: %s %s\n", hash, firstParent.substring(0, 7), secondParent.substring(0,7));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("E MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getDefault());
        String date = sdf.format(timeStamp);
        String dateString = String.format("Date: %s\n", date);

        String messageString = String.format("%s\n", message);

        return startString + commitString + dateString + messageString;
    }
}
