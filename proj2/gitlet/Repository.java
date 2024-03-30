package gitlet;

import java.io.File;
import java.io.IOException;

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
    public static final File INDEX_FILE = join(GITLET_DIR, "index");

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
            INDEX_FILE.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean hadBeenInit() {
        return GITLET_DIR.exists();
    }

    public static void createBranch(String nameOfBranch, String hash) {
        File newBranch = join(REFS_DIR, nameOfBranch);
        if (!newBranch.exists()) {
            try {
                newBranch.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        writeContents(newBranch, hash);
    }

    public static void init() {
        if (hadBeenInit()) {
            Utils.exitWithMessage("A Gitlet version-control system already exists in the current directory.");
        }

        initFilesAndDirs();

        Index stageArea = new Index();
        Commit commit = new Commit();

        String hash = Utils.calHash(commit);
        File commitFile = join(COMMITS_DIR, hash);
        try {
            commitFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeObject(commitFile, commit);

        writeObject(INDEX_FILE, stageArea);

        writeContents(HEAD_FILE, hash);

        createBranch("master", hash);
    }

    public static void add(String fileName) {//还没有实现和rm相关的内容
        if (!hadBeenInit()) {
            Utils.exitWithMessage("Not in an initialized Gitlet directory.");
        }

        File addFile = join(CWD, fileName);
        if (!addFile.exists()) {
            Utils.exitWithMessage("File does not exist.");
        }

        Index stageArea = readObject(INDEX_FILE, Index.class);
        Commit commit = readObject(join(COMMITS_DIR, readContentsAsString(HEAD_FILE)), Commit.class);
        String fileHash = Utils.calHash(readContents(addFile));

        if (stageArea.existTheFile(fileName)) {
            Blob.remove(fileHash);
            stageArea.removeTheFile(fileName);
        }
        if (!commit.existSameFile(fileName, fileHash)) {
            stageArea.addFile(fileName, fileHash);

            Blob blob = new Blob(fileHash, readContents(addFile));
            File file = join(STAGE_DIR, fileHash);
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeObject(file, blob);
        }
    }
}
