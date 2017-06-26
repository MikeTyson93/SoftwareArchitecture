package de.htwg.se.ws1516.fourwinning.controller.impl;

import de.htwg.se.ws1516.fourwinning.models.*;
import de.htwg.se.ws1516.fourwinning.persistence.PlayAreaInterfaceDAO;
import de.htwg.util.*;
import de.htwg.util.observer.Observable;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import de.htwg.se.ws1516.fourwinning.controller.*;



public class GameController extends Observable implements IGameController {
	private GameStates status = GameStates.WELCOME;
	private String statusText = "Welcome";
	private PlayAreaInterface grid;
	private IPlayerAreaFactory gridfactory;
	private Player one;
	private Player two;
	int mengeOne;
	int mengeTwo;
	RuleController regeln;
	private int rows;
	private int columns;
	int currentColumn;
	int currentRow;
	boolean spielGewonnen;
	CreateCommand commands;
	private IGameState state;
	private PlayAreaInterfaceDAO dbInterface;
	
	
	@Inject
	public GameController(IPlayerAreaFactory playfactory, PlayAreaInterfaceDAO dbInterface) {
		this.gridfactory = playfactory;
		this.dbInterface = dbInterface;
	}
	
	@Override
	public PlayAreaInterface getSpielfeld(){
		return grid;
	}
	
	@Override
	public void setSpielfeld(PlayAreaInterface grid){
		this.grid = grid;
	}
	
	@Override
	public void baueSpielfeld(int rows, int columns) {
		
		this.setRows(rows);
		this.setColumns(columns);
		grid = gridfactory.create(rows, columns);
		mengeOne = (rows * columns / 2 + 1);
		mengeTwo = (rows * columns / 2);
		regeln = new RuleController(rows, columns);
		status = GameStates.CREATE_AREA;
		statusText = "Area created";
		
	}

	@Override
	public void createPlayers(String nameOne, String nameTwo) {
		commands = new CreateCommand();
		state = new PlayerBuildState();
		one = new Player(nameOne, mengeOne);
		two = new Player(nameTwo, mengeTwo);
		one.setActive(true);
		two.setActive(false);
		status = GameStates.CREATE_PLAYERS;
		statusText = "Players created";
	}

	/*
	 * liefert momentan aktiven Spieler zurueck
	 */
	@Override
	public Player aktiverSpieler() {
		if (one.getActive()){
			status = GameStates.PLAYER_ONE_TURN;
			statusText = "It's the turn of Player one";
			return one;
		} else{
			status = GameStates.PLAYER_TWO_TURN;
			statusText = "It's the turn of Player two";
			return two;
		}
	}

	@Override
	public Player inAktiverSpieler(){
		if (one.getActive()){
			return two;
		} else{
			return one;
		}
	}

	@Override
	public Player getPlayerOne() {
		return one;
	}

	@Override
	public Player getPlayerTwo() {
		return two;
	}

	/*
	 * Wechselt aktiven Spieler und liefert den aktiven Spieler zurueck
	 */
	@Override
	public Player changePlayer(Player one, Player two) {
		
		if (one.getActive()) {
			one.setActive(false);
			two.setActive(true);
			
			return two;
        }
        one.setActive(true);
        two.setActive(false);
  
		return one;
		
		}

	/*
	 * Macht den Zug und gibt zurueck ob dieser funktioniert hat
	 */
	@Override
	public String zug(int column, Player p) {
		save(grid.getFeld(), column);
		currentColumn = column;
		int statuszug = grid.setChip(column, p);
		if (statuszug == -2){
			
			return "Zug fehlgeschlagen";
		}
		currentRow = statuszug;
		notifyObservers();
		
		return "Zug erfolgreich";
		
	}

	/*
	 * gibt aktuelles Spielfeld zurueck
	 */
	@Override
	public Feld[][] update(){
		
		return grid.getFeld();
	}

	@Override
	public boolean spielGewonnen(Feld[][] feld, Player p) {
		boolean won = false;
		this.spielGewonnen = regeln.getWin(feld, p, currentRow, currentColumn);
		status = GameStates.CHECK_WIN;
		statusText = "Regeln werden auf Gewinner ueberprueft";
		
		won = spielGewonnen;
		if (won){
			notifyObservers(new GameOverEvent());
		}
		return won;
	}

	@Override
	public boolean spielDraw(Feld[][] feld) {
		boolean draw = false;
		status = GameStates.CHECK_DRAW;
		statusText = "Regeln werden auf Unentschieden ueberprueft";
		
		draw = regeln.getDraw(feld);
		if (draw){
			notifyObservers(new GameDrawEvent());
			
		}
		return draw;
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	@Override
	public void undo(){
		Feld[][] ersatzfeld = commands.undoCommand();
		grid.setFeld(ersatzfeld);
		notifyObservers();
	}
	
	@Override
	public void save(Feld[][] grid, int column){
		commands.doCommand(grid, column);
		
	}
	
	@Override
	public void redo(){
		int spalte = commands.redoCommand();
		zug(spalte, aktiverSpieler());
		notifyObservers();
		
	}

	@Override
	public GameStates getStatus(){
		return status;
	}

	@Override
	public String getStatusText(){
		return statusText;
	}

	@Override
	public int getColumns() {
		return columns;
	}

	@Override
	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	@Override
	public void setState(IGameState state) {
        this.state = state;
        
    }

    @Override
	public void newGame(){
		grid.clearFeld();
		regeln.resetCounters();
		one.resetZuege();
		two.resetZuege();
		notifyObservers(new NewGameEvent());

	}

	@Override
	public IGameState getState(){
		return state;
	}
	
	// Database interactions######################
	// This methods checks if grid is available by name.
	// If not, return false else return true and replace the current grid with the grid from the db
	@Override
	public boolean loadFromDB(String name) {
		if (!dbInterface.containsPlayAreaByName(name))
			return false;
		PlayAreaInterface gridCopy = dbInterface.getPlayArea(name);
		
		this.grid = gridCopy;
		this.one = gridCopy.getPlayer(0);
		this.two = gridCopy.getPlayer(1);
		grid.replacePlayArea(gridCopy.getFeld(), gridCopy.getName(), gridCopy.getColumns(), gridCopy.getRows());
		notifyObservers(new GameLoadEvent());
		return true;
	}

	@Override
	public boolean saveToDB(String name){
		// First save the game field
		if (dbInterface.containsPlayAreaByName(name))
			return false;
		grid.setName(name);
		grid.setPlayers(one, two);
		dbInterface.savePlayArea(grid);
		// Now save the active player (which one continues?)
		return true;
	}
	
	@Override
	public boolean updateToDB(String name){
		grid.setName(name);
		grid.setPlayers(one, two);
		dbInterface.savePlayArea(grid);
		return true;
	}
	
	@Override
	public boolean deleteFromDB(String name){
		return dbInterface.deletePlayArea(name);
	}
	
	@Override
	public String getAllGridsFromDB(){
		List<PlayAreaInterface> areas = dbInterface.getAllPlayAreas();
		StringBuilder sb = new StringBuilder();
		int idx = 1;
		for (PlayAreaInterface area: areas){
			sb.append("\n");
			sb.append("'");
			sb.append("Spielstand " + idx + ": \t'" + area.getName() + "'");
			sb.append("'");
			sb.append("\n");
			idx++;
		}
		return sb.toString();
	}
	
	@Override
	public String[] AllGridNames(){
		List<String> all = new ArrayList<>();
		
		int idx = 0;
		List<PlayAreaInterface> areas = dbInterface.getAllPlayAreas();
		String[] retval = new String[areas.size()];
		for (PlayAreaInterface area: areas){
			all.add("Spielstand " + idx + ": \t" + area.getName());
			idx++;
		}
		return all.toArray(retval);
	}
}
