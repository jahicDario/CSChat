import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

public class ChatServer {

	// static ArrayList<Klijent> klijenti = new ArrayList<>();
	static LinkedList<ServerNit> nitiZaKlijente = new LinkedList();

	public static void main(String[] args) {
		int port = 2222;
		Socket klijentSoket = null;
		try {
			ServerSocket serverSoket = new ServerSocket(port);
			while (true) {
		
				klijentSoket = serverSoket.accept();
				// klijenti.add(klijent);
				nitiZaKlijente.add(new ServerNit(klijentSoket, nitiZaKlijente));
				nitiZaKlijente.getLast().start();
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
