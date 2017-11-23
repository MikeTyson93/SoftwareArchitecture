package de.htwg.se.ws1516.fourwinning.controller;


import de.htwg.util.observer.IObservable;

import de.htwg.se.ws1516.fourwinning.models.Feld;
import de.htwg.se.ws1516.fourwinning.models.PlayAreaInterface;
import de.htwg.se.ws1516.fourwinning.models.Player;

public interface IGameController extends IObservable {
	PlayAreaInterface getSpielfeld();

	void setSpielfeld(PlayAreaInterface grid);

	void baueSpielfeld(int rows, int columns);

	void createPlayers(String nameOne, String nameTwo);

	Player aktiverSpieler();

	Player inAktiverSpieler();

	Player getPlayerOne();

	Player getPlayerTwo();

	Player changePlayer(Player one, Player two);

	String zug(int column, Player p);

	Feld[][] update();

	boolean spielGewonnen(Feld[][] feld, Player p);

	boolean spielDraw(Feld[][] feld);

	int getRows();

	void setRows(int rows);

	void undo();

	void save(Feld[][] grid, int column);

	void redo();

	GameStates getStatus();

	String getStatusText();

	int getColumns();

	void setColumns(int columns);

	void setState(IGameState state);

	IGameState getState();

	boolean loadFromDB(String name);

	boolean saveToDB(String name);

	boolean deleteFromDB(String name);

	String getAllGridsFromDB();

	String[] AllGridNames();

	boolean updateToDB(String name);

	void newGame();

	void createPlayerOne(String nameOne);

	void createPlayerTwo(String nameTwo);

	boolean allPlayersCreated();
}