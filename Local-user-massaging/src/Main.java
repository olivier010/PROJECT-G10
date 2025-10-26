import java.util.*;

// User class
class User {
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

// Message class
class Message {
    String sender;
    String receiver;
    String content;

    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
}

// Bulletin class
class Bulletin {
    String poster;
    String content;

    public Bulletin(String poster, String content) {
        this.poster = poster;
        this.content = content;
    }
}

// "Database" class using ArrayLists
class InMemoryDatabase {
    List<User> users = new ArrayList<>();
    List<Message> messages = new ArrayList<>();
    List<Bulletin> bulletins = new ArrayList<>();

    // Add user
    public void addUser(String username, String password) {
        users.add(new User(username, password));
        System.out.println("User added: " + username);
    }

    // Authenticate user
    public boolean login(String username, String password) {
        for (User u : users) {
            if (u.username.equals(username) && u.password.equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Send private message
    public void sendMessage(String sender, String receiver, String content) {
        messages.add(new Message(sender, receiver, content));
        System.out.println("Message sent from " + sender + " to " + receiver);
    }

    // Post bulletin
    public void postBulletin(String poster, String content) {
        bulletins.add(new Bulletin(poster, content));
        System.out.println("Bulletin posted by " + poster);
    }

    // View all messages for a user
    public void viewMessages(String username) {
        System.out.println("Messages for " + username + ":");
        for (Message m : messages) {
            if (m.receiver.equals(username)) {
                System.out.println(m.sender + " -> " + m.content);
            }
        }
    }

    // View all bulletins
    public void viewBulletins() {
        System.out.println("Bulletin Board:");
        for (Bulletin b : bulletins) {
            System.out.println(b.poster + ": " + b.content);
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        InMemoryDatabase db = new InMemoryDatabase();

        // Add users
        db.addUser("Muhamadi", "1234");
        db.addUser("Marthe", "abcd");

        // Login
        if (db.login("Muhamadi", "1234")) {
            System.out.println("Muhamadi logged in successfully!");
        }

        // Send messages
        db.sendMessage("Muhamadi", "Marthe", "Hello Marthe!");
        db.sendMessage("Marthe", "Muhamadi", "Hi Muhamadi!");

        // Post bulletins
        db.postBulletin("Admin", "Welcome to the Bulletin Board!");

        // View messages and bulletins
        db.viewMessages("Marthe");
        db.viewBulletins();
    }
}
