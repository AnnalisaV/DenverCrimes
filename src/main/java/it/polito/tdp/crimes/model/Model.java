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
	
	public Model() {
		this.dao= new EventsDao(); 
	}
	
	/**
	 * Ottengo tutti i mesi in cui sono avvenuti Eventi 
	 * @return
	 */
	public List<Integer> getMesi(){
		return dao.getMesi(); 
	}
	/**
	 * Ottengo tutte le possibili categorie di Eventi
	 * @return
	 */
	public List<String> getCategorie(){
		return dao.getCategorie(); 
	}
	
	
	public void creaGrafo(String categoria, int mese) {
		this.graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class); 
		
		List<CoppiaOffense> coppie= this.dao.getCoppie(categoria, mese);
		for (CoppiaOffense c : coppie) {
			if(!this.graph.containsVertex(c.getOffense1())) {
			this.graph.addVertex(c.getOffense1()); 
		}
			if(!this.graph.containsVertex(c.getOffense2())) {
				this.graph.addVertex(c.getOffense2()); 
			}
			
			if(this.graph.getEdge(c.getOffense1(), c.getOffense2()) == null) {
				// aggiungo l'arco
				Graphs.addEdgeWithVertices(this.graph, c.getOffense1(), c.getOffense2(), c.getPeso()); 
			} // non e' orientato quindi va bene fare solo quel controllo
	}
		System.out.println("Grafo creato con "+this.graph.vertexSet().size()+" vertici e "+
	this.graph.edgeSet().size()+" archi\n"); 
		
	
}
	
	public List<CoppiaOffense> getArchi(){
		
		double pesoMedio=0.0;
		
		for(DefaultWeightedEdge e: this.graph.edgeSet()) {
			pesoMedio+=this.graph.getEdgeWeight(e); 
		}
		pesoMedio= pesoMedio/this.graph.edgeSet().size(); //peso medio degli archi del grafo
		
		//Voglio solo gli archi il cui peso > pesoMedio
		List<CoppiaOffense> archi= new ArrayList<>(); 
		for (DefaultWeightedEdge e : this.graph.edgeSet()) {
			if (this.graph.getEdgeWeight(e)> pesoMedio) {
				archi.add(new CoppiaOffense(this.graph.getEdgeSource(e), this.graph.getEdgeTarget(e), this.graph.getEdgeWeight(e))); 
			}
			
			
		}
		return archi; // se voglio la ordino anche per peso
		
	}
}
