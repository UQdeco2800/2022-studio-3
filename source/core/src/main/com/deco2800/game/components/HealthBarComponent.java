package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.building.TextureScaler;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Health Bar Component can be added to entities
 * This component will display the health under the entity
 * The health is specified by the combat stats component
 */
public class HealthBarComponent extends RenderComponent {

    private static final Logger logger = LoggerFactory.getLogger(HealthBarComponent.class);
    private Texture red;
    private Texture green;
    private Texture segment;

    private CombatStatsComponent stats;
    private boolean showing;
    private EntityType entityType; // Friendly units have green health bars. Enemies have red health bars
    private float zIndex;

    private long lastUpdate;
    private long visibleTime; // time(ms) that health bar is visible for after health is updated

    /**
     * Health bar will display health of entity in game. Buildings and Friendly entity types will display green health
     * and enemies will display red health
     * @param entityType Type of entity. e.g. Friendly, Enemy, Building
     */
    public HealthBarComponent(EntityType entityType) {
        logger.debug("Creating {} Health Bar", entityType);
        this.entityType = entityType;
        visibleTime = 15 * 1000L; // 15 seconds
    }

    @Override
    public void create() {
        super.create();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888 );
        pixmap.setColor(Color.GREEN);
        pixmap.fillRectangle(0, 0, 1,1);
        green = new Texture( pixmap );
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, 1,1);
        red = new Texture( pixmap );
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fillRectangle(0, 0, 1,1);
        segment = new Texture( pixmap );
        pixmap.dispose();

        stats = entity.getComponent(CombatStatsComponent.class);
        if (stats == null) {
            // Combat Stats component required for Health
            this.dispose();
        }
        lastUpdate = ServiceLocator.getTimeSource().getTime();
        showing = true;
        zIndex = 0f;

        entity.getEvents().addListener("updateHealth", this::updateHealth);
    }

    @Override
    public void dispose() {
        super.dispose();
        green.dispose();
        red.dispose();
        segment.dispose();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // Uncomment to allow hidden feature
        if (!showing) {
            return;
        }
        // friendly health is green enemy entity health is red
        Texture health = entityType == EntityType.FRIENDLY ? green : red;

        // Constants for health bar size
        float xdist = 0.5f; // x scaled distance for health bar from centre of entity (0.5 = whole entity
        float ydist = 0.3f; // y offset for health bar from centre of entity
        float height = 0.2f; // height of health bar
        float line_size = 0.06f; // segment line width
        float segment_health = 25f; // amount of health to display per segment

        Vector2 position = entity.getCenterPosition();
        Vector2 scale = entity.getScale();
        // Health bar position and width
        float xPos = position.x - (scale.x * xdist);
        float yPos = position.y - (scale.y * ydist);
        float width = 2 * scale.x * xdist; // width of entire health bar
        float healthWidth = width * (float) stats.getHealth() / stats.getMaxHealth(); // width of green health

        // Drawing health
        batch.draw(health, xPos, yPos, healthWidth, height);
        batch.draw(segment, xPos, yPos, width, line_size);  // bottom border
        batch.draw(segment, xPos, yPos + height, width, line_size); // top border
        // Drawing segments
        for (float x = xPos - line_size; x < xPos + width; x += width * (segment_health/ stats.getMaxHealth())) {
            batch.draw(segment, x, yPos, line_size, height + line_size);
        }

        zIndex = -yPos; // updates the zIndex to display above entity
    }

    @Override
    public void update() {
        if (entity.getComponent(SelectableComponent.class) != null && entity.getComponent(SelectableComponent.class).isSelected()) {
            showing = true;
            return;
        }
        if (ServiceLocator.getRenderService().getDebug().getActive()) {
            showing = true; // debug command sets health bar to be visible
        } else if (ServiceLocator.getTimeSource().getTimeSince(lastUpdate) > visibleTime) {
            showing = false; // hides health bar after time of inactivity
        }
    }

    /**
     * Event trigger by event listener. When the health is updated it causes the health bar to display if it is hidden
     * Also restarts the timer that automatically hides health bar
     * @param newHealth new health
     */
    public void updateHealth(int newHealth) {
        lastUpdate = ServiceLocator.getTimeSource().getTime();
        showing = true;
    }

    /**
     * Hides health bar
     */
    public void hideHealthBar() {
        lastUpdate = ServiceLocator.getTimeSource().getTime() - visibleTime;
    }

    /**
     * Makes health bar visible for an amount of time
     * @param time time in milliseconds
     */
    public void showHealthBar(long time) {
        lastUpdate = ServiceLocator.getTimeSource().getTime() - visibleTime + time;
    }

    /**
     * Makes health bar visible for default amount of time
     */
    public void showHealthBar() {
        showHealthBar(visibleTime);
    }

    public boolean getVisible() {
        return showing;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public float getZIndex() {
        return zIndex;
    }

}
