package database;

import model.Bulletin;
import model.User;
import model.Message;
import network.ClientHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDB {
    private List<User> users = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<Bulletin> bulletins = new ArrayList<>();
    private Map<String, ClientHandler> onlineUsers = new ConcurrentHashMap<>(); // thread-safe

    // User management
    public synchronized void addUser(String username, String password) {
        users.add(new User(username, password));
    }

    public boolean login(String username, String password) {
        return users.stream()
                .anyMatch(u -> u.getUsername().equals(username) && u.getPassword().equals(password));
    }

    // Online user management
    public void addOnlineUser(String username, ClientHandler handler) {
        onlineUsers.put(username, handler);
    }

    public void removeOnlineUser(String username) {
        onlineUsers.remove(username);
    }

    public boolean isUserOnline(String username) {
        return onlineUsers.containsKey(username);
    }

    public ClientHandler getOnlineUserHandler(String username) {
        return onlineUsers.get(username);
    }

    public Collection<ClientHandler> getOnlineUsers() {
        return onlineUsers.values();
    }

    // Messages
    public synchronized void addMessage(String sender, String receiver, String content) {
        messages.add(new Message(sender, receiver, content));
    }

    public List<Message> getMessages(String username) {
        List<Message> userMessages = new ArrayList<>();
        for (Message m : messages) {
            if (m.getReceiver().equals(username)) userMessages.add(m);
        }
        return userMessages;
    }

    // Bulletins
    public synchronized void addBulletin(String poster, String content) {
        bulletins.add(new Bulletin(poster, content));
    }

    public List<Bulletin> getAllBulletins() {
        return bulletins;
    }
}
