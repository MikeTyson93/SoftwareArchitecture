package de.htwg.se.ws1516.fourwinning.persistence.couchdb;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
// Revision may be needed if you want to clear the whole database
import org.ektorp.Revision;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import de.htwg.se.ws1516.fourwinning.models.Feld;
import de.htwg.se.ws1516.fourwinning.models.PlayArea;
import de.htwg.se.ws1516.fourwinning.models.PlayAreaInterface;
import de.htwg.se.ws1516.fourwinning.models.Player;
import de.htwg.se.ws1516.fourwinning.persistence.PlayAreaInterfaceDAO;

public class PlayAreaCouchDBDAO implements PlayAreaInterfaceDAO{

	private CouchDbConnector db = null;
	
	public PlayAreaCouchDBDAO(){
		HttpClient client = null;
		try {
			client = new StdHttpClient.Builder().url(
					"http://lenny2.in.htwg-konstanz.de:5984").build();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		CouchDbInstance dbInstance = new StdCouchDbInstance(client);
		db = dbInstance.createConnector("connect_four_db", true);
	}
	
	@Override
	public void savePlayArea(PlayAreaInterface PlayArea) {
		if (containsPlayAreaByName(PlayArea.getName())){
			db.update(copyPlayArea(PlayArea));
		} else {
			db.create(PlayArea.getName(), copyPlayArea(PlayArea));
		}
	}

	@Override
	public List<PlayAreaInterface> getAllPlayAreas() {
		List<PlayAreaInterface> lst = new ArrayList<>();
		ViewQuery query = new ViewQuery().allDocs();
		ViewResult vr = db.queryView(query);

		for (Row r : vr.getRows()) {
			lst.add(getPlayArea(r.getId()));
		}
		return lst;
	}

	@Override
	public boolean deletePlayArea(String name) {
		if (!containsPlayAreaByName(name))
			return false;
		try{
			db.delete(copyPlayArea(getPlayArea(name)));
		} catch (Exception e){
			// Unhandled Exception
			return false;
		}
		return true;
	}

	@Override
	public boolean containsPlayAreaByName(String name) {
		PersistancePlayArea pArea = db.find(PersistancePlayArea.class, name);
		if (pArea == null)
			return false;
		return true;
	}

	@Override
	public PlayAreaInterface getPlayArea(String name) {
		PersistancePlayArea pArea = db.find(PersistancePlayArea.class, name);
		if (pArea == null)
			return null;
		return copyPlayArea(pArea);
	}
	
	public PlayAreaInterface copyPlayArea(PersistancePlayArea pArea){
		// We need a transformation from db-model to game-model
		if (pArea == null)
			return null;
		PlayAreaInterface copyPlayArea = new PlayArea(pArea.getRows(), pArea.getColumns());
		copyPlayArea.setName(pArea.getName());
		List<Player> playerlist = pArea.getPlayers();
		copyPlayArea.setPlayers(playerlist.get(0), playerlist.get(1));
		Feld[][] areaOfGame = copyPlayArea.getFeld();
		PersistanceFeld[][] areaOfDb = pArea.getFeld();
		for (int i = 0; i < pArea.getRows(); i++){
			for (int j = 0; j < pArea.getColumns(); j++){
				areaOfGame[i][j].setX(areaOfDb[i][j].getX());
				areaOfGame[i][j].setY(areaOfDb[i][j].getY());
				areaOfGame[i][j].setSet(areaOfDb[i][j].getSet());
				areaOfGame[i][j].setOwner(areaOfDb[i][j].getOwner());
			}
		}
		copyPlayArea.replacePlayArea(areaOfGame, pArea.getName(), pArea.getColumns(), pArea.getRows());
		return copyPlayArea;
	}
	
	public PersistancePlayArea copyPlayArea(PlayAreaInterface pArea){
		// We need a transformation from game-model to db-model
		if (pArea == null)
			return null;
		String name = pArea.getName();
		PersistancePlayArea transformedArea;
		if (containsPlayAreaByName(name)){
			// Ist bereits in DB
			transformedArea = (PersistancePlayArea) db.find(PersistancePlayArea.class, name);
		} else {
			// Ist noch nicht in DB
			transformedArea = new PersistancePlayArea();
			transformedArea.setColumns(pArea.getColumns());
			transformedArea.setRows(pArea.getRows());
			transformedArea.setPlayers(pArea.getPlayerList());
			transformedArea.replacePlayArea(pArea.getFeld(), pArea.getName(), pArea.getColumns(), pArea.getRows());
		}
		return transformedArea;
		
	}

}
