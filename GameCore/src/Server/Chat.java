package Server;

import java.util.ArrayList;

public class Chat extends Thread{

	Server server;
	public static ArrayList<String> gelenler = new ArrayList<>();
	public static ArrayList<String> gidecekler = null;
	
	public Chat(Server server){
		this.server = server;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(true){
		gidecekler = null;
		gidecekler = gelenler;
		for(int i = 0 ; i < gidecekler.size(); i++){
			System.out.println(gidecekler.get(i));
			for(int clientler = 0; clientler < this.server.es.size() ; clientler++){
				System.out.println(gidecekler.get(i));
				this.server.es.get(clientler).out.println("chat"+gidecekler.get(i));
			}
			gelenler.remove(gidecekler.get(i));
		}
		
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
		
	}

	public void send(String gelenMesaj){
		gelenler.add(gelenMesaj);
		System.out.println(gelenMesaj);
	}
}
