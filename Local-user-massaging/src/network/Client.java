package network;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            // Thread to print server messages asynchronously
            new Thread(() -> {
                String serverMsg;
                try {
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println(serverMsg);
                    }
                } catch (IOException e) { e.printStackTrace(); }
            }).start();

            boolean running = true;
            while (running) {
                // === Main Menu ===
                System.out.println("\n===== MAIN MENU =====");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                String choice = userInput.readLine();

                switch (choice) {
                    case "1": // Register
                        System.out.print("Enter username: ");
                        out.println(userInput.readLine());
                        System.out.print("Enter password: ");
                        out.println(userInput.readLine());
                        Thread.sleep(300); // wait for server response
                        break;
                    case "2": // Login
                        System.out.print("Enter username: ");
                        out.println(userInput.readLine());
                        System.out.print("Enter password: ");
                        out.println(userInput.readLine());
                        Thread.sleep(300);
                        // Enter user menu after login
                        userMenu(userInput, out);
                        break;
                    case "0": // Exit
                        System.out.println("Exiting...");
                        running = false;
                        socket.close();
                        break;
                    default:
                        System.out.println("Invalid option! Enter 0, 1, or 2.");
                        break;
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void userMenu(BufferedReader userInput, PrintWriter out) throws IOException {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n===== USER MENU =====");
            System.out.println("3. Send Private Message");
            System.out.println("4. Post Bulletin");
            System.out.println("5. Show My Messages");
            System.out.println("6. Show All Bulletins");
            System.out.println("0. Logout / Back");
            System.out.print("Choose an option: ");
            String option = userInput.readLine();

            switch (option) {
                case "3": // Send message
                    System.out.print("Enter receiver username: ");
                    String receiver = userInput.readLine();
                    System.out.print("Enter message: ");
                    String msg = userInput.readLine();
                    out.println("MSG:" + receiver + ";" + msg);
                    break;
                case "4": // Post bulletin
                    System.out.print("Enter bulletin content: ");
                    String content = userInput.readLine();
                    out.println("BULLETIN:" + content);
                    break;
                case "5": // Show messages
                    out.println("SHOW MESSAGES");
                    break;
                case "6": // Show bulletins
                    out.println("SHOW BULLETINS");
                    break;
                case "0": // Logout
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option! Enter 0-6.");
                    break;
            }
        }
    }
}
