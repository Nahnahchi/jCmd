# jCmd
A small framework for building interactive command line applications.

This is a wrapper to jline3 library that will allow you to bind any cli command to a function.

For example, a verb named `execute-this-command` will be bound to a function `do_execute_this_command`. Similarly you can implement `help` functions that start with the prefix `help_`.

```Java
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

    void do_test(String... args) {
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
```
