public class ChatBot {
    private String name;
    private boolean isRunning;

    public ChatBot(String name) {
        this.name = name;
        this.isRunning = true;
    }

    public void run(){
        helloUser();
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
}
