package com.deco2800.game.soldiers.type;

import com.deco2800.game.components.Component;

public class SwordsmanComponent extends Component {

    private int isSwordsman;

    public SwordsmanComponent(){
        this.isSwordsman = 1;
    }

    public int getIsSwordsman(){
        return this.isSwordsman;
    }
}
