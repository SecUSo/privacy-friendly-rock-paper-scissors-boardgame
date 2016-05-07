package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import java.util.ArrayList;

/**
 * Created by David Giessing on 06.05.2016.
 * This class represents Scissor Figures. Scissor Figures loose against Rock Figures
 */
public class ScissorFigure extends Figure {

    public ScissorFigure(){
        super(new ArrayList<Class>(new ArrayList<Class>(){{add(RockFigure.class);}}));
    }
}
