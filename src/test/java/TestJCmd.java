import org.jline.reader.impl.completer.StringsCompleter;
import java.io.IOException;

public class TestJCmd extends jCmd {

    public TestJCmd() throws IOException {
        super(new StringsCompleter("test", "another-test", "help", "exit"));
    }

    public void do_Exit(String... args) {
        isExit = true;
    }

    public void help_Test(String... args) {
        System.out.println("Usage:\ttest [option]");
    }

    public void do_Test(String... args) {
        switch (args[0]) {
            case "1":
                System.out.println("Test 1 complete");
                break;
            case "2":
                System.out.println("Test 2 complete");
                break;
            default:
                System.out.println("Default test complete");
                break;
        }
    }

    public void help_AnotherTest(String... args) {
        System.out.println("Usage:\tanother-test [option]");
    }

    public void do_AnotherTest(String... args) {
        if (!args[0].equals("default")) {
            System.out.println(args[0]);
        } else {
            System.out.println("No arguments given");
        }
    }

    public static void main(String[] args) {
        try {
            TestJCmd test = new TestJCmd();
            test.cmdLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
