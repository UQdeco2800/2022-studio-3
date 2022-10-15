// package com.deco2800.game.components.building;

// import com.badlogic.gdx.graphics.g2d.SpriteBatch;
// import com.badlogic.gdx.scenes.scene2d.Actor;
// import com.badlogic.gdx.scenes.scene2d.ui.Label;
// import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
// import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
// import com.deco2800.game.components.friendlyunits.SelectableComponent;
// import com.deco2800.game.ui.UIComponent;

// public class ShopUIDisplayComponent extends UIComponent {
//     private boolean selected;
//     private BuildingActions ba;

//     public ShopUIDisplayComponent() {
//         selected = false;
//     }

//     @Override
//     public float getZIndex() {
//         return 2f;
//     }

//     @Override
//     public void create() {
//         super.create();

//         selected = entity.getComponent(SelectableComponent.class).isSelected();
//         ba = entity.getComponent(BuildingActions.class);
//     }

//     @Override
//     public void draw(SpriteBatch batch) {
//         // create the shop interface
//         if (this.selected) {
//             switch (ba.getType()) {
//                 case LIBRARY: {
//                     Label shopLabel = new Label("Shop", skin);
//                     TextButton upgrade = new TextButton("Unit\nupgrade", skin);
//                     upgrade.addListener(
//                         new ChangeListener() {
//                             @Override
//                             public void changed(ChangeEvent changeEvent, Actor actor) {

//                                 // logger.debug("Skip button clicked");
//                                 entity.getEvents().trigger("unit upgrade");
//                             }
//                         }
//                     );
//                     shopLabel.setPosition(370f, 160f);
//                     upgrade.setPosition(360f, 107f);

//                     TextButton levelUp = new TextButton("Level Up", skin);
//                     levelUp.addListener(
//                         new ChangeListener() {
//                             @Override
//                             public void changed(ChangeEvent changeEvent, Actor actor) {

//                                 // logger.debug("Skip button clicked");
//                                 entity.getEvents().trigger("levelUp");
//                             }
//                         }
//                     );
//                     levelUp.setPosition(360f, 60f);
//                     levelUp.getLabel().setFontScale(0.9f);
//                     levelUp.setTransform(true);
//                     levelUp.setScaleX(0.8f);
//                     contextBoxItems.addActor(shopLabel);
//                     contextBoxItems.addActor(upgrade);
//                     contextBoxItems.addActor(levelUp);
//                     break;

//                 } case BLACKSMITH: {

//                     Label shopLabel = new Label("Shop", skin);
//                     TextButton upgrade = new TextButton("Wall\nupgrade", skin);
//                     upgrade.addListener(
//                         new ChangeListener() {
//                             @Override
//                             public void changed(ChangeEvent changeEvent, Actor actor) {

//                                 // logger.debug("Skip button clicked");
//                                 entity.getEvents().trigger("wall upgrade");
//                             }
//                         }
//                     );
//                     shopLabel.setPosition(370f, 160f);
//                     upgrade.setPosition(360f, 107f);
//                     TextButton levelUp = new TextButton("Level Up", skin);
//                     levelUp.addListener(
//                         new ChangeListener() {
//                             @Override
//                             public void changed(ChangeEvent changeEvent, Actor actor) {

//                                 // logger.debug("Skip button clicked");
//                                 entity.getEvents().trigger("levelUp");
//                             }
//                         }
//                     );
//                     levelUp.setPosition(360f, 60f);
//                     levelUp.getLabel().setFontScale(0.9f);
//                     levelUp.setTransform(true);
//                     levelUp.setScaleX(0.8f);
//                     contextBoxItems.addActor(shopLabel);
//                     contextBoxItems.addActor(upgrade);
//                     contextBoxItems.addActor(levelUp);
//                     break;

//                 } case BARRACKS:

//                     Label shopLabel = new Label("Shop", skin);
//                     TextButton upgrade = new TextButton("Spawn\nunit M10", skin);
//                     upgrade.addListener(
//                         new ChangeListener() {
//                             @Override
//                             public void changed(ChangeEvent changeEvent, Actor actor) {

//                                 // logger.debug("Skip button clicked");
//                                 entity.getEvents().trigger("spawn unit");
//                             }
//                         }
//                     );
//                     shopLabel.setPosition(370f, 160f);
//                     upgrade.setPosition(360f, 107f);
//                     TextButton levelUp = new TextButton("Level Up W10", skin);
//                     levelUp.addListener(
//                         new ChangeListener() {
//                             @Override
//                             public void changed(ChangeEvent changeEvent, Actor actor) {

//                                 // logger.debug("Skip button clicked");
//                                 entity.getEvents().trigger("levelUp");
//                             }
//                         }
//                     );
//                     levelUp.setPosition(360f, 60f);
//                     levelUp.getLabel().setFontScale(0.9f);
//                     levelUp.setTransform(true);
//                     levelUp.setScaleX(0.8f);
//                     contextBoxItems.addActor(shopLabel);
//                     contextBoxItems.addActor(upgrade);
//                     contextBoxItems.addActor(levelUp);
//                     break;
                    
//                 default:
//             }
//         }
//     }
// }
