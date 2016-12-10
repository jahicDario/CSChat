import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import klijent.Klijent;

public class ChatServer {

	//static ArrayList<Klijent> klijenti = new ArrayList<>();
	static ArrayList<ServerNit> nitiZaKlijente = new ArrayList<>();
	
	public static void main(String[] args) {
		int port = 2222;
		
		Socket klijentSoket = null;
		
		try {
			ServerSocket serverSoket = new ServerSocket(port);
			while(true){
				klijentSoket = serverSoket.accept();
				//klijenti.add(klijent);
				nitiZaKlijente.add(new ServerNit(klijentSoket, nitiZaKlijente));
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
