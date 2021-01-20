package game.goals;

import java.util.Date;

import game.Game;
import game.Goal;
import game.Player;
import game.map.Castle;
import game.map.MapSize;

public class AllinOne extends Goal {
	Date start;

	public AllinOne() {
		super("King Of The Hill",
				"Take your Decision how to win the Fight:\n 1- Write The History : Eliminate Your Enemy\n 2- Win With Honor : Achieve 2000 Points\n 3-Win With Haste : Occupy the heighst Number of Castles in the next 2 Minutes");
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
		for (Player pl : this.getGame().getPlayers()) {
			if (this.getGame().getMapSize() == MapSize.LARGE)
				if (pl.getPoints() > 4999)
					return pl;
			if (this.getGame().getMapSize() == MapSize.MEDIUM)
				if (pl.getPoints() > 1999)
					return pl;
			if (this.getGame().getMapSize() == MapSize.SMALL)
				if (pl.getPoints() > 999)
					return pl;
		}
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
