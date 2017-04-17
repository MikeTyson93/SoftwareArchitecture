package de.htwg.se.ws1516.fourwinning.persistence.couchdb;

import java.net.MalformedURLException;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.Revision;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import de.htwg.se.ws1516.fourwinning.models.PlayAreaInterface;
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
		db = dbInstance.createConnector("connectFour_db", true);

	}
	
	@Override
	public void savePlayArea(PlayAreaInterface PlayArea) {
		
	}

	@Override
	public List<PlayAreaInterface> getAllPlayAreas() {
		return null;
	}

	@Override
	public boolean deletePlayArea(String name) {
		return false;
	}

	@Override
	public boolean containsPlayAreaByName(String name) {
		return false;
	}

	@Override
	public PlayAreaInterface getPlayArea(String name) {
		return null;
	}

}
