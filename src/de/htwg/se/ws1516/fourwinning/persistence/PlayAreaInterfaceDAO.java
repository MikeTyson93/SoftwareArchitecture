package de.htwg.se.ws1516.fourwinning.persistence;

import java.util.List;

import de.htwg.se.ws1516.fourwinning.models.PlayAreaInterface;

public interface PlayAreaInterfaceDAO {
	void savePlayArea(PlayAreaInterface PlayArea);
	List getAllPlayAreas();
	boolean deletePlayArea(final String name);
	boolean containsPlayAreaByName(String name);
	PlayAreaInterface getPlayArea(String name);
}