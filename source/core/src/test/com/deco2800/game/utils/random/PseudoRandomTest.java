package com.deco2800.game.utils.random;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class PseudoRandomTest {
    @Test
    void correctRangePseudoRandomUnit() {
        double test = PseudoRandom.randomUnitNumberGenerator();
        assertTrue(test >= 0 && test <= 1);
    }

    @Test
    void correctRangePseudoRandomDouble() {
        double lowerBound = 1.234;
        double upperBound = 4.543;
        double test = PseudoRandom.seedRandomDouble(lowerBound, upperBound);
        assertTrue(test >= lowerBound && test <= upperBound);
    }

    @Test
    void correctRangePseudoRandomInt() {
        int lowerBound = 0;
        int upperBound = 4;
        double test = PseudoRandom.seedRandomInt(lowerBound, upperBound);
        assertTrue(test >= lowerBound && test <= upperBound);
    }

    @Test
    void correctRangePseudoRandomItem() {
        List<Integer> testList = new ArrayList<>();
        testList.add(1);
        testList.add(2);
        testList.add(3);
        testList.add(4);
        Integer test = PseudoRandom.getRandomItem(testList);
        assertTrue(testList.contains(test));
    }
}
