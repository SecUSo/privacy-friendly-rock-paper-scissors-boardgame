package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

/**
 * Figure containing all relevant data like visibilty, ownership and type
 */
public class RPSGameFigure {

    RPSFigure type;
    boolean hidden;
    IPlayer owner;

    public RPSGameFigure(IPlayer owner, RPSFigure type) {
        this.owner = owner;
        this.type = type;
        this.hidden = true;
    }

    public boolean isHidden() {
        return hidden;
    }

    public RPSFigure discover() {
        this.hidden = false;
        return this.type;
    }

    public RPSFigure getType() {
        return this.type;
    }

    public IPlayer getOwner() {
        return owner;
    }

    public String toString() {
        String result="";
        result+=owner.getId();
        result += " " + type.toString();
        result += hidden ? " hidden": " open";
        return result;
    }
}