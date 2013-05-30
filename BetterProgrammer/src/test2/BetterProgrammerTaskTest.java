package test2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BetterProgrammerTaskTest {
    @Test
    public void shouldCapitalizeSingleWord() {
        assertEquals("Capitalized", (new BetterProgrammerTask()).capitalizeFirstLetters("capitalized"));
    }

    @Test
    public void shouldNotAlterSingleWordStartingWithNotChar() {
        assertEquals("[apitalized", (new BetterProgrammerTask()).capitalizeFirstLetters("[apitalized"));
    }

    @Test
    public void shouldNotAlterCapitalizedWords() {
        assertEquals("Capitalized", (new BetterProgrammerTask()).capitalizeFirstLetters("Capitalized"));
    }

    @Test
    public void shouldCapitalizeDoubleWordSingleSpaced() {
        assertEquals("Capitalized Word", (new BetterProgrammerTask()).capitalizeFirstLetters("capitalized word"));
    }

    @Test
    public void shouldCapitalizeDoubleWordPreservingSeparatingSpaces() {
        assertEquals("Capitalized   Word", (new BetterProgrammerTask()).capitalizeFirstLetters("capitalized   word"));
    }

    @Test
    public void shouldPreserveTrailingSpaces() {
        assertEquals("Capitalized Word   ", (new BetterProgrammerTask()).capitalizeFirstLetters("capitalized word   "));
    }

    @Test
    public void shouldPreserveLeadingSpaces() {
        assertEquals("  Capitalized Word", (new BetterProgrammerTask()).capitalizeFirstLetters("  capitalized word"));
    }

    @Test
    public void shouldSupportCombined() {
        assertEquals("  Capitalized Word  5ass 123aa -wod Wod  ", (new BetterProgrammerTask()).capitalizeFirstLetters("  capitalized Word  5ass 123aa -wod wod  "));
    }
}
