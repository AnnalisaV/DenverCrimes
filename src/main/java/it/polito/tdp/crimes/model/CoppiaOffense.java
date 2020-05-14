package it.polito.tdp.crimes.model;

public class CoppiaOffense {

	private String offense1; 
	private String offense2;
	private double  peso;
	
	
	/**
	 * @param offense1
	 * @param offense2
	 * @param peso
	 */
	public CoppiaOffense(String offense1, String offense2, double peso) {
		super();
		this.offense1 = offense1;
		this.offense2 = offense2;
		this.peso = peso;
	}


	public String getOffense1() {
		return offense1;
	}


	public void setOffense1(String offense1) {
		this.offense1 = offense1;
	}


	public String getOffense2() {
		return offense2;
	}


	public void setOffense2(String offense2) {
		this.offense2 = offense2;
	}


	public double getPeso() {
		return peso;
	}


	public void setPeso(int peso) {
		this.peso = peso;
	}


	@Override
	public String toString() {
		return "CoppiaOffense "+ offense1 + ", " + offense2 + " con " + peso;
	} 
	
	
	
	
	
	
}
