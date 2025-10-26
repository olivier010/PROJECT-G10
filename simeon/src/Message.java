package com.lumbbs;

import java.time.LocalDateTime;

/**
 * Represents a single message or bulletin in the LUM-BBS system.
 */
public class Message {
    
    // Enum to easily distinguish message types
    public enum Type {
        PRIVATE,  // NetMail to a specific user
        BULLETIN  // Public post to a category
    }

    private static int nextId = 1;
    
    private final int id;
    private final Type type;
    private final String sender;
    private final String recipient; // User for PRIVATE, Category for BULLETIN
    private final String subject;
    private final String body;
    private final LocalDateTime timestamp;

    /**
     * Constructs a new Message (used for both PRIVATE and BULLETIN types).
     *
     * @param type       The type of message (PRIVATE or BULLETIN).
     * @param sender     The username of the sender.
     * @param recipient  The target (username for PRIVATE, category for BULLETIN).
     * @param subject    The subject line of the message.
     * @param body       The content of the message.
     */
    public Message(Type type, String sender, String recipient, String subject, String body) {
        this.id = nextId++;
        this.type = type;
        this.sender = sender;
        this.recipient = recipient.toUpperCase(); // Standardize category/recipient
        this.subject = subject;
        this.body = body;
        this.timestamp = LocalDateTime.now();
    }

    // --- Accessor Methods (Getters) ---
    
    public int getId() { return id; }
    public Type getType() { return type; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Returns a formatted header for listing messages.
     *
     * @return A string summary of the message.
     */
    public String getHeader() {
        String typeLabel = (this.type == Type.BULLETIN) ? "BULLETIN" : "PRIVATE";
        return String.format(
            "| #%-4d | %-8s | TO: %-10s | FROM: %-10s | %s",
            id, typeLabel, recipient, sender, subject
        );
    }
}