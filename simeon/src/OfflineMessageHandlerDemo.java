import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a private message in the system.
 */
class PrivateMessage {
    private final String senderId;
    private final String recipientId;
    private final String content;
    private final long timestamp;
    private boolean isDelivered;

    public PrivateMessage(String senderId, String recipientId, String content) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
        this.isDelivered = false;
    }

    // --- Getters ---
    public String getRecipientId() { return recipientId; }
    public String getContent() { return content; }
    public boolean isDelivered() { return isDelivered; }

    // --- Actions ---
    public void markAsDelivered() { this.isDelivered = true; }

    @Override
    public String toString() {
        return "FROM: " + senderId + ", CONTENT: '" + content + "'";
    }
}

// --------------------------------------------------------------------------------------------------

/**
 * Service to manage message persistence and delivery for offline users.
 * This simulates the server-side logic required for the "Handle offline users" task.
 */
class OfflineMessageService {

    // Instance variable for message storage (simulates a database or cache).
    private final Map<String, List<PrivateMessage>> offlineStore = new HashMap<>();

    /**
     * Tries to deliver a message directly, or saves it if the recipient is offline.
     * @param message The message to send.
     * @param isRecipientOnline A simulated check for the user's connection status.
     */
    public void processMessage(PrivateMessage message, boolean isRecipientOnline) {
        if (isRecipientOnline) {
            // SCENARIO 1: Recipient is online. Deliver directly (simulated).
            System.out.println("✅ DIRECT DELIVERY to " + message.getRecipientId() + ": " + message.getContent());
            message.markAsDelivered();
        } else {
            // SCENARIO 2: Recipient is offline. Store for later.
            offlineStore
                    .computeIfAbsent(message.getRecipientId(), k -> new ArrayList<>())
                    .add(message);
            System.out.println("📦 STORED for offline user " + message.getRecipientId() + ": " + message.getContent());
        }
    }

    /**
     * Called when a user reconnects to the service. Checks for and delivers waiting messages.
     * @param userId The ID of the user who just connected.
     */
    public void deliverPendingMessages(String userId) {
        System.out.println("\n--- User Reconnect: " + userId + " ---");

        // 1. Retrieve all messages for the user.
        List<PrivateMessage> pendingMessages = offlineStore.getOrDefault(userId, new ArrayList<>());

        // 2. Filter for truly undelivered messages.
        List<PrivateMessage> messagesToDeliver = pendingMessages.stream()
                .filter(m -> !m.isDelivered())
                .collect(Collectors.toList());

        if (messagesToDeliver.isEmpty()) {
            System.out.println("👍 No pending messages found.");
            return;
        }

        System.out.println("🚚 DELIVERING " + messagesToDeliver.size() + " messages to " + userId + "...");

        // 3. Simulate delivery and mark as delivered.
        for (PrivateMessage msg : messagesToDeliver) {
            // [TODO: INTEGRATION] Actual delivery mechanism (e.g., sending over a socket) would go here.
            System.out.println("  -> Delivered: " + msg.toString());
            msg.markAsDelivered();
        }

        // 4. Clean up the store if all messages were delivered.
        if (pendingMessages.stream().allMatch(PrivateMessage::isDelivered)) {
            offlineStore.remove(userId);
            System.out.println("🧹 Store cleaned for " + userId + ".");
        }
    }
}

// --------------------------------------------------------------------------------------------------

/**
 * Main class to demonstrate the offline message handling logic.
 */
public class OfflineMessageHandlerDemo {
    public static void main(String[] args) {
        // Must create an instance of the service
        OfflineMessageService service = new OfflineMessageService();

        // --- SCENARIO SETUP ---
        String userA = "userA";
        String userB = "userB"; // This user will be offline initially
        String userC = "userC"; // This user will be online

        System.out.println("--- PHASE 1: User B is OFFLINE ---");

        // 1. Message to an OFFLINE user (user B)
        service.processMessage(new PrivateMessage(userA, userB, "Are you there?"), false);

        // 2. Another message to the OFFLINE user (user B)
        service.processMessage(new PrivateMessage(userA, userB, "I'll be waiting."), false);

        // 3. Message to an ONLINE user (user C)
        service.processMessage(new PrivateMessage(userA, userC, "Hello C!"), true);

        // ---------------------------------------------------------------------------------

        System.out.println("\n--- PHASE 2: User B Connects ---");

        // 4. User B logs in/reconnects. Triggers the delivery of pending messages.
        service.deliverPendingMessages(userB);

        // ---------------------------------------------------------------------------------

        System.out.println("\n--- PHASE 3: User B is now ONLINE ---");

        // 5. Send a new message while user B is now ONLINE (simulated)
        service.processMessage(new PrivateMessage(userA, userB, "You must be back now!"), true);
    }
}