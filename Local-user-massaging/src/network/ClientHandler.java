package network;

import database.InMemoryDB;
import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private static final InMemoryDB db = new InMemoryDB();

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            handleLogin();
            sendOfflineMessages();
            menuLoop();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.removeOnlineUser(username);
        }
    }

    private void handleLogin() throws IOException {
        out.println("Enter username:");
        username = in.readLine();
        out.println("Enter password:");
        String password = in.readLine();

        if (!db.login(username, password)) {
            db.addUser(username, password);
            out.println("User registered successfully!");
        } else {
            out.println("Login successful!");
        }

        db.addOnlineUser(username, this);
    }

    private void sendOfflineMessages() {
        db.getMessages(username).forEach(m ->
                sendMessage("Offline message from " + m.getSender() + ": " + m.getContent())
        );
    }

    private void menuLoop() throws IOException {
        String input;
        while ((input = in.readLine()) != null) {
            if (input.startsWith("MSG:")) handlePrivateMessage(input.substring(4));
            else if (input.startsWith("BULLETIN:")) handleBulletin(input.substring(9));
            else if (input.equalsIgnoreCase("SHOW MESSAGES")) showMessages();
            else if (input.equalsIgnoreCase("SHOW BULLETINS")) showBulletins();
            else sendMessage("Unknown command or invalid input.");
        }
    }

    private void handlePrivateMessage(String data) {
        String[] parts = data.split(";", 2);
        if (parts.length != 2) {
            sendMessage("Invalid message format. Use MSG:receiver;message");
            return;
        }

        String receiver = parts[0];
        String content = parts[1];
        db.addMessage(username, receiver, content);

        if (db.isUserOnline(receiver)) {
            db.getOnlineUserHandler(receiver).sendMessage("Private from " + username + ": " + content);
        }

        sendMessage("Message sent to " + receiver);
    }

    private void handleBulletin(String content) {
        db.addBulletin(username, content);
        db.getOnlineUsers().forEach(handler ->
                handler.sendMessage("Bulletin from " + username + ": " + content)
        );
        sendMessage("Bulletin posted!");
    }

    private void showMessages() {
        db.getMessages(username).forEach(m ->
                sendMessage(m.getSender() + " -> " + m.getContent())
        );
    }

    private void showBulletins() {
        db.getAllBulletins().forEach(b ->
                sendMessage(b.getPoster() + ": " + b.getContent())
        );
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
