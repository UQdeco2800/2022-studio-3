package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
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
    private final long VISIBLE_TIME = 15 * 1000; // time(ms) that health bar is visible for after health is updated

    /**
     * Health bar will display health of entity in game. Buildings and Friendly entity types will display green health
     * and enemies will display red health
     * @param entityType Type of entity. e.g. Friendly, Enemy, Building
     */
    public HealthBarComponent(EntityType entityType) {
        this.entityType = entityType;
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
        lastUpdate = TimeUtils.millis();
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
        if (!showing) {
            return;
        }
        Texture health = green;
        if (entityType == EntityType.ENEMY) {
            health = red; // enemy entity health is red
        }

        // Constants for health bar size
        float xdist = 0.4f; // x scaled distance for health bar from centre of entity (0.5 = whole entity
        float ydist = 0.3f; // y offset for health bar from centre of entity
        float height = 0.15f; // height of health bar
        float line_size = 0.03f; // segment line width
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
        if (ServiceLocator.getRenderService().getDebug().getActive()) {
            showing = true; // debug command sets health bar to be visible
        } else if (TimeUtils.timeSinceMillis(lastUpdate) > VISIBLE_TIME) {
            showing = false; // hides health bar after time of inactivity
        }
    }

    /**
     * Event trigger by event listener. When the health is updated it causes the health bar to display if it is hidden
     * Also restarts the timer that automatically hides health bar
     * @param newHealth
     */
    public void updateHealth(int newHealth) {
        lastUpdate = TimeUtils.millis();
        showing = true;
    }

    @Override
    public float getZIndex() {
        return zIndex;
    }

}
