package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import java.util.ArrayList;

/**
 * Created by David Giessing on 06.05.2016.
 * This class represents rock figures. Rock figures loose against Paper Figures
 */
public class RockFigure extends Figure {

    public RockFigure(Player owner){
        super(new ArrayList<Class>(){{add(PaperFigure.class);}},owner);
    }
}
