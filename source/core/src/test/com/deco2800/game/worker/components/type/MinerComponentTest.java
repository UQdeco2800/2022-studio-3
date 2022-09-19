package com.deco2800.game.worker.components.type;

import com.deco2800.game.components.worker.type.MinerComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinerComponentTest {
    @Test
    void getIsMiner() {
        MinerComponent minerComponent = new MinerComponent();

        assertEquals(1, minerComponent.getIsMiner());
    }
}
