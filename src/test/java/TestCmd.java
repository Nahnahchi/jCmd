import org.jetbrains.annotations.NotNull;
import org.jline.reader.impl.completer.StringsCompleter;

import java.io.IOException;

public class TestCmd extends Cmd {

    TestCmd() throws IOException {
        super(new StringsCompleter("test", "help", "exit"));
    }

    void do_exit(String... args) {
        System.exit(0);
    }

    void help_test(String... args) {
        System.out.println("Usage:\ttest [option]");
    }

    void do_test(@NotNull String... args) {
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

    public static void main(String[] args) {
        try {
            TestCmd test = new TestCmd();
            test.cmdLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
