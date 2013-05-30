package test1;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class BetterProgrammerTaskTest {

    @Test
    public void shouldReturnNullForNegativeValues() {
        assertNull((new BetterProgrammerTask()).getCorrectChange(-1));
        assertNull((new BetterProgrammerTask()).getCorrectChange(-100));
    }

    @Test
    public void shouldReturnRightChangeForDollars() {
        assertEquals(new BetterProgrammerTask.Change(1, 0, 0, 0, 0), (new BetterProgrammerTask()).getCorrectChange(100));
        assertEquals(new BetterProgrammerTask.Change(4, 0, 0, 0, 0), (new BetterProgrammerTask()).getCorrectChange(400));
    }

    @Test
    public void shouldReturnRightChangeForCents() {
        assertEquals(new BetterProgrammerTask.Change(1, 0, 0, 0, 2), (new BetterProgrammerTask()).getCorrectChange(102));
        assertEquals(new BetterProgrammerTask.Change(4, 0, 0, 0, 3), (new BetterProgrammerTask()).getCorrectChange(403));
    }

    @Test
    public void shouldReturnRightChangeForExactNickels() {
        assertEquals(new BetterProgrammerTask.Change(1, 0, 0, 1, 0), (new BetterProgrammerTask()).getCorrectChange(105));
        assertEquals(new BetterProgrammerTask.Change(4, 0, 0, 1, 0), (new BetterProgrammerTask()).getCorrectChange(405));
    }

    @Test
    public void shouldReturnRightChangeForExactDimes() {
        assertEquals(new BetterProgrammerTask.Change(1, 0, 1, 0, 0), (new BetterProgrammerTask()).getCorrectChange(110));
        assertEquals(new BetterProgrammerTask.Change(4, 0, 2, 0, 0), (new BetterProgrammerTask()).getCorrectChange(420));
    }

    @Test
    public void shouldReturnRightChangeForExactQuarters() {
        assertEquals(new BetterProgrammerTask.Change(1, 1, 0, 0, 0), (new BetterProgrammerTask()).getCorrectChange(125));
        assertEquals(new BetterProgrammerTask.Change(4, 2, 0, 0, 0), (new BetterProgrammerTask()).getCorrectChange(450));
        assertEquals(new BetterProgrammerTask.Change(4, 3, 0, 0, 0), (new BetterProgrammerTask()).getCorrectChange(475));
    }

    @Test
    public void shouldCombineThem() {
        assertEquals(new BetterProgrammerTask.Change(1, 1, 0, 0, 1), (new BetterProgrammerTask()).getCorrectChange(126));
        assertEquals(new BetterProgrammerTask.Change(1, 0, 1, 1, 1), (new BetterProgrammerTask()).getCorrectChange(116));
        assertEquals(new BetterProgrammerTask.Change(4, 2, 0, 1, 0), (new BetterProgrammerTask()).getCorrectChange(455));
        assertEquals(new BetterProgrammerTask.Change(4, 3, 1, 0, 0), (new BetterProgrammerTask()).getCorrectChange(485));
        assertEquals(new BetterProgrammerTask.Change(4, 3, 2, 0, 1), (new BetterProgrammerTask()).getCorrectChange(496));
        assertEquals(new BetterProgrammerTask.Change(4, 3, 1, 1, 0), (new BetterProgrammerTask()).getCorrectChange(490));
    }


    private void assertEquals(BetterProgrammerTask.Change expected, BetterProgrammerTask.Change actual) {
        Assert.assertEquals(expected.getDollars(), actual.getDollars());
        Assert.assertEquals(expected.getQuarters(), actual.getQuarters());
        Assert.assertEquals(expected.getDimes(), actual.getDimes());
        Assert.assertEquals(expected.getNickels(), actual.getNickels());
        Assert.assertEquals(expected.getCents(), actual.getCents());
    }
}
