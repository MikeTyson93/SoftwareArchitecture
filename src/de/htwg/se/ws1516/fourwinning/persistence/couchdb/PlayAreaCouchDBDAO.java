package de.htwg.se.ws1516.fourwinning.persistence.couchdb;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import de.htwg.se.ws1516.fourwinning.models.*;
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
			PersistancePlayArea pArea = db.find(PersistancePlayArea.class, PlayArea.getName());
			pArea = copyPlayArea(PlayArea, true);
			db.update(pArea);
		} else {
			db.create(PlayArea.getName(), copyPlayArea(PlayArea, false));
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
			db.delete(copyPlayArea(getPlayArea(name), false));
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
		List<PersistancePlayer> playerlist = pArea.getPlayers();
		// transform players
		Player one = null;
		Player two = null;
		int idx = 0;
		for (PersistancePlayer pp : playerlist){
			if (idx == 0) {
				one = new Player(pp.getName(), 21);
				one.setActive(pp.getActive());
			} else {
				two = new Player(pp.getName(), 21);
				two.setActive(pp.getActive());
			}
			idx++;
		}
		copyPlayArea.setPlayers(one, two);
		copyPlayArea.setId(pArea.getId());
		Feld[][] areaOfGame = copyPlayArea.getFeld();
		PersistanceFeld[][] areaOfDb = pArea.getFeld();
		/*if (areaOfDb == null){
			pArea.setClearFeld();
			areaOfDb = pArea.getFeld();
		}*/

		for (int i = 0; i < pArea.getRows(); i++){
			for (int j = 0; j < pArea.getColumns(); j++) {
				areaOfGame[i][j].setX(areaOfDb[i][j].getX());
				areaOfGame[i][j].setY(areaOfDb[i][j].getY());
				areaOfGame[i][j].setSet(areaOfDb[i][j].getSet());
				Player owner = null;
				if (areaOfDb[i][j].getOwner() != null) {
					String playerName = areaOfDb[i][j].getOwner().getName();
					int menge = areaOfDb[i][j].getOwner().getMenge();
					boolean isActive = areaOfDb[i][j].getOwner().getActive();
					owner = new Player(playerName, menge);
					owner.setActive(isActive);
					areaOfGame[i][j].setOwner(owner);
				}
			}
		}
		copyPlayArea.replacePlayArea(areaOfGame, pArea.getName(), pArea.getColumns(), pArea.getRows());
		return copyPlayArea;
	}
	
	public PersistancePlayArea copyPlayArea(PlayAreaInterface pArea, boolean overwrite){
		// We need a transformation from game-model to db-model
		if (pArea == null)
			return null;
		String name = pArea.getName();
		PersistancePlayArea transformedArea;
		if (containsPlayAreaByName(name)){
			// Ist bereits in DB
			if (!overwrite) {
				transformedArea = (PersistancePlayArea) db.find(PersistancePlayArea.class, name);
			}
			else{
				transformedArea = (PersistancePlayArea) db.find(PersistancePlayArea.class, name);
				transformedArea.setColumns(pArea.getColumns());
				transformedArea.setRows(pArea.getRows());
				transformedArea.setPlayers(pArea.getPlayerList());
				transformedArea.setId(pArea.getId());
				transformedArea.setClearFeld(pArea.getRows(), pArea.getColumns());
				transformedArea.replacePlayArea(transformedArea.getFeld(), pArea.getFeld(), pArea.getName(), pArea.getColumns(), pArea.getRows());
			}
		} else {
			// Ist noch nicht in DB
			transformedArea = new PersistancePlayArea();
			transformedArea.setColumns(pArea.getColumns());
			transformedArea.setRows(pArea.getRows());
			transformedArea.setPlayers(pArea.getPlayerList());
			transformedArea.setId(pArea.getId());
			transformedArea.setClearFeld(pArea.getRows(), pArea.getColumns());
			transformedArea.replacePlayArea(transformedArea.getFeld(), pArea.getFeld(), pArea.getName(), pArea.getColumns(), pArea.getRows());
		}
		return transformedArea;
		
	}

}
