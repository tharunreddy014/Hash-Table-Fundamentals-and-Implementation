import java.util.*;

public class DNSCacheSystem {
    private final int capacity;
    private final Map<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;

    class DNSEntry {
        String ipAddress;
        long expiryTime;

        DNSEntry(String ipAddress, int ttlSeconds) {
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
        }
    }

    public DNSCacheSystem(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCacheSystem.this.capacity;
            }
        };
    }

    public synchronized String resolve(String domain) {
        DNSEntry entry = cache.get(domain);

        if (entry != null) {
            if (System.currentTimeMillis() < entry.expiryTime) {
                hits++;
                return entry.ipAddress + " (HIT)";
            } else {
                cache.remove(domain);
            }
        }

        misses++;
        String ip = queryUpstreamDNS(domain);
        cache.put(domain, new DNSEntry(ip, 300)); 
        return ip + " (MISS/QUERY UPSTREAM)";
    }

    private String queryUpstreamDNS(String domain) {
        return "172.217.14." + (new Random().nextInt(254) + 1);
    }

    public String getCacheStats() {
        double total = hits + misses;
        double hitRate = (total == 0) ? 0 : (hits / total) * 100;
        return String.format("Hit Rate: %.1f%%, Hits: %d, Misses: %d", hitRate, hits, misses);
    }

    public static void main(String[] args) throws InterruptedException {
        DNSCacheSystem dns = new DNSCacheSystem(2);

        System.out.println("google.com: " + dns.resolve("google.com"));
        System.out.println("google.com: " + dns.resolve("google.com"));
        
        System.out.println("example.com: " + dns.resolve("example.com"));
        System.out.println("openai.com: " + dns.resolve("openai.com")); 

        System.out.println(dns.getCacheStats());
    }
}