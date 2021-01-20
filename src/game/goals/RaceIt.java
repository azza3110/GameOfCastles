package game.goals;

import game.Game;
import game.Goal;
import game.Player;
import game.map.Castle;
import game.map.MapSize;

public class RaceIt extends Goal {

	public RaceIt() {
		super("Need For Speed", "the first player who reaches 1,000/2,000/5,000 Points wins");
	}

	@Override
    public boolean isCompleted() {
        return this.getWinner() != null;
    }

    @Override
    public Player getWinner() {
        Game game = this.getGame();
        if(game.getRound() < 2)
            return null;

        Player p = null;
        for(Player pl :this.getGame().getPlayers()) {
        	if(this.getGame().getMapSize() == MapSize.LARGE)
        	if (pl.getPoints()>4999)
        		return pl;
        	if(this.getGame().getMapSize() == MapSize.MEDIUM)
            	if (pl.getPoints()>1999)
            		return pl;
        	if(this.getGame().getMapSize() == MapSize.SMALL)
            	if (pl.getPoints()>999)
            		return pl;
        }
        for(Castle c : game.getMap().getCastles()) {
            if(c.getOwner() == null)
                return null;
            else if(p == null)
                p = c.getOwner();
            else if(p != c.getOwner())
                return null;
        }

        return p;
    }

    @Override
    public boolean hasLost(Player player) {
        if(getGame().getRound() < 2)
            return false;

        return player.getNumRegions(getGame()) == 0;
    }

}
