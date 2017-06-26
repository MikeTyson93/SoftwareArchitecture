package de.htwg.se.ws1516.fourwinning;
import com.google.inject.AbstractModule;

import de.htwg.se.ws1516.fourwinning.controller.IGameController;
import de.htwg.se.ws1516.fourwinning.models.IPlayerAreaFactory;
import de.htwg.se.ws1516.fourwinning.persistence.PlayAreaInterfaceDAO;
import de.htwg.se.ws1516.fourwinning.persistence.couchdb.*;


public class FourWinningModule extends AbstractModule{
	 @Override
	    protected void configure() {

	        bind(IPlayerAreaFactory.class)
	                .to(de.htwg.se.ws1516.fourwinning.models.PlayAreaFactory.class);
	        bind(IGameController.class).to(
	                de.htwg.se.ws1516.fourwinning.controller.impl.GameController.class);

	        //bind(PlayAreaInterfaceDAO.class).to(
	       	//		de.htwg.se.ws1516.fourwinning.persistence.db4o.PlayAreaDb4oDAO.class);
	        bind(PlayAreaInterfaceDAO.class).to(
	        		de.htwg.se.ws1516.fourwinning.persistence.couchdb.PlayAreaCouchDBDAO.class);
	 }
}
