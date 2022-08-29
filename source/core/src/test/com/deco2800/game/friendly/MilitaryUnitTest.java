package com.deco2800.game.friendly;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import com.deco2800.game.components.friendly.MilitaryUnit;

public class MilitaryUnitTest {

    @Test
    public void testGetter(){
        MilitaryUnit militaryUnit= new MilitaryUnit();
        assertEquals(1, militaryUnit.getMilitaryUnit());
    }
}
