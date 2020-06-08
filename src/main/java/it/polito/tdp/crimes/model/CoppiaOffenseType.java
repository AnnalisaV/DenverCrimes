package it.polito.tdp.crimes.model;

public class CoppiaOffenseType {
	
	private String off1; 
	private String off2; 
	private int peso; // numero di distretti in cui avvengono contemporaneamente
	
	/**
	 * @param off1
	 * @param off2
	 * @param peso
	 */
	public CoppiaOffenseType(String off1, String off2, int peso) {
		super();
		this.off1 = off1;
		this.off2 = off2;
		this.peso = peso;
	}

	public String getOff1() {
		return off1;
	}

	public void setOff1(String off1) {
		this.off1 = off1;
	}

	public String getOff2() {
		return off2;
	}

	public void setOff2(String off2) {
		this.off2 = off2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "CoppiaOffenseType [off1=" + off1 + ", off2=" + off2 + ", peso=" + peso + "]";
	}
	
	

}
