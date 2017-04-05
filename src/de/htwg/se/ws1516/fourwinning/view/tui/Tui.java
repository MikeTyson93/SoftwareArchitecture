package de.htwg.se.ws1516.fourwinning.view.tui;

import de.htwg.se.ws1516.fourwinning.controller.impl.*;
import de.htwg.se.ws1516.fourwinning.controller.*;
import de.htwg.util.observer.IObserver;
import de.htwg.util.observer.Event;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import de.htwg.se.ws1516.fourwinning.models.*;

public class Tui implements IObserver {

	IGameController spiel;
	private static final String NEWLINE = System.getProperty("line.separator");
	private static final Logger LOGGER = Logger.getLogger(Tui.class.getName());
	private static Scanner eingabe;
	Player eins;
	Player zwei;
	Player aktiv = eins;
	Feld[][] spielfeld;
	int rows = 0;
	int columns = 0;
	String zugerfolgreich;
	String next = "next round";
	String regex = "[0-9]+";
	
	@Inject
	public Tui(IGameController spiel) {
		this.spiel = spiel;
		spiel.addObserver(this);
	}

	public String ausgabe(Feld[][] feld, int rows, int columns, Player eins, Player zwei) {
		StringBuilder sb = new StringBuilder();
		LOGGER.setLevel(Level.FINER);
		LOGGER.info("Ausgabe");
		sb.append(NEWLINE);
		for (int k = 0; k < rows; k++) {
			for (int l = 0; l < columns; l++) {
				if (feld[k][l].getSet()) {
					if (feld[k][l].getOwner().getName().equals(eins.getName())) {
						sb.append("[X]\t");
						
					} else if (feld[k][l].getOwner().getName().equals(zwei.getName())) {
						sb.append("[O]\t");
					
					}
				} else {
					sb.append("[ ]\t");
				
				}
			}
			sb.append(NEWLINE);
		}
		String ausgabe = sb.toString();
		LOGGER.info(ausgabe);
		return ausgabe;
	}

	public void createGameArea() {
		LOGGER.info("Rows werden von GUI übernommen");
		rows = spiel.getRows();
		LOGGER.info("Columns werden von GUI übernommen");
		columns = spiel.getColumns();
		LOGGER.info(spiel.getStatusText());
	}

	public void createPlayers() {
		this.eins = spiel.getPlayerOne();
		this.zwei = spiel.getPlayerTwo();
		this.aktiv = spiel.aktiverSpieler();
	}

	public String playGame(String command) {
		spielfeld = spiel.update();
		eingabe = new Scanner(System.in);
		aktiv = spiel.aktiverSpieler();
		LOGGER.info(spiel.getStatusText());

		String rowExplain = String.format("%nMachen sie Ihren Zug, geben sie dafuer die Column an, zwischen 0 und %d%n",
				columns - 1);
		LOGGER.info(rowExplain);

		//LOGGER.info("Um den letzten Zug zu widerholen, geben sie redo ein");
		LOGGER.info("Um einen alten Spielstand zu laden, geben Sie 'load #Savegame' ein");
		LOGGER.info("Um ein Spiel zu speichern, geben sie 'save #savegame' ein");
		LOGGER.info("Um alle Spielstände anzuschauen, geben sie 'show' ein");
		LOGGER.info("Um einen Spielstand zu löschen, geben Sie 'delete #savegame' ein");
		String currentColumnString;
		if (command == ""){
			currentColumnString = eingabe.nextLine();
		} else{
			currentColumnString = command;
		}
		if ("redo".equals(currentColumnString)) {
			spiel.redo();
			spielerwaechsel(eins, zwei);
		} else if (currentColumnString.startsWith("load")){
			// Load savegame
			LOGGER.info("Nun müssen sie noch den Namen des Spielstandes eingeben, welchen Sie laden wollen.");
			String loadname = eingabe.nextLine();

			boolean success = spiel.loadFromDB(loadname);
			if (!success){
				LOGGER.info("Sie haben einen ungültigen Spielnamen angegeben!");
				return "load";
			}
			this.eins = spiel.getPlayerOne();
			this.zwei = spiel.getPlayerTwo();
			this.aktiv = spiel.aktiverSpieler();
			return "load";
		} else if (currentColumnString.startsWith("save")){
			// save game
			LOGGER.info("Nun müssen sie noch den Namen des Spielstandes eingeben, welchen Sie speichern wollen.");
			String savegame = eingabe.nextLine();
			if (!spiel.saveToDB(savegame)){
				LOGGER.info("Sie haben einen schon belegten Namen angegeben. Wollen Sie den Spielstand überschreiben? y/n");
				String yesNo = eingabe.nextLine();
				while (yesNo != "y" && yesNo != "n"){
					LOGGER.info("Falsche Eingabe. Spielstand überschreiben? y/n");
					yesNo = eingabe.nextLine();
				}
				if (yesNo == "y"){
					spiel.updateToDB(savegame);
				} else {
					LOGGER.info("Vorgang abgebrochen.");
					return "save";
				}
			}
			
			return "save";
		} else if (currentColumnString.startsWith("show")){
			LOGGER.info(spiel.getAllGridsFromDB());
			return "show";
		} else if (currentColumnString.startsWith("delete")){
			LOGGER.info("Nun müssen sie noch den Namen des Spielstandes eingeben, welchen Sie löschen wollen.");
			String deleteGame = eingabe.nextLine();
			if (!spiel.deleteFromDB(deleteGame)){
				LOGGER.info("Spielstand konnte nicht gelöscht werden.");
				return "delete";
			}
			LOGGER.info("Spielstand gelöscht.");
			return "delete";
		} else if (currentColumnString.matches(regex)){
			spielfeld = spiel.update();
			int currentColumn = Integer.parseInt(currentColumnString);
			zugerfolgreich = (spiel.zug(currentColumn, aktiv));
			LOGGER.info(zugerfolgreich);
		} else {
			LOGGER.warning("Ungültige Eingabe!");
			return "show";
		}
		
		spiel.notifyObservers();
		String whoHasWon = "";
		if (spiel.spielGewonnen(spielfeld, aktiv)) {
			
			return "";
		}
		//LOGGER.info(spiel.getStatusText());
		LOGGER.info(whoHasWon);
		
		if (spiel.spielDraw(spielfeld)) {
			
			return "";
		}
		//LOGGER.info(spiel.getStatusText());
		//LOGGER.info("");
		//LOGGER.info("%n%n Schreibe undo, um den Zug rueckgaengig zu machen, ansonsten beliebige taste %n%n");
		/*if(command == ""){
			String undo = eingabe.next();
			if ("undo".equals(undo)) {
				spiel.undo();
				spielfeld = spiel.update();
				return next;
			}
		}*/
		spiel.notifyObservers(new PlayerChangeEvent());
		return next;
	}

	public void spielerwaechsel(Player eins, Player zwei) {
		spiel.changePlayer(eins, zwei);
	}

	public String runGame() {

		if (spiel.getState() instanceof PlayerBuildState) {
			createPlayers();
			spiel.getState().nextState(spiel);
			return next;
		} else if (spiel.getState() instanceof GameRunningState) {
			spiel.getState().nextState(spiel);
			String rueck = playGame("");
			if (rueck == next)
				spielerwaechsel(eins, zwei);
			if (rueck == next || rueck == "show" || rueck == "load" || rueck == "save" || rueck == "delete")
				return next;
		} else if (spiel.getState() instanceof PlayerChangeState) {
			spiel.getState().nextState(spiel);
			return next;
		}
		return null;
	}
	
	public String runGameFromUrl(String command){
		if(spiel.getState() instanceof PlayerBuildState)
        {
            createPlayers();
            spiel.getState().nextState(spiel);
            runGameFromUrl(command);
        } else
        if(spiel.getState() instanceof GameRunningState)
        {
            String rueck = playGame(command);
            if (rueck == next);
            	spielerwaechsel(eins, zwei);
            if (rueck == next)
            	return next;
        }
        return null;
	}
	

	@Override
	public void update(Event e) {
		if (e == null) {
			this.spielfeld = spiel.update();
			ausgabe(spielfeld, rows, columns, eins, zwei);
			this.aktiv = spiel.aktiverSpieler();
		} else if (e instanceof GameOverEvent) {
			String gameOver = String.format("%n%s hat das Spiel gewonnen!%n", aktiv.getName());
			LOGGER.info(gameOver);
		} else if (e instanceof GameDrawEvent){
			String gameDraw = "Draw";
			LOGGER.info(gameDraw);
		} else if (e instanceof GameLoadEvent){
			this.spielfeld = spiel.update();
			ausgabe(spielfeld, rows, columns, eins, zwei);
		}
	}
	
	public String toHtml()
    {
        String game = toString();
        String result = game.replace(NEWLINE, "<br>");
        result = result.replace("%n", "<br>");
        result = result.replace("     ", " &nbsp; &nbsp; ");
        result = result.replace("   ", " &nbsp; ");
        return result;
    }

    public String toString()
    {
        spielfeld = spiel.update();
        return ausgabe(spielfeld, rows, columns, eins, zwei);
    }

}
