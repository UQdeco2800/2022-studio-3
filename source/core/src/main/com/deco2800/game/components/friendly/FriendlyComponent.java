package com.deco2800.game.components.friendly;
import com.deco2800.game.components.Component;

/**
 * Component to indicate that this Entity is friendly - used by the gate to
 * distinguish friendly entities.
 */
public class FriendlyComponent extends Component {
    public static int globalFriendlyId = 0;
    public int friendlyId = globalFriendlyId++;

    /**
     * Returns unique identifier ID for this friendly unit
     * @return id number identifying a friendly unit
     */
    public int getId() {
        return friendlyId;
    }
}
