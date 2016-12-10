package klijent;

import java.net.ServerSocket;
import java.net.Socket;

public class Klijent {

	private String ime;
	private String pol;
	Socket klijentSoket;
	ServerNit serverNit; 
	
	public Klijent(String ime, String pol, Socket klijentSoket) {
		this.ime = ime;
		this.pol = pol;
		this.klijentSoket = klijentSoket;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public Socket getKlijentSoket() {
		return klijentSoket;
	}

	public void setKlijentSoket(Socket klijentSoket) {
		this.klijentSoket = klijentSoket;
	}
	
	
}
