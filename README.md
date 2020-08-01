# jCmd
A small framework for building interactive command line applications.

This is a wrapper to [jline3](https://github.com/jline/jline3) library that will allow you to bind any cli command to a method.

For example, a verb named `execute-this-command` will be bound to a method `do_ExecuteThisCommand()`. Similarly you can implement `help` methods that start with the prefix `help_`.

Override the `do_Default()` method to execute something when no user input is given.

```Java
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
```
