import org.jetbrains.annotations.NotNull;
import org.jline.terminal.TerminalBuilder;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.LineReader;
import org.jline.reader.Completer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Parser {

    private String command;
    private String[] arguments;

    Parser(String userInput) {
        String[] input = userInput.split(" ");
        command = input[0];
        arguments = input.length > 1 ? pickArgs(input) : new String[] {""};
    }

    String getCommand() {
        return command;
    }

    String[] getArgs() {
        return arguments;
    }

    private static String[] pickArgs(String[] inp) {
        int len = inp.length - 1;
        String[] args = new String[len];
        System.arraycopy(inp, 1, args, 0, len);
        return args;
    }

}

class Cmd {

    private static final String PROMPT_PREFIX = "~ ";
    private static final String COM_PREFIX = "do_";
    private static final String HELP_PREFIX = "help_";
    private final LineReader reader;


    Cmd(Completer completer) throws IOException {
        this.reader = LineReaderBuilder.builder()
                .terminal(TerminalBuilder.terminal())
                .completer(completer)
                .build();
    }

    private static String getMethodName(String prefix, String command) {
        return prefix + command.replace('-', '_');
    }

    void cmdLoop() throws InvocationTargetException, IllegalAccessException {
        while (true) {
            String command = "";
            command = reader.readLine(PROMPT_PREFIX);
            executeCommand(command);
        }
    }

    private void executeCommand(@NotNull String command) throws InvocationTargetException, IllegalAccessException {
        Parser parser = new Parser(command);
        String com = parser.getCommand();
        String[] arg = parser.getArgs();
        String methodName = getMethodName(COM_PREFIX, com);
        Method method;
        try {
            method = this.getClass().getDeclaredMethod(methodName, String[].class);
            method.invoke(this, new Object[] {arg});
        } catch (NoSuchMethodException e) {
            System.out.println("No such function: " + methodName);
        }
    }

    public void do_help(@NotNull String... args) {
        if (!args[0].isEmpty()) {
            String methodName = getMethodName(HELP_PREFIX, args[0]);
            try {
                Method helpMethod = this.getClass().getDeclaredMethod(methodName, String[].class);
                helpMethod.invoke(this, new Object[] {new String[] {}});
            } catch (NoSuchMethodException e) {
                System.out.println("No such function: " + methodName);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.println(e.getMessage());
            }
        } else {
            Method[] methods = this.getClass().getDeclaredMethods();
            for (Method method : methods) {
                String fullName = method.getName();
                String prefix = fullName.substring(0, COM_PREFIX.length());
                if (prefix.equals(COM_PREFIX)) {
                    System.out.println("\t" + fullName.substring(COM_PREFIX.length()));
                }
            }
        }
    }

}
