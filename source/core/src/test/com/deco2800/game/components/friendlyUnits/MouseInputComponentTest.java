package com.deco2800.game.components.friendlyUnits;

import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class MouseInputComponentTest {

    @Test
    void notSelectedWhenCreated() {
        SelectableComponent selectableComponent = new SelectableComponent();
        assertFalse(selectableComponent.isSelected());
    }



}
