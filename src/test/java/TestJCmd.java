import org.jline.reader.impl.completer.StringsCompleter;
import java.io.IOException;

public class TestJCmd extends jCmd {

    public TestJCmd() throws IOException {
        super(new StringsCompleter("test", "help", "exit"));
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
            case Parser.DEFAULT:
                System.out.println("No arguments given");
                break;
        }
    }

    @Override
    public void do_Exit(String... args) {
        System.out.println("Exiting...");
        super.do_Exit(args);
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
