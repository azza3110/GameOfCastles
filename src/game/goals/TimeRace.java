package game.goals;

import java.util.Date;

import game.Game;
import game.Goal;
import game.Player;
import game.map.Castle;

public class TimeRace extends Goal {
	Date start;

	public TimeRace() {
		super("Time Race", "the Player who occupies the most Amount of castles in the next 2 minutes wins");
		start = new Date();
	}

	@Override
	public boolean isCompleted() {
		return this.getWinner() != null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Player getWinner() {
		Game game = this.getGame();
		Date end = new Date();
		if (end.getMinutes() > start.getMinutes() + 1) {
			if (end.getSeconds() >= start.getSeconds()) {
				return game.getPlayerWithMostCastles();
			}
		}
		if (game.getRound() < 2)
			return null;

		Player p = null;
		for (Castle c : game.getMap().getCastles()) {
			if (c.getOwner() == null)
				return null;
			else if (p == null)
				p = c.getOwner();
			else if (p != c.getOwner())
				return null;
		}

		return p;
	}

	@Override
	public boolean hasLost(Player player) {
		if (getGame().getRound() < 2)
			return false;

		return player.getNumRegions(getGame()) == 0;
	}
}
