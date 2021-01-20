package game.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Diese Klasse teilt Burgen in Königreiche auf
 */
public class Clustering {

	private Random random;
	private final List<Castle> allCastles;
	private final int kingdomCount;

	/**
	 * Ein neues Clustering-Objekt erzeugen.
	 * 
	 * @param castles      Die Liste von Burgen, die aufgeteilt werden sollen
	 * @param kingdomCount Die Anzahl von Königreichen die generiert werden sollen
	 */
	public Clustering(List<Castle> castles, int kingdomCount) {
		if (kingdomCount < 2)
			throw new IllegalArgumentException("Ungültige Anzahl an Königreichen");

		this.random = new Random();
		this.kingdomCount = kingdomCount;
		this.allCastles = Collections.unmodifiableList(castles);
	}

	/**
	 * Gibt eine Liste von Königreichen zurück. Jedes Königreich sollte dabei einen
	 * Index im Bereich 0-5 bekommen, damit die Burg richtig angezeigt werden kann.
	 * Siehe auch {@link Kingdom#getType()}
	 */
	public List<Kingdom> getPointsClusters() {

		Point[] centers = new Point[kingdomCount];
		List<Kingdom> kingdoms = new ArrayList<Kingdom>();

		for (int i = 0; i < kingdomCount; i++) {
			centers[i] = new Point(getRandomWidth(),getRandomHeight());
			kingdoms.add(new Kingdom(i));
		}

		return makeBetterKingdoms(kingdoms, centers);
	}


	/**
	 * this methods puts every castle in its right kingdom and keeps doing it tell
	 * it gets the best center
	 * 
	 * @param kingdoms
	 * @param centers
	 * @return an arraylist that contains the kingdoms
	 */
	private List<Kingdom> makeBetterKingdoms(List<Kingdom> kingdoms, Point[] centers) {
		Point[] backup = centers;
		for (Castle ca : allCastles) {
			kingdoms.get(castleToKingdom(ca, centers)).addCastle(ca);
		}
		for (Kingdom kingdom : kingdoms) {
			centers[kingdoms.indexOf(kingdom)] = new Point(mittelX(kingdom.getCastles()),
					mittelY(kingdom.getCastles()));
		}
		if (!Arrays.equals(centers, backup)) {
			return makeBetterKingdoms(clearKingdoms(kingdoms), centers);
		} else
			return kingdoms;
	}

	/**
	 * looks for the given castle the closest Center
	 * 
	 * @param castle
	 * @param centers
	 * @return the index number of the center in the given array centers
	 */
	private int castleToKingdom(Castle castle, Point[] centers) {
		int kingdom = 0;
		double tmp = euklid(castle, centers[0]);
		for (int i = 1; i < centers.length; i++) {
			double compare = euklid(castle, centers[i]);
			if (compare < tmp) {
				tmp = compare;
				kingdom = i;
			}
		}
		return kingdom;
	}

	/**
	 * deletes all the castles from every kingdom in the given parameter
	 * 
	 * @param kingdoms
	 * @return kingdoms with no castles
	 */
	private List<Kingdom> clearKingdoms(List<Kingdom> kingdoms) {
		for (Kingdom k : kingdoms)
			k.removeCastles();
		return kingdoms;
	}

	/**
	 * calculates the average Y for the new Center
	 * 
	 * @param castles
	 * @return the new Y
	 */
	private int mittelY(List<Castle> castles) {
		double d = 0;
		if (castles.size()==0) {
			return 0;
		}
		else {
			for (Castle c : castles) {
				d = d + c.getLocationOnMap().getY();
			}
			return (int) d / castles.size();
		}
	}

	/**
	 * calculates the average X for the new Center
	 * 
	 * @param castles
	 * @return the new X
	 */

	private int mittelX(List<Castle> castles) {
		if (castles.size()==0) {
			return 0;
		}
		else {
			double d = 0;
			for (Castle c : castles) {
				d = d + c.getLocationOnMap().getX();
			}
			return (int) d / castles.size();
		}
	}

	private int getRandomHeight() {
		double smallestY = allCastles.get(0).getLocationOnMap().getY();
		double bigestY = smallestY;
		for (Castle ca : allCastles) {
			if (ca.getLocationOnMap().getY() > bigestY) {
				bigestY = ca.getLocationOnMap().getY();
			}else if (ca.getLocationOnMap().getY()<smallestY) {
				smallestY = ca.getLocationOnMap().getY();
			}
		}
		return random.nextInt((int)(bigestY - smallestY))+(int)smallestY;
	}

	private int getRandomWidth() {
		double smallestX = allCastles.get(0).getLocationOnMap().getX();
		double bigestX = smallestX;
		for (Castle ca : allCastles) {
			if (ca.getLocationOnMap().getX() > bigestX) {
				bigestX = ca.getLocationOnMap().getX();
			}else if (ca.getLocationOnMap().getX()<smallestX) {
				smallestX = ca.getLocationOnMap().getX();
			}
		}
		return random.nextInt((int)(bigestX - smallestX))+(int)smallestX;

	}

	/**
	 * calculates the distance between the location of ca and the given point
	 * 
	 * @param ca
	 * @param point
	 * @return the distance
	 */
	private double euklid(Castle ca, Point point) {
		return Math.sqrt(
				pow(ca.getLocationOnMap().getX() - point.getX()) + pow(ca.getLocationOnMap().getY() - point.getY()));
	}

	/**
	 * @param d
	 * @return d to the power of 2
	 */
	private double pow(double d) {
		return d * d;
	}
}