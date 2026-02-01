package app;

import logic.ChatBot;

/**
 * Launches the Cat ChatBot application.
 */
public class Cat {

    /**
     * Starts the ChatBot programme.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        ChatBot chatBot = new ChatBot("Cat");
        chatBot.run();
    }
}
