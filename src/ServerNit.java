import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import klijent.Klijent;

public class ServerNit extends Thread{

	BufferedReader ulazniTokOdKlijenta = null;
	PrintStream izlazniTokKaKlijetnu = null;
	Socket soketZaKom = null;
	Klijent klijent = null;
	ArrayList<ServerNit> nitiZaKlijente;
	//static ArrayList<Klijent> klijenti = new ArrayList<>();
	String ime;
	String pol;
	
	public ServerNit(Socket soket, ArrayList<ServerNit> klijent) {
		this.soketZaKom = soket; 
		this.nitiZaKlijente = klijent;
	}
	
	public Socket getSoketZaKom() {
		return soketZaKom;
	}

	public void setSoketZaKom(Socket soketZaKom) {
		this.soketZaKom = soketZaKom;
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

	@Override
	public void run() {
		String linija;
		String ime;
		String pol = "";
		boolean provera = false;
		String zaKogaJePoruka;
		String[] nizKlijentaKomeTrebaPoslatiPoruku;
		try {
			ulazniTokOdKlijenta = new BufferedReader(new InputStreamReader(soketZaKom.getInputStream()));
			izlazniTokKaKlijetnu = new PrintStream(soketZaKom.getOutputStream());
			
			izlazniTokKaKlijetnu.println("Unesite ime: ");
			this.ime = ulazniTokOdKlijenta.readLine();
			while(provera != true){
				izlazniTokKaKlijetnu.println("Unesite pol (M ili Z): ");
				this.pol = ulazniTokOdKlijenta.readLine();
				
				if(pol.equals("M")){
					izlazniTokKaKlijetnu.println("Dobrosao " + ime + "./nZa izlaz /quit");
					provera = true;
				}else if(pol.equals("Z")){
					izlazniTokKaKlijetnu.println("Dobrosla " + ime + "./nZa izlaz /quit");
					provera = true;
				}else{
					izlazniTokKaKlijetnu.println("Niste lepo uneli pol.");
				}
			}
//			Klijent klijent = new Klijent(ime, pol, soketZaKom);
//			klijenti.add(klijent);
			while(true){
				linija = ulazniTokOdKlijenta.readLine();
				if(linija.startsWith("/quit")){
					break;
				}
				//proverava koji su trenutno povezani a suprotnog su pola
				izlazniTokKaKlijetnu.println("Izaberite osobu kome zelite da posaljete poruku(npr. pera mika zika):");
				int i = 1;
				for (ServerNit sn : nitiZaKlijente) {
					if(sn.getPol().equals(this.getPol())){
						continue;
					}else{
						izlazniTokKaKlijetnu.println(i + ". " + sn.getIme());
						i++;
					}
				}
				zaKogaJePoruka = ulazniTokOdKlijenta.readLine();
				nizKlijentaKomeTrebaPoslatiPoruku = zaKogaJePoruka.split(" ");
				
				for (int j = 0; j < nizKlijentaKomeTrebaPoslatiPoruku.length; j++) {
					for (ServerNit sn : nitiZaKlijente) {
						if(sn.getIme().equals(nizKlijentaKomeTrebaPoslatiPoruku[j])){
							sn.izlazniTokKaKlijentu("<" + ime + ">" + linija); /////????
						}
					}
				}
				izlazniTokKaKlijetnu.println("Poruke su uspesno poslate");
				
			}
			izlazniTokKaKlijetnu.println("Dovidjenja " + ime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
