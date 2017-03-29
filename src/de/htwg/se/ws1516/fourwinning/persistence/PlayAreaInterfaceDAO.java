package de.htwg.se.ws1516.fourwinning.persistence;

import java.util.List;

import de.htwg.se.ws1516.fourwinning.models.PlayAreaInterface;

public interface PlayAreaInterfaceDAO {
	void savePlayArea(PlayAreaInterface PlayArea);
	List<PlayAreaInterface> getAllPlayAreas();
	void deletePlayArea(PlayAreaInterface PlayArea);
	boolean containsPlayAreaByName(String name);
	PlayAreaInterface getPlayArea(String name);
}
