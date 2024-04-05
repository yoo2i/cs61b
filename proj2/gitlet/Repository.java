package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File BLOBS_DIR = join(OBJECTS_DIR, "blobs");
    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");
    public static final File STAGE_DIR = join(OBJECTS_DIR, "stage");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    public static final File STAGE_FILE = join(GITLET_DIR, "stage");

    /* TODO: fill in the rest of this class. */

    private static void initFilesAndDirs() {
        GITLET_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        STAGE_DIR.mkdir();
        REFS_DIR.mkdir();

        try {
            HEAD_FILE.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            STAGE_FILE.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean hadBeenInit() {
        return GITLET_DIR.exists();
    }

    public static void init() {
        if (hadBeenInit()) {
            Utils.exitWithMessage("A Gitlet version-control system already exists in the current directory.");
        }

        initFilesAndDirs();

        Stage stageArea = new Stage();
        Commit commit = new Commit();

        String hash = commit.calHash();
        File commitFile = join(COMMITS_DIR, hash);
        try {
            commitFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        commit.save(commitFile);

        stageArea.save();

        writeContents(HEAD_FILE, hash);

        Branch.createBranch("master", hash);
    }

    public static void add(String fileName) {
        if (!hadBeenInit()) {
            Utils.exitWithMessage("Not in an initialized Gitlet directory.");
        }

        File addFile = join(CWD, fileName);
        if (!addFile.exists()) {
            Utils.exitWithMessage("File does not exist.");
        }

        Stage stageArea = Stage.load();
        Commit commit = Commit.load();
        String fileHash = Utils.calHash(readContents(addFile));

        if (stageArea.existFileInAddition(fileName)) {
            Blob.remove(fileHash);
            stageArea.removeFileInAddition(fileName);
        }
        if (!commit.existSameFile(fileName, fileHash)) {
            stageArea.addFileInAddition(fileName, fileHash);

            Blob blob = new Blob(readContents(addFile), fileHash);
            File file = join(STAGE_DIR, fileHash);
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            blob.save(file);
        }

        if (stageArea.exitFileInRemoval(fileName)) {
            stageArea.removeFileInRemoval(fileName);
        }
        stageArea.save();
    }

    public static void commit(String message) {
        Stage stageArea = Stage.load();
        if (stageArea.getAddition().isEmpty()) {
            exitWithMessage("No changes added to the commit.");
        }
        if (message.isEmpty()) {
            exitWithMessage("Please enter a commit message.");
        }
        Commit sonCommit = new Commit(stageArea, message);

        for (Map.Entry<String, String> entry : stageArea.getAddition().entrySet()) {
            File oldFile = join(STAGE_DIR, entry.getValue());
            File newFile = join(BLOBS_DIR, entry.getValue());
            oldFile.renameTo(newFile);
        }

        stageArea.clear();
        stageArea.save();

        String hash = sonCommit.calHash();
        writeContents(HEAD_FILE, hash);

        File commitFile = join(COMMITS_DIR, hash);
        try {
            commitFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sonCommit.save(commitFile);
    }

    public static void rm(String fileName) {
        if (!hadBeenInit()) {
            Utils.exitWithMessage("Not in an initialized Gitlet directory.");
        }

        Stage stageArea = Stage.load();
        Commit commit = Commit.load();
        int flag = 0;

        if (stageArea.existFileInAddition(fileName)) {
            flag = 1;
            stageArea.removeFileInAddition(fileName);
        }
        if (commit.trackTheFile(fileName)) {
            flag = 1;
            stageArea.addFileInRemoval(fileName);

            File file = join(CWD, fileName);
            if (file.exists()) {
                file.delete();
            }
        }

        stageArea.save();

        if (flag == 0) {
            Utils.exitWithMessage("No reason to remove the file.");
        }
    }

    public static void log() {
        if (!hadBeenInit()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        String tmp = readContentsAsString(HEAD_FILE);
        while (!tmp.isEmpty()) {
            Commit commit = Commit.load(tmp);

            System.out.println(commit);

            tmp = commit.getDad();
        }
    }

    public static void globalLog() {
        if (!hadBeenInit()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        List<String> commitNameList = plainFilenamesIn(COMMITS_DIR);
        for(String name : commitNameList) {
            Commit commit = Commit.load(name);
            System.out.println(commit);
        }
    }

    public static void find(String message) {
        if (!hadBeenInit()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        List<String> commitNameList = plainFilenamesIn(COMMITS_DIR);
        int flag = 0;

        for(String name : commitNameList) {
            Commit commit = Commit.load(name);
            if (message.equals(commit.getMessage())) {
                flag = 1;
                System.out.println(commit.getHash());
            }
        }

        if (flag == 0) {
            exitWithMessage("Found no commit with that message.");
        }
    }

    public static String getCompleteCommitId(String fileHash) {
        List<String> commitHashList = plainFilenamesIn(COMMITS_DIR);
        for (String hash : commitHashList) {
            if (hash.startsWith(fileHash)) {
                return hash;
            }
        }
        return "";
    }
    public static void checkoutForFile(String fileName) {
        checkoutForFile(readContentsAsString(HEAD_FILE), fileName);
    }
    public static void checkoutForFile(String commitId, String fileName) {
        commitId = getCompleteCommitId(commitId);
        if (commitId.isEmpty()) {
            exitWithMessage("No commit with that id exists.");
        }
        Commit commit = Commit.load(commitId);

        if (commit.trackTheFile(fileName)) {
            String fileHash = commit.getFileHash(fileName);
            byte[] content = Blob.load(fileHash).getContent();

            File target = join(CWD, fileName);
            if (!target.exists()) {
                try {
                    target.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            writeContents(target, content);
        } else {
            exitWithMessage("File does not exist in that commit.");
        }
    }
    public static void checkoutForBranch(){

    }
}
