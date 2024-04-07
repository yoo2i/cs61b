package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        insureArgsIsNotEmpty(args);

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                judgeTheNumber(args, 1);
                Repository.init();
                break;

            case "add":
                // TODO: handle the `add [filename]` command
                judgeTheNumber(args, 2);
                Repository.add(args[1]);
                break;

            case "commit":
                judgeTheNumber(args, 2);
                Repository.commit(args[1]);
                break;

            case "rm":
                // TODO:2
                judgeTheNumber(args, 2);
                Repository.rm(args[1]);
                break;

            case "log":
                //TODO:3
                judgeTheNumber(args, 1);
                Repository.log();
                break;

            case "global-log":
                judgeTheNumber(args, 1);
                Repository.globalLog();
                break;

            case "find":
                judgeTheNumber(args, 2);
                Repository.find(args[1]);
                break;

            case "status":
                judgeTheNumber(args, 1);
                Repository.status();
                break;

            case "checkout":
                int flag = judgeForCheckout(args);
                switch(flag) {
                    case 0:
                        Repository.checkoutForBranch(args[1]);
                        break;
                    case 1:
                        Repository.checkoutForFile(args[2]);
                        break;
                    case 2:
                        Repository.checkoutForFile(args[1], args[3]);
                        break;
                }
                break;

            case "branch":
                judgeTheNumber(args, 2);
                Repository.branch(args[1]);
                break;

            case "rm-branch":
                judgeTheNumber(args, 2);
                Repository.rmBranch(args[1]);
                break;

            case "reset":
                judgeTheNumber(args, 2);
                Repository.reset(args[1]);
                break;

            case "merge":
                judgeTheNumber(args, 2);
                Repository.merge(args[1]);
                break;

            default:
                Utils.exitWithMessage("No command with that name exists.");
        }
    }

    public static void insureArgsIsNotEmpty(String[] args) {
        if (args.length == 0) {
            Utils.exitWithMessage("Please enter a command.");
        }
    }

    public static void judgeTheNumber(String[] args, int mount) {
        if (args.length != mount) {
            Utils.exitWithMessage("Incorrect operands.");
        }
    }

    /** case1: java gitlet.Main checkout -- [file name]
     *  case2: java gitlet.Main checkout [commit id] -- [file name]
     *  case0: java gitlet.Main checkout [branch name]
     */
    public static int judgeForCheckout(String[] args) {
        if (args.length == 2) {
            if (args[1].equals("--")) {
                Utils.exitWithMessage("Incorrect operands.");
            }
            return 0;
        } else if (args.length == 3) {
            if (!args[1].equals("--")) {
                Utils.exitWithMessage("Incorrect operands.");
            }
            return 1;
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                Utils.exitWithMessage("Incorrect operands.");
            }
            return 2;
        } else {
            Utils.exitWithMessage("Incorrect operands.");
            return 999;
        }
    }
}
