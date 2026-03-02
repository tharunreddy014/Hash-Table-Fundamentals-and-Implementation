import java.util.*;
import java.util.concurrent.*;

public class FlashSaleInventoryManager {
    private final ConcurrentHashMap<String, Integer> inventory;
    private final Map<String, LinkedHashSet<Integer>> waitingLists;

    public FlashSaleInventoryManager() {
        this.inventory = new ConcurrentHashMap<>();
        this.waitingLists = new HashMap<>();
    }

    public void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingLists.put(productId, new LinkedHashSet<>());
    }

    public int checkStock(String productId) {
        return inventory.getOrDefault(productId, 0);
    }

    public String purchaseItem(String productId, int userId) {
        synchronized (productId.intern()) {
            int currentStock = inventory.getOrDefault(productId, 0);

            if (currentStock > 0) {
                inventory.put(productId, currentStock - 1);
                return "Success, " + (currentStock - 1) + " units remaining";
            } else {
                LinkedHashSet<Integer> waitList = waitingLists.get(productId);
                if (waitList.add(userId)) {
                    int position = waitList.size();
                    return "Added to waiting list, position #" + position;
                }
                return "Already in waiting list";
            }
        }
    }

    public static void main(String[] args) {
        FlashSaleInventoryManager manager = new FlashSaleInventoryManager();
        manager.addProduct("IPHONE15_256GB", 100);

        System.out.println(manager.checkStock("IPHONE15_256GB") + " units available");
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 98; i++) {
            manager.purchaseItem("IPHONE15_256GB", 2000 + i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}