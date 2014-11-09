//@author A0116014Y
package bakatxt.core;

import bakatxt.gui.BakaUI;

public class BakaTxtMain {

    private static BakaProcessor _processor;

    private static final String CLI = "--cli";

    public BakaTxtMain() {
        _processor = new BakaProcessor();
    }

    public static void main(String[] args) {

        BakaTxtMain thisSession = new BakaTxtMain();

        if (isCLI(args)) {
            BakaCommandLine.startCli(_processor);
        }
        BakaUI.startGui(_processor);
    }

    //@author A0116538A
    /**
     * This method checks if the flag to initiate the CLI for bakatxt is in the
     * arguments specified to the program in the terminal.
     *
     * note: this is a simple implementation. It would (probably) be much better
     *       to parse the flags properly for extensibility in the future. However,
     *       since there is only one flag that we actually even need, this solution
     *       is " Good Enough™ ".
     *
     * @param args
     *        the String array we are checking
     * @return true if the array contains the flag to start the CLI
     */
     private static boolean isCLI (String[] args) {
         for (int i = 0; i < args.length; i++) {
             if (args[i].trim().equalsIgnoreCase(CLI)) {
                 return true;
             }
         }
         return false;
     }
}
