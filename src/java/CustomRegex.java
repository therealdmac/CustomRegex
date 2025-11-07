import java.util.HashMap;
import java.util.Map;

public class CustomRegex {

    public static boolean match(String text, String pattern) {
        // DP memo: key = i<<32 | j, value = match result for pattern[i:] vs text[j:]
        Map<Long, Boolean> memo = new HashMap<>();
        return match(text, pattern, 0, 0, memo);
    }

    private static boolean match(String text, String pattern, int i, int j, Map<Long, Boolean> memo) {
        long key = (((long) i) << 32) ^ (j & 0xffffffffL);
        if (memo.containsKey(key)) return memo.get(key);

        // If we've consumed the whole pattern, text must also be fully consumed
        if (i >= pattern.length()) {
            boolean ans = (j == text.length());
            memo.put(key, ans);
            return ans;
        }

        char p = pattern.charAt(i);

        // Handle '*' meaning "repeat the NEXT char 0 or more times"
        if (p == '*') {
            // Invalid if '*' is last or there's no next char to repeat
            if (i + 1 >= pattern.length()) {
                memo.put(key, false);
                return false;
            }
            char next = pattern.charAt(i + 1);

            // Try k occurrences of `next` starting at j, including k = 0
            // We advance pattern by 2 (skip '*' and the repeated char) when done consuming.
            // We must try all k (greedy with backtracking).
            int t = j;

            // First, try zero occurrences
            if (match(text, pattern, i + 2, t, memo)) {
                memo.put(key, true);
                return true;
            }

            // Then try one or more occurrences as long as next matches
            while (t < text.length() && charMatches(text.charAt(t), next)) {
                t++;
                if (match(text, pattern, i + 2, t, memo)) {
                    memo.put(key, true);
                    return true;
                }
            }

            memo.put(key, false);
            return false;
        }

        // Normal literal or '.'
        if (j >= text.length()) {
            memo.put(key, false);
            return false;
        }

        if (p == '.' || p == text.charAt(j)) {
            boolean ans = match(text, pattern, i + 1, j + 1, memo);
            memo.put(key, ans);
            return ans;
        }

        memo.put(key, false);
        return false;
    }

    private static boolean charMatches(char t, char p) {
        return p == '.' || p == t;
    }
}
