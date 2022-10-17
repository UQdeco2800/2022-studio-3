package com.deco2800.game.soldiers.type;

import com.deco2800.game.components.Component;

public class ArcherComponent extends Component {

    private int isArcher;

    public ArcherComponent(){
        this.isArcher = 1;
    }

    public int getIsArcher(){
        return this.isArcher;
    }
}
