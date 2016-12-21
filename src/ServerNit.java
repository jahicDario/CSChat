import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class ServerNit extends Thread {

	BufferedReader ulazniTokOdKlijenta = null;
	PrintStream izlazniTokKaKlijetnu = null;
	Socket soketZaKom = null;
	LinkedList<ServerNit> nitiZaKlijente;
	// static ArrayList<Klijent> klijenti = new ArrayList<>();
	String ime;
	String pol;

	public ServerNit(Socket soket, LinkedList<ServerNit> nitiZaKlijente2) {
		this.soketZaKom = soket;
		this.nitiZaKlijente = nitiZaKlijente2;
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
		String zvezda = "";
		String proveriPol = "";
		try {
			ulazniTokOdKlijenta = new BufferedReader(new InputStreamReader(soketZaKom.getInputStream()));
			izlazniTokKaKlijetnu = new PrintStream(soketZaKom.getOutputStream());

			izlazniTokKaKlijetnu.println("Unesite ime: ");
			ime = ulazniTokOdKlijenta.readLine();
			// Unosenje pola dok se ne unese u pravom formatu
			while (provera != true) {
				izlazniTokKaKlijetnu.println("Unesite pol (M ili Z): \n");
				pol = ulazniTokOdKlijenta.readLine();

				if (pol.equals("M")) {
					izlazniTokKaKlijetnu.println("Dobrosao " + ime + ".\nZa izlaz unesite quit");
					provera = true;
				} else if (pol.equals("Z")) {
					izlazniTokKaKlijetnu.println("Dobrosla " + ime + ".\nZa izlaz unesite quit");
					provera = true;
				} else {
					izlazniTokKaKlijetnu.println("Niste lepo uneli pol u pravom formatu - M ili Z.");
				}
			}
			// Unesi u listu korisnika.
			this.ime = ime;
			this.pol = pol;
			while (true) {
				// proveri da li ima nekog kome moze da se posalje poruka.
				if (nitiZaKlijente.size() <= 1) {
					izlazniTokKaKlijetnu.println("Trenutno nema korisnika koji su online.");
					izlazniTokKaKlijetnu.println("Unesite * da bi se proverilo da li se neko konektovao.");
					zvezda = "";
					while (!zvezda.startsWith("*")) {
						zvezda = ulazniTokOdKlijenta.readLine();
					}
					continue;
				}
				izlazniTokKaKlijetnu.println("Unesite svoju poruku:\n");
				linija = ulazniTokOdKlijenta.readLine();
				if (linija.startsWith("quit")) {
					break;
				}

				int i = 1;
				while (true) {
					// proverava koji su trenutno povezani a suprotnog su pola
					// izlistaj listu korisnika suprotnog pola
					for (ServerNit sn : nitiZaKlijente) {
						if (sn.getPol().equals(pol)) {
							continue;
						} else {
							izlazniTokKaKlijetnu.println(i + ". " + sn.getIme());
							i++;
						}
					}
					// ako je i jedan, nema nijednog klijenta suprotnog pola
					if (i == 1) {
						izlazniTokKaKlijetnu.println("Nema korisnika suprotnog pola.");
						izlazniTokKaKlijetnu.println("Unesite * da proverite da li se neko suprotnog pola konektovao.");
						proveriPol = "";
						while (!proveriPol.startsWith("*")) {
							proveriPol = ulazniTokOdKlijenta.readLine();
						}
						// proveri ponovo da li ima nekog suprotnog pola
						continue;
					} else {
						izlazniTokKaKlijetnu
								.println("Izaberite osobe kojima zelite da posaljete poruku(npr. pera mika zika):");
						zaKogaJePoruka = ulazniTokOdKlijenta.readLine();
						nizKlijentaKomeTrebaPoslatiPoruku = zaKogaJePoruka.split(" ");
						int neposlatePoruke = 0;
						for (int j = 0; j < nizKlijentaKomeTrebaPoslatiPoruku.length; j++) {
							// proveri da li se umedjuvremenu izabrani korisnik
							// odjavio
							int brojac = 0;
							for (ServerNit sn : nitiZaKlijente) {
								if (sn.getIme().equals(nizKlijentaKomeTrebaPoslatiPoruku[j])) {
									sn.izlazniTokKaKlijetnu.println("<" + ime + ">" + linija); ///// ????
									try {
										PrintWriter out = new PrintWriter(
												new BufferedWriter(new FileWriter("poruke.txt", true)));
										DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
										Date dateobj = new Date();
										out.println("<" + ime + "> " + linija + "\t | Vreme slanja: " + df.format(dateobj)
												+ "\t | Za: " + sn.getIme());
										out.close();
									} catch (FileNotFoundException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								} else {
									brojac++;
								}
							}
							// korisnik se ne nalazi u listi online korisnika,
							// izasao je umedjuuvremenu
							if (brojac == nitiZaKlijente.size()) {
								izlazniTokKaKlijetnu
										.println("Poruka nije poslata: " + nizKlijentaKomeTrebaPoslatiPoruku[j] 
												 + ", trazeni klijent se ne nalazi na cetu.");
								neposlatePoruke++;
								//upisi u fajl da poruka nije uspesno poslata
								try {
									PrintWriter out = new PrintWriter(
											new BufferedWriter(new FileWriter("poruke.txt", true)));
									DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
									Date dateobj = new Date();
									out.println("<" + ime + "> " + linija + "\t | Vreme slanja: " + df.format(dateobj)
											+ "\t | Za: " + nizKlijentaKomeTrebaPoslatiPoruku[j] + "\t | NIJE USPESNO POSLATA");
									out.close();
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
						if (neposlatePoruke == 0) {
							izlazniTokKaKlijetnu.println("Sve poruke su uspesno poslate");
						} else if (neposlatePoruke == nizKlijentaKomeTrebaPoslatiPoruku.length) {
							// nijedna poruka nije poslata.
						} else {
							izlazniTokKaKlijetnu.println("Preostale poruke su uspesno poslate.");
						}

						break;
					}
				}

			}
			izlazniTokKaKlijetnu.println("Dovidjenja " + ime);
			nitiZaKlijente.remove(this);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
