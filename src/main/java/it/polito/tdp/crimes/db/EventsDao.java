package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.crimes.model.CoppiaOffense;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	//non sapendo se ci sono Eventi in ogni mese conviene chieder al dao e non fare automatico da 1-12
	public List<Integer> getMesi(){
		String sql="SELECT DISTINCT Month(reported_date) as mese "
				+ "FROM events "; 
		
		List<Integer> mesi= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
		    ResultSet res = st.executeQuery() ;
		    
		    while(res.next()) {
		    	Integer mese= new Integer(res.getInt("mese")); 
		    	mesi.add(mese); 
		    }
			conn.close();
			Collections.sort(mesi); //li ordino per estetica
			return mesi; 
		
	}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

}
	
	//Prelevo tutte le categorie
	public List<String> getCategorie(){
		String sql="SELECT DISTINCT offense_category_id as cate "
				+ "FROM events "; 
		
		List<String> categorie= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
		    ResultSet res = st.executeQuery() ;
		    
		    while(res.next()) {
		    	String c= new String(res.getString("cate")); 
		    	categorie.add(c); 
		    }
			conn.close(); 
			return categorie; 
		
	}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

}

	/**
	 * Tipi di reato che avvengono nello stesso mese, nella stessa categoria, nello stesso quartiere
	 * @param categoria
	 * @param mese
	 * @return
	 */
	public List<CoppiaOffense> getCoppie(String categoria, int mese) {
		String sql= "SELECT e1.offense_type_id as v1, e2.offense_type_id as v2, " + 
				"       COUNT(DISTINCT e1.neighborhood_id) AS peso " + 
				"FROM EVENTS e1, EVENTS e2 " + 
				"WHERE e1.offense_category_id=?  AND " + 
				"      e1.offense_category_id=e2.offense_category_id and " + 
				"      MONTH(e1.reported_date)=? AND MONTH(e2.reported_date)=? AND " + 
				"      e1.offense_type_id != e2.offense_type_id AND " + 
				"      e1.neighborhood_id = e2.neighborhood_id " + 
				"GROUP BY e1.offense_type_id, e2.offense_type_id "; 
		
           List<CoppiaOffense> offenses= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            
            st.setString(1, categoria);
            st.setInt(2, mese);
            st.setInt(3,  mese);
            
		    ResultSet res = st.executeQuery() ;
		    
		    while(res.next()) {
		    	CoppiaOffense c= new CoppiaOffense(res.getString("v1"), res.getString("v2"), res.getDouble("peso")); 
		    	offenses.add(c); 
		    }
			conn.close(); 
			return offenses; 
		
	}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		
	}
}
