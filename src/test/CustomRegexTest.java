import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomRegexTest {

    @Test
    void testExactMatches() {
        assertTrue(CustomRegex.match("abc", "abc"));
        assertTrue(CustomRegex.match("aaa", "aaa"));
        assertTrue(CustomRegex.match("a", "a"));
        assertTrue(CustomRegex.match("", ""));
    }

    @Test
    void testMismatches() {
        assertFalse(CustomRegex.match("abc", "def"));
        assertFalse(CustomRegex.match("ddddf", "qqqqq"));
        assertFalse(CustomRegex.match("abc", "abcd"));
        assertFalse(CustomRegex.match("abcd", "abc"));
    }

    @Test
    void testDotOperator() {
        assertTrue(CustomRegex.match("abcd", "abc."));
        assertTrue(CustomRegex.match("abcd", "ab.d"));
        assertFalse(CustomRegex.match("ab", "a.c"));
    }

    @Test
    void testStarNextCharOperator() {
        assertTrue(CustomRegex.match("abbbbcyz", "a*bc.z"));
        assertFalse(CustomRegex.match("abbbbc", "ab*c"));
        assertTrue(CustomRegex.match("b", "b*a"));
        assertTrue(CustomRegex.match("baaaaa", "b*a"));
        assertFalse(CustomRegex.match("a", "b*a"));
    }
    
    @Test
    void emptyVsNonEmpty() {
        assertFalse(CustomRegex.match("a", ""));
        assertTrue(CustomRegex.match("", ""));
    }

    @Test
    void starAtStartRepeatsNextChar() {
        assertTrue(CustomRegex.match("", "*a"));
        assertTrue(CustomRegex.match("aaaa", "*a"));
        assertFalse(CustomRegex.match("b", "*a"));
    }

    @Test
    void starAtEndIsInvalid() {
        assertFalse(CustomRegex.match("", "*"));
        assertFalse(CustomRegex.match("anything", "a*"));
    }

    @Test
    void consecutiveStars() {
        assertTrue(CustomRegex.match("a", "**a"));
        assertTrue(CustomRegex.match("***a", "**a"));
        assertFalse(CustomRegex.match("", "**a"));
    }

    @Test
    void starFollowedByDotMeansRepeatAnyChar() {
        assertTrue(CustomRegex.match("", "*."));
        assertTrue(CustomRegex.match("xyz", "*."));
        assertTrue(CustomRegex.match("hello", "*."));
    }

    @Test
    void mixedStarDotThenLiteral() {
        assertTrue(CustomRegex.match("z", "*.z"));
        assertTrue(CustomRegex.match("helloz", "*.z"));
        assertFalse(CustomRegex.match("hello", "*.z"));
    }

    
    @Test
    void backtrackToSatisfyLaterLiteral() {
        assertTrue(CustomRegex.match("aaab", "*ab"));
        assertTrue(CustomRegex.match("b", "*ab"));
        assertFalse(CustomRegex.match("ac", "*ab"));
    }

    @Test
    void zeroOccurrencesNeeded() {
        assertTrue(CustomRegex.match("bc", "*abc"));
        assertFalse(CustomRegex.match("ac", "*abc"));
    }

    @Test
    void dotMatchesSingleCharOnly() {
        assertTrue(CustomRegex.match("x", "."));
        assertFalse(CustomRegex.match("", "."));
        assertFalse(CustomRegex.match("xy", "."));
    }

    @Test
    void dotCombinedWithLiterals() {
        assertTrue(CustomRegex.match("abz", "a.z"));
        assertFalse(CustomRegex.match("az", "a.z"));
    }

    @Test
    void longerTextThanPattern() {
        assertFalse(CustomRegex.match("abcd", "ab."));
    }

    @Test
    void longerPatternThanText() {
        assertFalse(CustomRegex.match("ab", "ab."));
    }
}
