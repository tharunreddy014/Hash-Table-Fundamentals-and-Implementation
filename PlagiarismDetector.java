import java.util.*;

public class PlagiarismDetector {
    private final Map<String, Set<String>> ngramDatabase = new HashMap<>();
    private final int N = 5;

    public void addDocument(String docId, String content) {
        for (String ngram : extractNGrams(content)) {
            ngramDatabase.computeIfAbsent(ngram, k -> new HashSet<>()).add(docId);
        }
    }

    public void analyzeDocument(String content) {
        List<String> queryNGrams = extractNGrams(content);
        Map<String, Integer> matches = new HashMap<>();

        for (String ngram : queryNGrams) {
            if (ngramDatabase.containsKey(ngram)) {
                for (String docId : ngramDatabase.get(ngram)) {
                    matches.put(docId, matches.getOrDefault(docId, 0) + 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : matches.entrySet()) {
            double similarity = (entry.getValue() * 100.0) / queryNGrams.size();
            String status = similarity > 60 ? "PLAGIARISM DETECTED" : "suspicious";
            System.out.printf("Found %d matching n-grams with %s. Similarity: %.1f%% (%s)%n",
                    entry.getValue(), entry.getKey(), similarity, status);
        }
    }

    private List<String> extractNGrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i <= words.length - N; i++) {
            ngrams.add(String.join(" ", Arrays.copyOfRange(words, i, i + N)));
        }
        return ngrams;
    }
}