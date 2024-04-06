package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Repository.CURRENT_BRANCH_FILE;
import static gitlet.Repository.REFS_DIR;
import static gitlet.Utils.join;
import static gitlet.Utils.writeContents;

public class Branch {
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

    public static String getCurrentBranchName() {
        return Utils.readContentsAsString(CURRENT_BRANCH_FILE);
    }

    public static void updateCurrentBranch(String branchName) {
        Utils.writeContents(CURRENT_BRANCH_FILE, branchName);
    }
}
