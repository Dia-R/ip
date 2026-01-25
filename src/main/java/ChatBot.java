import java.util.Scanner;

public class ChatBot {
    private String name;
    private boolean isRunning;
    private Scanner scanner;

    public ChatBot(String name) {
        this.name = name;
        this.isRunning = true;
        this.scanner = new Scanner(System.in);
    }

    public void run(){
        helloUser();

        while (isRunning) {
            String userCommand = scanner.nextLine();
            executeCommand(userCommand);
        }
        byeUser();
    }

    private void helloUser() {
        String hello = "Hello, furrr-iend! Do you need a helping paw?";
        System.out.println(hello);
    }
    private void byeUser() {
        String hello = "Aww, see mew next time!";
        System.out.println(hello);
    }

    private void executeCommand(String userCommand) {
        if (userCommand.equalsIgnoreCase("bye")) {
            isRunning = false;
        } else {
            System.out.println(userCommand);
        }
    }
}
