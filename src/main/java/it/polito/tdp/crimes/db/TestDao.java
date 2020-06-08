package it.polito.tdp.crimes.db;

import java.util.List;

import it.polito.tdp.crimes.model.CoppiaOffenseType;
import it.polito.tdp.crimes.model.Event;

public class TestDao {

	public static void main(String[] args) {
		EventsDao dao = new EventsDao();
		/*for(Event e : dao.listAllEvents())
			System.out.println(e);*/
		
		List<String> lista= dao.getOffenceType("auto-theft", 5); 
		for(String s : lista) {
		System.out.println(s+"\n"); 
		}
		
		List<CoppiaOffenseType> coppie= dao.getCouples("auto-theft", 5);  
		for(CoppiaOffenseType c : coppie) {
		System.out.println(c+"\n"); 
		}
	}

}
