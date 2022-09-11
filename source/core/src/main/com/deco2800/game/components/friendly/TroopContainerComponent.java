package com.deco2800.game.components.friendly;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntMap;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.ComponentType;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


/**
 * Allows us to make use of an entity that contains other entities
 * <p>
 * i.e. an in-game 'unit'. Does so by binding them within a range of one
 * another and propagating updates to its sub-entities.
 * </p>
 *
 * <p>
 * This implementation does not assume that all Units are homogenous
 * </p>
 *
 * Also, this slightly acts as a disgusting entity/component blend, unsure if
 * it should just be an extension of the entity.
 */
public class TroopContainerComponent extends Component {
    private ArrayList<Entity> troops;

    /*
     * We need to essentially recreate the behaviour of an Entity within a
     * component, by adding components to the sub-entities and controlling those
     */
    //private final Entity template = new Entity();
    private boolean created = false; // if we double create, we don't want to
    // generate multiple error messages
    private final IntMap<Component> troopComponents = new IntMap<>(4);
    private float boundingRadius; // note: we need to use this to create
    private static final Logger logger =
            LoggerFactory.getLogger(TroopContainerComponent.class);


    public TroopContainerComponent (ArrayList<Entity> troops,
                                     float permissibleRange) {
        this.troops = troops;
        this.boundingRadius = permissibleRange;
    }

    /* TODO: see if this implementation can be reworked without copying the
     *       component. */
//    public Entity addTroopComponent(Component component) {
//        if (created) {
//            logger.error(
//                    "Adding {} to {} troops after creation is not supported " +
//                            "and will be ignored", component, this);
//            return template;
//        }
//        ComponentType componentType = ComponentType.getFrom(component.getClass());
//        if (troopComponents.containsKey(componentType.getId())) {
//            logger.error(
//                    "Attempted to add multiple components of class {} to {}. Only one component of a class "
//                            + "can be added to an entity, this will be ignored.",
//                    component,
//                    this);
//            return template;
//        }
//        troopComponents.put(componentType.getId(), component);
//        for (Entity troop:troops) {
//            troop.addComponent()
//        }
//        component.setEntity(this);
//
//        return template;
//    }


    /**
     * Called when the corresponding entity is created.
     *
     * Used to create all of the sub-entities, so that they do not need to be
     * individually created. Note that this fails if the sub-entities are
     * created on their own.
     */
    @Override
    public void create() {
        super.create();
        if (created) {
            logger.error(
                    "{} was created twice. Its sub-entities should only be " +
                            "registered with the entity service once.",
                    this);
        }
        ArrayList<Vector2> startPositions =
                getFormation(this.entity.getCenterPosition(),
                        this.boundingRadius, troops.size());
        // start in formation
        for (int i = 0; i < troops.size(); i++) {
            troops.get(i).setPosition(startPositions.get(i));
        }
        troops.forEach(Entity::create);
        created = true;
    }

    @Override
    public void earlyUpdate() {
        super.earlyUpdate();
        troops.forEach(Entity::earlyUpdate);
    }

    /**
     * updates the components of troops, and pulls them back in line if they
     * have managed to move too far from the center of the unit
     */
    @Override
    public void update() {
        // want to update the center of the bounding radius to be the mean
        // position of the troops
        Vector2 troopMean = troops.stream().map(Entity::getCenterPosition)
                .reduce(Vector2.Zero.cpy(), (a, b) -> a.mulAdd(b,
                        1.0f/troops.size()));
        // remember that position is set as the top left corner
        this.entity.setPosition(troopMean.sub(boundingRadius, boundingRadius));
        for (Entity troop: troops) {
            troop.update();
//            Vector2 troopCenterRelative =
//                    troop.getCenterPosition().sub(entity.getCenterPosition());
//            float centerDist =
//                    troop.getCenterPosition().dst(entity.getCenterPosition());
//            // we want to keep the edge of the troop within the bounds
//            // there are smarter ways to do this, but it's the simplest
//            // guarantee
//            float realRange = boundingRadius - Math.max(troop.getScale().x,
//                    troop.getScale().y);
//            // bound location to edge of permissible region
//            if ( centerDist > realRange) {
//                // scale vector from center by factor of realRange/currentDist,
//                // then move to top left of troop from that point
//                troop.setPosition(troopMean.mulAdd(troopCenterRelative,
//                        realRange/centerDist).mulAdd(troop.getScale(), -0.5f));
//            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Entity entity:troops) {
            entity.dispose();
        }
    }

    /**
     * Finds the formation of the troops around a point
     *
     * @param centrePos the position the unit is/will be centred on
     * @param n the number of troops to form in the unit
     * @return the positions of the resultant troops
     */
    public static ArrayList<Vector2> getFormation(Vector2 centrePos,
                                                  float boundingRadius,
                                                  int n) {
        ArrayList<Vector2> out = new ArrayList<>();
        //TODO: decide whether to log on failure
        if (n == 1) {
            out.add(centrePos);
        } else if (n > 1) {
            Vector2 cur =
                    Vector2.Y.cpy().scl(boundingRadius*7f/12f)
                             .rotateDeg(225).add(centrePos);
            for (int i = 0; i < n; i++) {
                out.add(cur.cpy());
                cur.rotateAroundDeg(centrePos, 360f/n);
            }
        }
        return out;
    }
}
