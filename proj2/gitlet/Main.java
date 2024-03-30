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
                judgeTheNumberOrFormatOfOperands(args, 1);
                Repository.init();
                break;

            case "add":
                // TODO: handle the `add [filename]` command
                judgeTheNumberOrFormatOfOperands(args, 2);
                Repository.add(args[1]);
                break;

            case "commit":
                // TODO:1
                break;

            case "checkout":
                // TODO:2
                break;

            case "log":
                //TODO:3
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

    public static void judgeTheNumberOrFormatOfOperands(String[] args, int mount) {
        if (args.length != mount) {
            Utils.exitWithMessage("Incorrect operands.");
        }
    }
}
