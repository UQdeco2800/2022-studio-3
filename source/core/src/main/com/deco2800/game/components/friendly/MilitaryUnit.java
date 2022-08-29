package com.deco2800.game.components.friendly;

import com.deco2800.game.components.Component;
import java.lang.String;

public class MilitaryUnit extends Component {
        private int MilitaryUnit;
        private String type;
    
        public void MilitaryUnits(){
            this.MilitaryUnit = 1;
        }
    
        public void setEnabled(boolean enabled){
            this.enabled = enabled;
        }
        
        public void setMilitaryUnit(String type){
            this.type = type;
        }
    
        
        public int getMilitaryUnit(){
            return this.MilitaryUnit;
        }   
    
}
