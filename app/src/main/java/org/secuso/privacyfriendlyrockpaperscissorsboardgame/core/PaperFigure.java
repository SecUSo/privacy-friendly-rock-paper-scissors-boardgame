package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import java.util.ArrayList;

/**
 * Created by David Giessing on 06.05.2016.
 * This class Represents Figures of the Type Paper. Paper Figures loose against Scissors.
 */
public class PaperFigure extends Figure {

    public PaperFigure(Player owner){
        super(new ArrayList<Class>(){{add(ScissorFigure.class);}},owner);
    }
}
