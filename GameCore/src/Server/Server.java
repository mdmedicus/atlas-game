package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Core.AllianceRun;
import Core.Player;
import Core.WarSimulator;

public class Server {

	protected static Map<String,String> auth = new HashMap<String, String>(); 
	ServerSocket ssocket = null;
	String serverL = "localhost";
	int serverP = 3333;
	String clientGelen;
	Socket csocket;
	public static int client = 0;
	public static int kayitli = 0;
	
	
	public static int[][] map;
	
	public static ArrayList<Integer> playersNo;
	public static ArrayList<String>playersName;
	
	public static Map<String, Player> players;
	public static Map<Integer, String> p_name;
	
	public static WarSimulator ws;
	public static ArrayList<HashMap<Integer,int[]>> marketplace;
	public static AllianceRun allianceRun;
	public static Chat chat;
	public static ArrayList<EchoServer> es = new ArrayList<>();
	
	public Server(ArrayList<Integer> playersNo, ArrayList<String>playersName, Map players, int[][] map,
			Map p_name,WarSimulator ws,ArrayList<HashMap<Integer,int[]>> marketplace, AllianceRun allianceRun){
		this.playersNo = playersNo;
		this.playersName = playersName;
		this.players = players;
		this.map = map;
		this.p_name = p_name;
		this.ws = ws;
		this.marketplace = marketplace;
		this.allianceRun = allianceRun;
		chat = new Chat(this);
		chat.start();
	}	
	
	public void Server(){
	try {
		ssocket = new ServerSocket(serverP);		
		while(true){
			client++;
			if(client <= 10000){
			 csocket = ssocket.accept();
			 
			 System.out.println("New Client:"+client);
			 new EchoServer(csocket,playersNo,playersName, auth, players,map,p_name,ws,client,
					 kayitli,marketplace,allianceRun,chat,es).start();
			} else{
				break;
			}
		}
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Port Hatasý");
	}
	
	
}

}
