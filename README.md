# jCmd
A small framework for building interactive command line applications.

This is a wrapper to [jline3](https://github.com/jline/jline3) library that will allow you to bind any cli command to a method.

For example, a verb named `execute-this-command` will be bound to a method `do_ExecuteThisCommand()`. Similarly you can implement `help` methods that start with the prefix `help_`. All bound methods need to be *public*.

Override the `do_Default()` method to execute something when no user input is given.

```Java
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
```
