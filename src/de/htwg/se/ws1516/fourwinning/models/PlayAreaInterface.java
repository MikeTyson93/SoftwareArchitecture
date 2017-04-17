package de.htwg.se.ws1516.fourwinning.models;

import java.util.List;

public interface PlayAreaInterface {
	void buildArea(int rows, int columns);
	int setChip(int column, Player p);
	int getColumns();
	int getRows();
	void setFeld(Feld[][] zusatzfeld);
	Feld[][] getFeld();
	String getName();
	void setName(String name);
	void replacePlayArea(Feld[][] feld, String name, int columns, int rows);
	public void setPlayers(Player one, Player two);
	Player getPlayer(int idx);
	void clearPlayers();
	List<Player> getPlayerList();
}
