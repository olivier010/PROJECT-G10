import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

 class MultiUserPostTest {

    // Inner class representing one user posting
    static class UserThread extends Thread {
        private String userName;
        private String message;

        UserThread(String userName, String message) {
            this.userName = userName;
            this.message = message;
        }

        @Override
        public void run() {
            String url = "jdbc:oracle:thin:@192.168.1.10:1521/orcl";  // your Oracle DB connection
            String username = "school_admin";
            String password = "school123";

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection conn = DriverManager.getConnection(url, username, password);

                String sql = "INSERT INTO messages (sender_name, content) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userName);
                pstmt.setString(2, message);
                pstmt.executeUpdate();

                System.out.println("✅ " + userName + " posted: " + message);
                conn.close();
            } catch (Exception e) {
                System.out.println("❌ Error for " + userName + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("🔄 Simulating 3 users posting at the same time...");

        Thread user1 = new UserThread("Alice", "Hello everyone!");
        Thread user2 = new UserThread("Bob", "Good morning all!");
        Thread user3 = new UserThread("Clarisse", "Testing simultaneous post!");

        user1.start();
        user2.start();
        user3.start();
    }
}
