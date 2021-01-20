package game.players;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import base.Edge;
import base.Graph;
import base.Node;
import game.AI;
import game.Game;
import game.map.Castle;
import game.map.Kingdom;
import gui.AttackThread;

public class AntiBasicAI extends AI {

	Kingdom toOccupy;

	public AntiBasicAI(String name, Color color) {
		super(name, color);
		toOccupy = null;
		giveAJoker();
	}

	private Castle getCastleWithFewestTroops(List<Castle> castles) {
		Castle fewestTroops = castles.get(0);
		for (Castle castle : castles) {
			if (castle.getTroopCount() < fewestTroops.getTroopCount()) {
				fewestTroops = castle;
			}
		}

		return fewestTroops;
	}

	@Override
	protected void actions(Game game) throws InterruptedException {
		if (game.getRound() == 1) {

			List<Kingdom> emptyKingdoms = game.getMap().getKingdoms().stream().filter(k -> k.isEmpty())
					.collect(Collectors.toList());
			if (emptyKingdoms.isEmpty()) {
				getFreeNearbyCastles(game);
			} else

			if (toOccupy == null) {
				toOccupy = emptyKingdoms.remove(0);

			}
			List<Castle> li = new ArrayList<Castle>(toOccupy.getCastles());

			while (getRemainingTroops() > 0) {
				if (toOccupy.isFull()) {
					if (emptyKingdoms.isEmpty()) {
						getFreeNearbyCastles(game);
						break;
					} else {
						toOccupy = emptyKingdoms.get(0);
						li = new ArrayList<Castle>(toOccupy.getCastles());
					}
				}
				Castle cas = li.remove(0);
				if (cas.getOwner() == null) {
					sleep(1000);
					game.chooseCastle(cas, this);

				}
			}
		} else {

			// 1. Distribute remaining troops
			Graph<Castle> graph = game.getMap().getGraph();
			List<Castle> castleNearEnemy = new ArrayList<>();
			for (Castle castle : this.getCastles(game)) {
				Node<Castle> node = graph.getNode(castle);
				for (Edge<Castle> edge : graph.getEdges(node)) {
					Castle otherCastle = edge.getOtherNode(node).getValue();
					if (otherCastle.getOwner() != this && !castleNearEnemy.contains(castle)) {
						castleNearEnemy.add(castle);
						break;
					}
				}
			}

			while (this.getRemainingTroops() > 0) {
				Castle fewestTroops = getCastleWithFewestTroops(castleNearEnemy);
				sleep(500);
				game.addTroops(this, fewestTroops, 1);
			}

			boolean attackWon;
			Castle defenderCastle = null;
			Castle attackerCastle = null;
			do {
				// 2. Move troops from inside to border
				for (Castle castle : this.getCastles(game)) {
					if (!castleNearEnemy.contains(castle) && castle.getTroopCount() > 1) {
						Castle fewestTroops = getCastleWithFewestTroops(castleNearEnemy);
						game.moveTroops(castle, fewestTroops, castle.getTroopCount() - 1);
					}
				}

				// 3. attack!
				attackWon = false;
				for (Castle castle : castleNearEnemy) {
					if (castle.getTroopCount() < 2)
						continue;

					Node<Castle> node = graph.getNode(castle);
					for (Edge<Castle> edge : graph.getEdges(node)) {
						Castle otherCastle = edge.getOtherNode(node).getValue();
						if (otherCastle.getOwner() != this && castle.getTroopCount() >= otherCastle.getTroopCount()) {
							AttackThread attackThread = game.startAttack(castle, otherCastle, castle.getTroopCount());
							if (fastForward)
								attackThread.fastForward();
							defenderCastle = attackThread.getDefenderCastle();
							attackerCastle = attackThread.getAttackerCastle();
							attackThread.join();
							attackWon = attackThread.getWinner() == this;
							//if (attackThread.getDefenderCastle().getOwner() == attackThread.getAttackerCastle()
								//	.getOwner() && attackThread.getAttackerCastle().getTroopCount()>=10) {
								//game.moveTroops(attackerCastle, defenderCastle, 7);
							//};
							break;
						}
					}
					if (attackWon)
						break;
				}
			} while (attackWon);
			if(defenderCastle != null && attackerCastle != null)
			if (defenderCastle.getOwner() == attackerCastle.getOwner() && attackerCastle.getTroopCount()>=8 ) {
				game.moveTroops(attackerCastle, defenderCastle, 7);}
		}
	}

	private void getFreeNearbyCastles(Game game) throws InterruptedException {
		Graph<Castle> graph = game.getMap().getGraph();
		List<Castle> castlesNearMe = new ArrayList<>();
		for (Castle castle : this.getCastles(game)) {
			for (Edge<Castle> edge : graph.getEdges(graph.getNode(castle))) {
				Castle otherCastle = edge.getOtherNode(graph.getNode(castle)).getValue();
				if (otherCastle.getOwner() == null && !castlesNearMe.contains(otherCastle)) {
					castlesNearMe.add(otherCastle);
				}
			}
		}
		while (getRemainingTroops() > 0 && !castlesNearMe.isEmpty()) {
			sleep(1000);
			game.chooseCastle(castlesNearMe.remove(0), this);
		}
		if (castlesNearMe.isEmpty()) {
			List<Castle> availableCastles = game.getMap().getCastles().stream().filter(c -> c.getOwner() == null)
					.collect(Collectors.toList());
			while (availableCastles.size() > 0 && getRemainingTroops() > 0) {
				sleep(1000);
				game.chooseCastle(availableCastles.remove(0), this);
			}
		}
	}
}