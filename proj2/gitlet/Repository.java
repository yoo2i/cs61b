package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    public static final File CURRENT_BRANCH_FILE = join(GITLET_DIR, "current_branch");

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
        try {
            CURRENT_BRANCH_FILE.createNewFile();
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
        Branch.updateCurrentBranch("master");
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
        String fileHash = Utils.sha1(readContents(addFile));

        if (stageArea.existFileInAddition(fileName)) {
            Blob.remove(fileHash);
            stageArea.removeFileInAddition(fileName);
        }
        if (!commit.existSameFile(fileName, fileHash)) {
            stageArea.addFileInAddition(fileName, fileHash);

            Blob blob = new Blob(readContents(addFile), fileHash);
            blob.save(fileHash);
        }

        if (stageArea.exitFileInRemoval(fileName)) {
            stageArea.removeFileInRemoval(fileName);
        }
        stageArea.save();
    }

    public static void deleteAllInStage(Stage stageArea) {
        stageArea.clear();
        stageArea.save();

        File[] files = STAGE_DIR.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    public static void commit(String message) {
        Stage stageArea = Stage.load();
        if (stageArea.isEmpty()) {
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

        deleteAllInStage(stageArea);

        String hash = sonCommit.calHash();
        writeContents(HEAD_FILE, hash);
        String currentBranchName = Branch.getCurrentBranchName();
        writeContents(join(REFS_DIR, currentBranchName), hash);

        File commitFile = join(COMMITS_DIR, hash);
        try {
            commitFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sonCommit.save(commitFile);
    }
    public static void commit(String message, String secondParent) {
        Stage stageArea = Stage.load();
        if (stageArea.isEmpty()) {
            exitWithMessage("No changes added to the commit.");
        }
        if (message.isEmpty()) {
            exitWithMessage("Please enter a commit message.");
        }
        Commit sonCommit = new Commit(stageArea, message, secondParent);

        for (Map.Entry<String, String> entry : stageArea.getAddition().entrySet()) {
            File oldFile = join(STAGE_DIR, entry.getValue());
            File newFile = join(BLOBS_DIR, entry.getValue());
            oldFile.renameTo(newFile);
        }

        deleteAllInStage(stageArea);

        String hash = sonCommit.calHash();
        writeContents(HEAD_FILE, hash);
        String currentBranchName = Branch.getCurrentBranchName();
        writeContents(join(REFS_DIR, currentBranchName), hash);

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

    public static void status() {
        if (!hadBeenInit()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        StringBuilder branches = new StringBuilder("=== Branches ===\n");
        List<String> branchesNameList = plainFilenamesIn(REFS_DIR);
        Collections.sort(branchesNameList);
        for (String branch : branchesNameList) {
            if (branch.equals(Branch.getCurrentBranchName())) {
                branches.append(String.format("*%s\n", branch));
            } else {
                branches.append(String.format("%s\n", branch));
            }
        }
        System.out.println(branches);

        Stage stageArea = Stage.load();
        StringBuilder stagedFiles = new StringBuilder("=== Staged Files ===\n");
        List<String> stagedFilesNameList = new ArrayList<>(stageArea.getAddition().keySet());
        Collections.sort(stagedFilesNameList);
        for (String fileName : stagedFilesNameList) {
            stagedFiles.append(String.format("%s\n", fileName));
        }
        System.out.println(stagedFiles);

        StringBuilder removedFiles = new StringBuilder("=== Removed Files ===\n");
        List<String> removedFilesNameList = new ArrayList<>(stageArea.getRemoval());
        Collections.sort(removedFilesNameList);
        for (String fileName : removedFilesNameList) {
            removedFiles.append(String.format("%s\n", fileName));
        }
        System.out.println(removedFiles);

        System.out.println("=== Modifications Not Staged For Commit ===\n");
        System.out.println("=== Untracked Files ===\n");
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
    public static void modifyCWDSafely(Commit nowCommit, Commit targetCommit) {
        List<String> allFiles = plainFilenamesIn(CWD);
        for (String fileName : allFiles) {
            if (!nowCommit.trackTheFile(fileName)) {
                if (targetCommit.trackTheFile(fileName)){
                    if (!sha1(readContents(join(CWD, fileName))).equals(targetCommit.getFileHash(fileName))) {
                        exitWithMessage("There is an untracked file in the way; delete it, or add and commit it first.");
                    }
                }
            }
        }

        for (String fileName : allFiles) {
            if (nowCommit.trackTheFile(fileName)) {
                if (!targetCommit.trackTheFile(fileName)) {
                    File file = join(CWD, fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }

        for (Map.Entry<String, String> entry : targetCommit.getTracks().entrySet()) {
            Blob blob = Blob.load(entry.getValue());
            File file = join(CWD, entry.getKey());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            writeContents(file, blob.getContent());
        }
    }
    public static void checkoutForFile(String fileName) {
        checkoutForFile(readContentsAsString(HEAD_FILE), fileName);
    }
    public static void checkoutForFile(String commitId, String fileName) {
        if (!hadBeenInit()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }
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
    public static void checkoutForBranch(String branchName){
        if (!hadBeenInit()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }
        File branchFile = join(REFS_DIR, branchName);
        if (!branchFile.exists()) {
            exitWithMessage("No such branch exists.");
        }

        if (branchName.equals(Branch.getCurrentBranchName())) {
            exitWithMessage("No need to checkout the current branch.");
        }

        Commit nowCommit = Commit.load(readContentsAsString(HEAD_FILE));
        Commit checkCommit = Commit.load(readContentsAsString(branchFile));

        modifyCWDSafely(nowCommit, checkCommit);

        Stage stageArea = Stage.load();
        deleteAllInStage(stageArea);

        writeContents(HEAD_FILE, checkCommit.getHash());
        Branch.updateCurrentBranch(branchName);
    }

    public static void branch(String branchName) {
        if (!hadBeenInit()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        if (Branch.hasBeenCreated(branchName)) {
            exitWithMessage("A branch with that name already exists.");
        }

        Branch.createBranch(branchName, readContentsAsString(HEAD_FILE));
    }

    public static void rmBranch(String branchName) {
        if (!hadBeenInit()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        if (!Branch.hasBeenCreated(branchName)) {
            exitWithMessage("A branch with that name does not exist.");
        }

        if (Branch.getCurrentBranchName().equals(branchName)) {
            exitWithMessage("Cannot remove the current branch.");
        }

        Branch.deleteBranch(branchName);
    }

    public static void reset(String commitId) {
        if (!hadBeenInit()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        commitId = getCompleteCommitId(commitId);
        if (commitId.isEmpty()) {
            exitWithMessage("No commit with that id exists.");
        }
        Commit targetCommit = Commit.load(commitId);
        Commit nowCommit = Commit.load();

        modifyCWDSafely(nowCommit, targetCommit);

        Stage stageArea = Stage.load();
        deleteAllInStage(stageArea);

        Branch.updateCurrentBranchHead(commitId);
        writeContents(HEAD_FILE, commitId);
    }

    public static String getSplitId(Commit nowCommit, Commit givenCommit) {
        Set<String> nowCommitAncestor = new HashSet<>();
        nowCommitAncestor.add(nowCommit.getHash());
        for (Commit tmp = nowCommit; !tmp.getDad().isEmpty(); tmp = Commit.load(tmp.getDad())) {
            nowCommitAncestor.add(tmp.getDad());
        }
        String splitId = givenCommit.getHash();
        Commit tmp = givenCommit;
        while (!tmp.getDad().isEmpty()) {
            if (nowCommitAncestor.contains(splitId)) {
                break;
            }
            splitId = tmp.getDad();
            tmp = Commit.load(tmp.getDad());
        }
        return splitId;
    }
    public static Set<String> getAllFiles(Commit splitCommit, Commit nowCommit, Commit givenCommit) {
        Set<String> answer = new HashSet<>();
        answer.addAll(splitCommit.getTracks().keySet());
        answer.addAll(nowCommit.getTracks().keySet());
        answer.addAll(givenCommit.getTracks().keySet());
        return answer;
    }
    public static boolean modifyFile(Commit commit, Commit splitCommit, String fileName) {
        return (!commit.trackTheFile(fileName)) || (commit.trackTheFile(fileName) && !commit.getFileHash(fileName).equals(splitCommit.getFileHash(fileName)));
    }
    public static boolean modifySame(Commit nowCommit, Commit givenCommit, String fileName) {
        if (nowCommit.trackTheFile(fileName)) {
            if (givenCommit.trackTheFile(fileName)) {
                return nowCommit.getFileHash(fileName).equals(givenCommit.getFileHash(fileName));
            } else {
                return false;
            }
        } else {
            return !givenCommit.trackTheFile(fileName);
        }
    }
    public static void merge(String branchName) {
        if (!hadBeenInit()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }
        Stage stageArea = Stage.load();
        if (!stageArea.isEmpty()) {
            exitWithMessage("You have uncommitted changes.");
        }
        File branch = join(REFS_DIR, branchName);
        if (!branch.exists()) {
            exitWithMessage("A branch with that name does not exist.");
        }
        if (branchName.equals(Branch.getCurrentBranchName())) {
            exitWithMessage("Cannot merge a branch with itself.");
        }
        Commit nowCommit = Commit.load();
        Commit givenCommit = Commit.load(readContentsAsString(join(REFS_DIR, branchName)));
        List<String> allFiles = plainFilenamesIn(CWD);
        for (String fileName : allFiles) {
            if (!nowCommit.trackTheFile(fileName)) {
                if (givenCommit.trackTheFile(fileName)){
                    if (!sha1(readContents(join(CWD, fileName))).equals(givenCommit.getFileHash(fileName))) {
                        exitWithMessage("There is an untracked file in the way; delete it, or add and commit it first.");
                    }
                }
            }
        }
        Boolean conflictFlag = false;

        String splitId = getSplitId(nowCommit, givenCommit);
        if (splitId.equals(givenCommit.getHash())) {
            exitWithMessage("Given branch is an ancestor of the current branch.");
        } else if (splitId.equals(nowCommit.getHash())) {
            checkoutForBranch(branchName);
            exitWithMessage("Current branch fast-forwarded.");
        } else {
            Commit splitCommit = Commit.load(splitId);
            Set<String> Files = getAllFiles(splitCommit, nowCommit, givenCommit);

            for (String fileName : Files) {
                if (splitCommit.trackTheFile(fileName)) {
                    if (modifyFile(nowCommit, splitCommit, fileName)) {
                        if (modifyFile(givenCommit, splitCommit, fileName)) {
                            if (!modifySame(nowCommit, givenCommit, fileName)) {//3b
                                conflictFlag = true;
                                StringBuilder sb = new StringBuilder("<<<<<<< HEAD\n");
                                if (nowCommit.trackTheFile(fileName)) {
                                    String tmp = new String(Blob.load(nowCommit.getFileHash(fileName)).getContent());
                                    sb.append(tmp);
                                }
                                sb.append("=======\n");
                                if (givenCommit.trackTheFile(fileName)) {
                                    String tmp = new String(Blob.load(givenCommit.getFileHash(fileName)).getContent());
                                    sb.append(tmp);
                                }
                                sb.append(">>>>>>>");
                                String content = sb.toString();

                                String hash = sha1(content);
                                stageArea.addFileInAddition(fileName, hash);
                                Blob blob = new Blob(content.getBytes(), hash);
                                blob.save(hash);
                            }
                        }
                    } else {
                        if (modifyFile(givenCommit, splitCommit, fileName)) {
                            if (givenCommit.trackTheFile(fileName)) { //1
                                stageArea.addFileInAddition(fileName, givenCommit.getFileHash(fileName));
                            } else { //6
                                stageArea.addFileInRemoval(fileName);
                            }
                        }
                    }
                } else {
                    if (nowCommit.trackTheFile(fileName)) {
                        if (givenCommit.trackTheFile(fileName)) {
                            if (!modifySame(nowCommit, givenCommit, fileName)) { //3b
                                conflictFlag = true;
                                StringBuilder sb = new StringBuilder("<<<<<<< HEAD\n");

                                String tmp = new String(Blob.load(nowCommit.getFileHash(fileName)).getContent());
                                sb.append(tmp);

                                sb.append("=======\n");

                                String tmp1 = new String(Blob.load(givenCommit.getFileHash(fileName)).getContent());
                                sb.append(tmp1);

                                sb.append(">>>>>>>");
                                String content = sb.toString();

                                String hash = sha1(content);
                                stageArea.addFileInAddition(fileName, hash);
                                Blob blob = new Blob(content.getBytes(), hash);
                                blob.save(hash);
                            }
                        }
                    } else {
                        if (givenCommit.trackTheFile(fileName)) { //5
                            stageArea.addFileInAddition(fileName, givenCommit.getFileHash(fileName));
                        }
                    }
                }
            }

            stageArea.save();
            String message = String.format("Merged %s into %s.", branchName, Branch.getCurrentBranchName());
            commit(message, givenCommit.getHash());
            if (conflictFlag) {
                System.out.println("Encountered a merge conflict.");
            }
        }

    }
}
