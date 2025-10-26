package localbbs.model;

import java.time.LocalDateTime;

public  class PrivateMessage {
    private int id;
    private int senderId;
    private int receiverId;
    private String message;
    private LocalDateTime sentAt;
    private boolean isRead;

    public PrivateMessage(int id, int senderId, int receiverId, String message, LocalDateTime sentAt, boolean isRead) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }

    public PrivateMessage(int senderId, int receiverId, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.sentAt = LocalDateTime.now();
        this.isRead = false;
    }

    // Getters
    public int getId() { return id; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getMessage() { return message; }
    public LocalDateTime getSentAt() { return sentAt; }
    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    // 👇 Add this for testing
    public static void main(String[] args) {
        PrivateMessage msg = new PrivateMessage(1, 2, "Hello, how are you?");
        System.out.println("Sender ID: " + msg.getSenderId());
        System.out.println("Receiver ID: " + msg.getReceiverId());
        System.out.println("Message: " + msg.getMessage());
        System.out.println("Sent At: " + msg.getSentAt());
        System.out.println("Is Read: " + msg.isRead());
    }
}
