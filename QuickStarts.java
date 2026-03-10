public class QuickStarts {
    public static void main(String[] args) {
       
        String language = "Java";
        int yearCreated = 1995; 

        System.out.println("Exploring " + language + ", created in " + yearCreated);

        
        for (int i = 1; i <= 3; i++) {
            if (i % 2 == 0) {
                System.out.println("Step " + i + " is even.");
            } else {
                System.out.println("Step " + i + " is odd.");
            }
        }
    }
}