import org.jetbrains.annotations.NotNull;
import org.jline.terminal.TerminalBuilder;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.LineReader;
import org.jline.reader.Completer;
import com.google.common.base.CaseFormat;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.ArrayList;

public class jCmd {

    private static final String PROMPT_PREFIX = "~ ";
    private static final String COM_PREFIX = "do_";
    private static final String HELP_PREFIX = "help_";
    private final LineReader reader;

    public boolean isExit = false;

    public jCmd(Completer completer) throws IOException {
        this.reader = LineReaderBuilder.builder()
                .terminal(TerminalBuilder.terminal())
                .completer(completer)
                .build();
    }

    static class Parser {

        public static final String DEFAULT = "default";

        private final String command;
        private final String[] arguments;

        public Parser(String userInput) {
            String[] input = userInput.split(" ");
            command = !input[0].isEmpty() ? input[0] : DEFAULT;
            arguments = input.length > 1 ? pickArgs(input) : new String[] {DEFAULT};
        }

        public String getCommand() {
            return command;
        }

        public String[] getArgs() {
            return arguments;
        }

        private String[] pickArgs(String[] inp) {
            int len = inp.length - 1;
            String[] args = new String[len];
            System.arraycopy(inp, 1, args, 0, len);
            return args;
        }

    }

    public static String getMethodName(@NotNull String prefix, @NotNull String command) {
        return prefix + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,
                command.replace('-', '_')
        );
    }

    public static String getCommandName(@NotNull String methodName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                methodName.substring(COM_PREFIX.length())
        ).replace("_", "-");
    }

    public void cmdLoop() {
        do {
            try {
                executeCommand(reader.readLine(PROMPT_PREFIX));
            } catch (InvocationTargetException | IllegalAccessException e) {
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        } while (!isExit);
    }

    public void executeCommand(@NotNull String command) throws InvocationTargetException, IllegalAccessException {
        Parser parser = new Parser(command);
        String methodName = getMethodName(COM_PREFIX, parser.getCommand());
        try {
            Method method = this.getClass().getMethod(methodName, String[].class);
            method.invoke(this, new Object[] {parser.getArgs()});
        } catch (NoSuchMethodException e) {
            System.out.println("No such function: " + methodName);
        }
    }

    public void do_Help(@NotNull String... args) {
        if (!args[0].equals(Parser.DEFAULT)) {
            String methodName = getMethodName(HELP_PREFIX, args[0]);
            try {
                Method helpMethod = this.getClass().getDeclaredMethod(methodName, String[].class);
                helpMethod.invoke(this, new Object[] {new String[] {}});
            } catch (NoSuchMethodException e) {
                System.out.println("No such function: " + methodName);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        } else {
            Method[] methods = this.getClass().getMethods();
            ArrayList<String> comNames = new ArrayList<>();
            for (Method method : methods) {
                String fullName = method.getName();
                String prefix = fullName.substring(0, COM_PREFIX.length());
                if (prefix.equals(COM_PREFIX)) {
                    comNames.add(getCommandName(fullName));
                }
            }
            Collections.sort(comNames);
            for (String name : comNames) {
                if (!name.equals(Parser.DEFAULT)) {
                    System.out.println("\t" + name);
                }
            }
        }
    }

    public void do_Exit(String... args) {
        isExit = true;
    }

    public void do_Default(String... args) {}

}
