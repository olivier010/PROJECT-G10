import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class BroadcastServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("Broadcast Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();

                System.out.println("New client connected. Total clients: " + clients.size());
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        if (clients.isEmpty()) {
            System.out.println("No clients connected to broadcast message.");
            return;
        }

        System.out.println("Broadcasting to " + clients.size() + " clients: " + message);

        Iterator<ClientHandler> iterator = clients.iterator();
        while (iterator.hasNext()) {
            ClientHandler client = iterator.next();
            try {
                if (client != sender) { // Don't send back to sender
                    client.sendMessage(message);
                }
            } catch (IOException e) {
                System.err.println("Failed to send to client: " + e.getMessage());
                iterator.remove();
                System.out.println("Client disconnected. Total clients: " + clients.size());
            }
        }
    }

    // Overloaded method for broadcasting without sender (for system messages)
    public static void broadcastMessage(String message) {
        broadcastMessage(message, null);
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client removed. Total clients: " + clients.size());
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error creating client handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // Get client name
            out.println("Welcome to Broadcast Server! Enter your name:");
            clientName = in.readLine();
            if (clientName == null) {
                return;
            }

            System.out.println("Client registered: " + clientName);
            // FIX: Call the static method from BroadcastServer class
            BroadcastServer.broadcastMessage("SYSTEM: " + clientName + " joined the chat", this);

            String message;
            while ((message = in.readLine()) != null) {
                if ("quit".equalsIgnoreCase(message)) {
                    break;
                }
                System.out.println("Message from " + clientName + ": " + message);
                // FIX: Call the static method from BroadcastServer class
                BroadcastServer.broadcastMessage(clientName + ": " + message, this);
            }
        } catch (IOException e) {
            System.err.println("Client handler error: " + e.getMessage());
        } finally {
            try {
                if (clientName != null) {
                    // FIX: Call the static method from BroadcastServer class
                    BroadcastServer.broadcastMessage("SYSTEM: " + clientName + " left the chat", this);
                }
                socket.close();
                BroadcastServer.removeClient(this);
                System.out.println("Client disconnected: " + clientName);
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        if (out != null) {
            out.println(message);
        }
    }
}