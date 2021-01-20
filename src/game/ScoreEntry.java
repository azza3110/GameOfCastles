package game;

import java.io.PrintWriter;
import java.util.Date;

/**
 * Diese Klasse stellt einen Eintrag in der Bestenliste dar.
 * Sie enthält den Namen des Spielers, das Datum, die erreichte Punktzahl sowie den Spieltypen.
 */
public class ScoreEntry implements Comparable<ScoreEntry> {

    private String name;
    private Date date;
    private int score;
    private String gameType;

    /**
     * Erzeugt ein neues ScoreEntry-Objekt
     * @param name der Name des Spielers
     * @param score die erreichte Punktzahl
     * @param date das Datum
     * @param gameGoal der Spieltyp
     */
    private ScoreEntry(String name, int score, Date date, String gameGoal) {
        this.name = name;
        this.score = score;
        this.date = date;
        this.gameType = gameGoal;
    }

    /**
     * Erzeugt ein neues ScoreEntry-Objekt
     * @param player der Spieler
     * @param gameGoal der Spieltyp
     */
    public ScoreEntry(Player player, Goal gameGoal) {
        this.name = player.getName();
        this.score = player.getPoints();
        this.date = new Date();
        this.gameType = gameGoal.getName();
    }

    @Override
    public int compareTo(ScoreEntry scoreEntry) {
        return Integer.compare(score, scoreEntry.score);
    }

    /**
     * Schreibt den Eintrag als neue Zeile mit dem gegebenen {@link PrintWriter}
     * Der Eintrag sollte im richtigen Format gespeichert werden.
     * @see #read(String)
     * @see Date#getTime()
     * @param printWriter der PrintWriter, mit dem der Eintrag geschrieben wird
     */
    public void write(PrintWriter printWriter) {
    	 printWriter.print(name+";"+date.toString()+";"+String.valueOf(score)+";"+gameType);
    }

    /**
     * List eine gegebene Zeile ein und wandelt dies in ein ScoreEntry-Objekt um.
     * Ist das Format der Zeile ungültig oder enthält es ungültige Daten, wird null zurückgegeben.
     * Eine gültige Zeile enthält in der Reihenfolge durch Semikolon getrennt:
     *    den Namen, das Datum als Unix-Timestamp (in Millisekunden), die erreichte Punktzahl, den Spieltypen
     * Gültig wäre beispielsweise: "Florian;1546947397000;100;Eroberung"
     *
     *
     * @see String#split(String)
     * @see Long#parseLong(String)
     * @see Integer#parseInt(String)
     * @see Date#Date(long)
     *
     * @param line Die zu lesende Zeile
     * @return Ein ScoreEntry-Objekt oder null
     */
    public static ScoreEntry read(String line) {
    	 String[] arr = line.split(";");
         if(arr.length<4) { System.out.println("hooooon");return null;}
         String name = arr[0];
         String score = arr[1];
         String date= arr[2];
         String gameType=arr[3];
         if(!gameType.matches("[a-zA-Z]+")||!score.matches("[0-9]+")) {
             System.out.println("honnnnn");
        	 return null;
         }
         return new ScoreEntry(name,Integer.parseInt(score),new Date(Long.parseLong(date)),gameType); 
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public String getMode() {
        return this.gameType;
    }
}
