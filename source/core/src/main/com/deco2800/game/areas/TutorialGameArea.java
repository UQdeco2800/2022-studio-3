package com.deco2800.game.areas;

import com.deco2800.game.components.maingame.DialogueBoxActions;
import com.deco2800.game.components.maingame.DialogueBoxDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

public class TutorialGameArea extends GameArea {

    /** dialogue box */
    private DialogueBoxDisplay dialogueBoxDisplay;

    /** textures needed to load */
    private String[] tutorialTextures = {
            "images/dialogue_box_pattern2_background.png",
            "images/dialogue_box_image_default.png",
            "images/exit-button.PNG",
            "images/dialogue_box_background_Deep_Sea.png"
    };

    /**
     * On init load needed textures
     */
    private void loadAssets() {

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(this.tutorialTextures);
        resourceService.loadAll();
    }

    /**
     * On exit clean-up loaded textures
     */
    private void unloadAssets() {

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(this.tutorialTextures);
    }

    /**
     * Create and spawn entities to this area
     */
    private void displayUI() {

        Entity dialogueBox = new Entity();
        this.dialogueBoxDisplay = new DialogueBoxDisplay();
        dialogueBox.addComponent(this.dialogueBoxDisplay)
                .addComponent(new DialogueBoxActions(this.dialogueBoxDisplay));

        spawnEntity(dialogueBox);
    }

    @Override
    public void create() {

        this.loadAssets();
        this.displayUI();
    }

    @Override
    public void dispose() {

        super.dispose();
        this.unloadAssets();
    }
}
