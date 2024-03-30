package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    /* TODO: fill in the rest of this class. */

    public Commit() {
        timeStamp = new Date(0);
        message = "initial commit";
        firstParent = null;
        secondParent = null;
        blobs = new HashMap<>();
    }

    public boolean existSameFile(String fileName, String fileHash) {
        if (blobs.containsKey(fileName)) {
            return blobs.get(fileName).equals(fileHash);
        } else {
            return false;
        }
    }

}
