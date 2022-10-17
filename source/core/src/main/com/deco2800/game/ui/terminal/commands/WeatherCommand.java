package com.deco2800.game.ui.terminal.commands;

import java.util.ArrayList;

public class WeatherCommand implements Command {

    @Override
    public boolean action(ArrayList<String> args) {
        if (args.size() == 0) {
            return false;
        }

        switch(args.get(0)) {
            case "pause":

        }
        return true;
    }
}
