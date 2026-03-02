import java.util.*;

public class UsernameSystem {
   
    private HashMap<String, String> registeredUsers;

    private HashMap<String, Integer> attemptCounts;
    
    private String mostAttemptedUsername = null;
    private int maxAttempts = 0;

    public UsernameSystem() {
        this.registeredUsers = new HashMap<>();
        this.attemptCounts = new HashMap<>();
    }

    public boolean checkAvailability(String username) {
        trackAttempt(username);
        return !registeredUsers.containsKey(username);
    }

    private void trackAttempt(String username) {
        int count = attemptCounts.getOrDefault(username, 0) + 1;
        attemptCounts.put(username, count);

        if (count > maxAttempts) {
            maxAttempts = count;
            mostAttemptedUsername = username;
        }
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        int suffix = 1;

        while (suggestions.size() < 3) {
            String candidate = username + suffix;
            if (!registeredUsers.containsKey(candidate)) {
                suggestions.add(candidate);
            }
            suffix++;
        }
        return suggestions;
    }

    public String getMostAttempted() {
        if (mostAttemptedUsername == null) return "No attempts yet";
        return mostAttemptedUsername + " (" + maxAttempts + " attempts)";
    }

    public void register(String username, String userId) {
        registeredUsers.put(username, userId);
    }

    public static void main(String[] args) {
        UsernameSystem system = new UsernameSystem();

        system.register("john_doe", "USR123");

        System.out.println("Availability of john_doe: " + system.checkAvailability("john_doe")); 
        System.out.println("Availability of jane_smith: " + system.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe: " + system.suggestAlternatives("john_doe"));

        system.checkAvailability("admin");
        system.checkAvailability("admin");
        System.out.println("Most attempted: " + system.getMostAttempted());
    }
}