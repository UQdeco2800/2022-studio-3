package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.game.rendering.RenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        float xdist = 0.35f; // x scaled distance for health bar from centre of entity (0.5 = whole entity
        float ydist = 0.3f; // y offset for health bar from centre of entity
        float height = 0.12f; // height of health bar
        float line_size = 0.02f; // segment line width

        Vector2 position = entity.getCenterPosition();
        Vector2 scale = entity.getScale();
        // Health bar position and width
        float xpos = position.x - (scale.x * xdist);
        float ypos = position.y - (scale.y * ydist);
        float width = 2 * scale.x * xdist; // width of entire health bar
        float healthwidth = width * (float) stats.getHealth() / stats.getMaxHealth(); // width of green health
        // amount of health to display per segment
        float segment_health = 25f;

        // Drawing
//        batch.draw(red, xpos, ypos, width, height);
        batch.draw(health, xpos, ypos, healthwidth, height);
        batch.draw(segment, xpos, ypos, width, line_size);  // bottom border
        batch.draw(segment, xpos, ypos + height, width, line_size); // top border
        // Segments
        for (float x = xpos - line_size; x < xpos + width; x += width * (segment_health/ stats.getMaxHealth())) {
            batch.draw(segment, x, ypos, line_size, height + line_size);
        }

        zIndex = -ypos;
    }

    @Override
    public void update() {
        if (TimeUtils.timeSinceMillis(lastUpdate) > VISIBLE_TIME) {
            showing = false;
        }
    }

    public void updateHealth(int newHealth) {
        lastUpdate = TimeUtils.millis();
        showing = true;
    }

    @Override
    public float getZIndex() {
        return zIndex;
    }

}
