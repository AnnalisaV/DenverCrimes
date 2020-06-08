package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs; 
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao; 
	private Graph<String, DefaultWeightedEdge> graph; 
	private List<String> best; 
	
	public Model() {
		this.dao= new EventsDao(); 
	}
	
	/**
	 * Lista di OffenseCategories nel db
	 * @return
	 */
	public List<String> getAllOffenseCategories(){
		return this.dao.getAllOffenseCategories(); 
	}
	
	/**
	 * Lista di Months del db
	 * @return
	 */
	public List<Integer> getMonths(){
		return this.dao.getMonths(); 
	}
	
	public boolean creaGrafo(String category, int month) {
		this.graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertex
		List<String> vertex= this.dao.getOffenceType(category, month);
		
		if(vertex.size() !=0) {
		Graphs.addAllVertices(this.graph, vertex); 
		
		//archi
		for (CoppiaOffenseType c : dao.getCouples(category, month)) {
			// stanno nel grafo?
			if (this.graph.containsVertex(c.getOff1()) && this.graph.containsVertex(c.getOff2())) {
				if (c.getPeso()>0) {
					Graphs.addEdge(this.graph, c.getOff1(),c.getOff2(), c.getPeso()); 
				}
			}
		}
			
		return true; 
		}
		
		
		return false; 
	}
	
	public int nVertex() {
		return this.graph.vertexSet().size(); 
	}
	public int nArchi() {
		return this.graph.edgeSet().size(); 
	}
	
	/**
	 * Archi il cui peso e' > di quello medio
	 * @return
	 */
	public List<CoppiaOffenseType> getEdges(){
		List<CoppiaOffenseType> lista= new ArrayList<>(); 
		
		double pesoMedio=0.0; 
		for (DefaultWeightedEdge e : this.graph.edgeSet()) {
			pesoMedio+=this.graph.getEdgeWeight(e);
		}
		pesoMedio= pesoMedio/this.graph.edgeSet().size(); 
		
		//quale arco va bene 
		for (DefaultWeightedEdge edge : this.graph.edgeSet()) {
			if (this.graph.getEdgeWeight(edge) > pesoMedio) {
				lista.add(new CoppiaOffenseType(this.graph.getEdgeSource(edge), 
						this.graph.getEdgeTarget(edge), (int)this.graph.getEdgeWeight(edge))); 
				
			}
		}
		return lista; 
	}
	
	public List<String> percorso(CoppiaOffenseType arco){
		String partenza= arco.getOff1(); 
		String arrivo= arco.getOff2(); 
		this.best= new ArrayList<>(); //pulita
		
		List<String> percorso= new ArrayList<>(); 
		percorso.add(partenza); 
		ricorsione(percorso, arrivo ); 
		return best; 
		
	}
	
	private void ricorsione(List<String> percorso, String arrivo) {
		
		//condizione terminale 
		if (percorso.get(percorso.size()-1).equals(arrivo)) {
			// e' l'ottima
			if (percorso.size() > best.size()) {
				best= new ArrayList<>(percorso);
			}
			return; //anyway
		}
		
		//in genere 
		String ultimo= percorso.get(percorso.size()-1); 
		for (String vicino : Graphs.neighborListOf(this.graph, ultimo)) {
			//non voglio loop
			if (!percorso.contains(vicino)) {
				percorso.add(vicino); 
				ricorsione(percorso, arrivo); 
				percorso.remove(percorso.size()-1); 
			}
		}
	}
}
