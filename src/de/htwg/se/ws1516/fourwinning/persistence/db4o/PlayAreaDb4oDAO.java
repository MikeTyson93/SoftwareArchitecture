package de.htwg.se.ws1516.fourwinning.persistence.db4o;

import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

import de.htwg.se.ws1516.fourwinning.models.PlayAreaInterface;
import de.htwg.se.ws1516.fourwinning.models.Player;
import de.htwg.se.ws1516.fourwinning.persistence.PlayAreaInterfaceDAO;

public class PlayAreaDb4oDAO implements PlayAreaInterfaceDAO{
	
	private ObjectContainer database;
	
	public PlayAreaDb4oDAO() {
		database = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
				"playArea.data");
	}


	@Override
	public void savePlayArea(final PlayAreaInterface PlayArea) {
		try{
			database.store(PlayArea);
			database.commit();
			database.rollback();
		} catch (Exception e){
			// Unhandled Exception
			database.rollback();
		}
	}

	@Override
	public List<PlayAreaInterface> getAllPlayAreas() {
		return database.query(PlayAreaInterface.class);
	}


	@Override
	public void deletePlayArea(final PlayAreaInterface PlayArea) {
		try{
			database.delete(PlayArea);
			database.commit();
		} catch (Exception e){
			// Unhandled Exception
			database.rollback();
		}
	}


	@Override
	public boolean containsPlayAreaByName(String name) {
		List<PlayAreaInterface> playareas = database.query(new Predicate<PlayAreaInterface>() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean match(PlayAreaInterface playarea) {
				return (playarea.getName().equals(name));
			}
		});

		if (playareas.size() > 0) {
			return true;
		}
		return false;
	}
	

	@Override
	public PlayAreaInterface getPlayArea(String name) {
		List<PlayAreaInterface> playareas = database.query(new Predicate<PlayAreaInterface>() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean match(PlayAreaInterface playarea) {
				return (playarea.getName().equals(name));
			}
		});

		if (playareas.size() > 0) {
			database.rollback();
			return playareas.get(0);
		}
		return null;
	}

}