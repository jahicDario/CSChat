package zadatak;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatKlijent implements Runnable{

	static Socket soketZaKomunikaciju = null;
	static PrintStream izlazniTokKaServeru = null;
	static BufferedReader ulazniTokOdServera = null;
	static BufferedReader ulazKonzola = null;
	static boolean kraj = false;
	static DatagramSocket datagramSoket = null;
	
	public static void main(String[] args) {
		try {
			int port = 2222;
			soketZaKomunikaciju = new Socket("localhost", port);
			ulazKonzola = new BufferedReader(new InputStreamReader(System.in));
			izlazniTokKaServeru  = new PrintStream(soketZaKomunikaciju.getOutputStream());
			ulazniTokOdServera = new BufferedReader(new InputStreamReader
					(soketZaKomunikaciju.getInputStream()));
			datagramSoket = new DatagramSocket();
			int klijentPort = datagramSoket.getLocalPort();
			//nit za citanje poruka
			new Thread(new ChatKlijent()).start();

			izlazniTokKaServeru.println(klijentPort); //prvo posalji port za udp serveru.
			while(!kraj){
				izlazniTokKaServeru.println(ulazKonzola.readLine());
			}
			soketZaKomunikaciju.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public void run() {
		String linijaOdServera;
		String lista;
		byte[] porukaOdServera = new byte[1024];
		try {
			while((linijaOdServera = ulazniTokOdServera.readLine()) != null){
				System.out.println(linijaOdServera);
						
//				if (linijaOdServera.startsWith("<")) {
//					DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
//					Date dateobj = new Date();
//					System.out.println("\t" +df.format(dateobj));
//				}
				if(linijaOdServera.startsWith("Izaberite")){
					DatagramPacket paketOdServera = new DatagramPacket(porukaOdServera, porukaOdServera.length);
					datagramSoket.receive(paketOdServera);
					lista = new String(paketOdServera.getData());
					System.out.println(lista.trim());
				}
				if(linijaOdServera.indexOf("Dovidjenja") == 0){
					kraj = true;
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
