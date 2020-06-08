package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.CoppiaOffenseType;
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
	
	/**
	 * Ottenere tutte le categorie di reato dal db
	 * @return lista di categorie reato
	 */
	public List<String> getAllOffenseCategories(){
		String sql="SELECT DISTINCT offense_category_id " + 
				"FROM EVENTS " + 
				"ORDER BY offense_category_id ASC ";
		List<String> lista= new ArrayList<>(); 
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				lista.add(new String(res.getString("offense_category_id"))); 
			}
			conn.close();
			return lista ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	/**
	 * Ottenere da db i Months in cui sono avvenuti dei reati
	 * @return lista di Months
	 */
	public List<Integer> getMonths(){
		String sql="SELECT DISTINCT MONTH(reported_date) as mese " + 
				"FROM EVENTS " + 
				"ORDER BY mese ASC ";
		List<Integer> listaMesi= new ArrayList<>();
		
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				listaMesi.add(new Integer(res.getInt("mese"))); 
			}
			conn.close();
			return listaMesi ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		
	}
	/**
	 * Tipo di reato appartenenti ad una certa Category avvenuti in un certo Month
	 * @param category categoria di appartenza del reato
	 * @param month mese in cui esso e' avvenuto
	 * @return
	 */
	public List<String> getOffenceType(String category, Integer month){
		String sql="SELECT distinct offense_type_id as off " + 
				"FROM EVENTS " + 
				"WHERE MONTH(reported_date)=?  AND offense_category_id=? "; 
		List<String> lista= new ArrayList<>(); 
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            st.setInt(1,  month);
            st.setString(2,  category);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				lista.add(new String(res.getString("off"))); 
			}
			conn.close();
			return lista ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	/**
	 * Coppie reati
	 * @param category
	 * @param month
	 * @return
	 */
	public List<CoppiaOffenseType> getCouples(String category, int month) {
		//devo ripetere alcune condizioni altrimenti il db non ce la fa 
		String sql="SELECT e1.offense_type_id, e2.offense_type_id, " + 
				"COUNT(DISTINCT e1.neighborhood_id) AS n " + 
				"FROM EVENTS AS e1, EVENTS AS e2 " + 
				"WHERE e1.offense_type_id > e2.offense_type_id " + 
				"AND e1.offense_category_id=e2.offense_category_id " + 
				"AND e1.offense_category_id=? " + 
				"AND MONTH(e1.reported_date)= MONTH(e2.reported_date) " + 
				"AND MONTH(e1.reported_date)=? " + 
				"AND e1.neighborhood_id=e2.neighborhood_id " + 
				"GROUP BY e1.offense_type_id, e2.offense_type_id "; 
		
		List<CoppiaOffenseType> lista= new ArrayList<>(); 
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            st.setString(1, category);
            st.setInt(2, month);
           
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				CoppiaOffenseType c= new CoppiaOffenseType(res.getString("e1.offense_type_id"), 
					res.getString("e2.offense_type_id"), res.getInt("n"));
				lista.add(c); 
			}
			conn.close();
			return lista ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

}
