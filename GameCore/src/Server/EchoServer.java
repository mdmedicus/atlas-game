package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Core.Alliance;
import Core.AllianceRun;
import Core.Player;
import Core.WarSimulator;

public class EchoServer extends Thread{
	protected Socket socket;
	protected String nickname;
	protected String password;
	protected Map auth;
	protected boolean logined = false;
	
	private int race = 1;
	private int[] coordinate = {0,0};
	
	
	public static ArrayList<Integer> playersNo;
	public static ArrayList<String>playersName;
	public static Map<String, Player> players;
	public static Map<Integer, String> p_name;
	public static int[][] map;
	public Player player;
	public static WarSimulator ws;
	public static ArrayList<HashMap<Integer,int[]>> marketplace;
	
	public static int client;
	public static int kayitli;
	
	public static AllianceRun allianceRun;
	public static Chat chat;
	public static ArrayList<EchoServer> es;
	public PrintWriter out;
	public BufferedReader in;
	
	public EchoServer(Socket csocket, ArrayList<Integer> playersNo, ArrayList<String>playersName,Map auth,
			Map players,int[][] map, Map p_name,WarSimulator ws, int client,
			int kayitli,ArrayList<HashMap<Integer,int[]>> marketplace, AllianceRun allianceRun,
				Chat chat, ArrayList<EchoServer> es){
		this.socket = csocket;
		this.playersNo = playersNo;
		this.playersName = playersName;
		this.auth = auth;
		this.players = players;
		this.map = map;
		this.p_name = p_name;
		this.ws = ws;
		this.client = client;
		this.kayitli = kayitli;
		this.marketplace = marketplace;
		this.allianceRun = allianceRun;
		this.chat = chat;
		this.es = es;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String clientGelen;
		try{
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		
		
		
			while(true){
				clientGelen = in.readLine();
		if(clientGelen.equals("0")){
			
			nickname = in.readLine();
			password = in.readLine();
			race = Integer.parseInt(in.readLine());
			if(!playersName.contains(nickname)){
				playersName.add(nickname);
				auth.put(nickname, password);
				players.put(nickname, new Player(race,coordinate,map,playersNo,p_name, nickname,ws,marketplace,allianceRun));
				players.get(nickname).start();
				player = players.get(nickname);
				kayitli++;	
				out.println("Welcome");
				es.add(this);
				break;
			} else{
				System.out.println("!This nickname already taken.");
				out.println("!This nickname already taken.");
			}
		}
		
		
		if(clientGelen.equals("1")){
			nickname = in.readLine();
			password = in.readLine();
			if(auth.containsKey(nickname)){
				
				if(auth.get(nickname).equals(password)){
					out.println("Welcome");
					es.add(this);
					System.out.println(nickname+"+joined the game.");
					player = players.get(nickname);
					break;
				}else{
					out.println("!");
				}
				
				
			} else{
				out.println("!");
			}
			
			}
		}
		
		
		while((clientGelen = in.readLine()) != null){
			System.out.println("Client gelen: " + clientGelen);
			
			if(clientGelen.equals("gtI")){
				System.out.println("gtI'ya girildi.");
				String info="";
				for(int i = 0; i < 8; i++){
					info = info +player.getVillage().resource[i] + "/";
				}
				for(int i = 0; i < 7; i++){
					info = info +player.getVillage().soldiersInVillage[i][0]+"/";
				}
				for(int i = 0; i < 35; i++){
					info = info +player.getVillage().location[i] + "/";
				}
				for(int i = 0; i < 35; i++){
					info = info +player.getVillage().bS[i][0] + "/";
				}
				info = info + player.x+"-"+player.y+"/";//86 köy coordinatlarý
				info = info + player.playerNo+"/";//87 playerNo
				info = info + player.time+"/";//88 time
				for(int i = 0; i < 7; i++){//89909192939495
					info = info +player.getVillage().rb[i]+"/";
				}
				info = info + player.getVillage().nufus+"/";//96
				info = info + player.getVillage().refah+"/";//97
				info = info +player.name +"/";//98
				info = info +player.race +"/";//99
				info = info +player.inAllianceName+"/";//100
				for(int i = 0; i < 9 ; i++){//101-102-103-104-105-106-107-108-109
					info = info + player.getVillage().stuffs[i]+"/";
				}
				info = info+"%";	
				
				
				for(int i = 0; i < player.getVillage().resourcesX.size(); i++){
					info = info+player.getVillage().resourcesX.get(i)+"-";
					info = info+player.getVillage().resources.get(player.getVillage().resourcesX.get(i))[0]+"-";
					info = info+((int)player.getVillage().resources.get(player.getVillage().resourcesX.get(i))[1])+"/";
				}
				info = info+"%";
				
				if(player.comingMarket.size() != 0){
				for(int i = 0; i < player.comingMarket.size();i++){
					info = info+player.comingMarket.get(i)[0] + "-";
					info = info+player.comingMarket.get(i)[1] + "-";
					info = info+player.comingMarket.get(i)[2] + "-";
					info = info+player.comingMarket.get(i)[3] + "-";
					info = info+player.comingMarket.get(i)[4] + "-";
					info = info+player.comingMarket.get(i)[5] + "-";
					info = info+player.p_name.get((int)player.comingMarket.get(i)[0]/10)+"/";
				}}else{
					info = info+"NON";
				}
				
				
				
				info = info+"%U";
				for(int i = 0; i < player.upgrading.size(); i++){
					info = info + player.upgrading.get(i)[0] + "-"+player.upgrading.get(i)[1]+"-"+player.upgrading.get(i)[2]+"/";
					
				}
				out.println("getInfo"+info);
			}
			
			if(clientGelen.equals("map")){
				String cevap = "";
				String playerNo = "";
				for(int i = -10; i < 10; i++){
					for(int k = -10; k < 10; k++){
						if(player.x+i < 0 || player.x +i > 49 || player.y + k < 0 || player.y + k > 49){
							cevap = cevap +0+"/";
						}else{
						cevap = cevap +player.map[player.x+i][player.y+k]+"/";
						if(p_name.containsKey(map[player.x+i][player.y+k])){
							playerNo = playerNo+map[player.x+i][player.y+k]+"-"+p_name.get(map[player.x+i][player.y+k])+"/";
						}
						}
						
					}
				}
				cevap = cevap+playerNo;
				out.println("getMap"+cevap);
			}
			
			if(clientGelen.startsWith("topla")){//0topla/1x/2y
				String cevap = "";
				player.topla(Integer.parseInt(clientGelen.split("/")[1]),Integer.parseInt(clientGelen.split("/")[2]));
				try {
					Thread.sleep(17);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(clientGelen.startsWith("C/")){//C/1bina/2yeri
				System.out.println("Client gelen: " + clientGelen);
				if(clientGelen.split("/").length == 3){
					System.out.println("EchoServer Construct");
					player.Construct(Integer.parseInt(clientGelen.split("/")[1]),Integer.parseInt(clientGelen.split("/")[2]));
				}
			}
			if(clientGelen.startsWith("U")){//U/1hangibina
				System.out.println("Client gelen: " + clientGelen);
				if(clientGelen.split("/").length == 2){
					player.Upgrade(Integer.parseInt(clientGelen.split("/")[1]));
				}
			}
			
			if(clientGelen.startsWith("chat/")){
				System.out.println(clientGelen.substring(5));
				chat.send("["+player.name+"]"+" : "+clientGelen.substring(5));
			}
			
			if(clientGelen.startsWith("pazar")){
				System.out.println(clientGelen);
				String cevap = "";
				String playerNo = "";
				for(Map.Entry<Integer, int[]> e : player.marketplace.get(Integer.parseInt(clientGelen.split("/")[1])).entrySet()){
					int key = e.getKey()/10;
					int[] value = e.getValue();
					System.out.println(key);
					cevap = cevap +key+"-"+value[0]+"-"+value[1]+"-"+value[2]+"-"+value[3]+"-"+value[4]+"/";
					//0-1key/2-satýlancins-3miktar/4istenilencins5miktar
					playerNo = playerNo + key +"-"+player.p_name.get(key)+"/";
					
				}
				
				System.out.println("pazar"+cevap+"%"+playerNo);
				out.println("pazar"+cevap+"%"+playerNo);
				
			}
			
			if(clientGelen.startsWith("buy")){//buy/1hangitür/numarasý
				System.out.println("Client gelen: "+ clientGelen);
				if(clientGelen.split("/").length == 3){
					player.Buy(Integer.parseInt(clientGelen.split("/")[1]),Integer.parseInt(clientGelen.split("/")[2]), players);
				}
			}
			
			if(clientGelen.startsWith("sell")){//sell/1hangimaddeden/2nekadar/3hangimaddeistiyon/4nekadar
				System.out.println("Client gelen: "+ clientGelen);
				if(clientGelen.split("/").length == 5){
					player.Sell(Integer.parseInt(clientGelen.split("/")[1]),Integer.parseInt(clientGelen.split("/")[2]),
							Integer.parseInt(clientGelen.split("/")[3]),Integer.parseInt(clientGelen.split("/")[4]));
				}
			}
			
			if(clientGelen.startsWith("profil")){//profil/1playerNo
				System.out.println("Client gelen: "+clientGelen);
				String cevap = "";
				cevap = cevap + p_name.get(Integer.parseInt(clientGelen.split("/")[1]))+"/"+///adý
				players.get(p_name.get(Integer.parseInt(clientGelen.split("/")[1]))).race +"/"+//ýrký
				players.get(p_name.get(Integer.parseInt(clientGelen.split("/")[1]))).getVillage().nufus+"/"+//nufus
				players.get(p_name.get(Integer.parseInt(clientGelen.split("/")[1]))).getVillage().refah+"/"+//refah
				players.get(p_name.get(Integer.parseInt(clientGelen.split("/")[1]))).inAllianceName+"/"+//alliance
				players.get(p_name.get(Integer.parseInt(clientGelen.split("/")[1]))).points+"/"+//points
				players.get(p_name.get(Integer.parseInt(clientGelen.split("/")[1]))).getVillage().resources.size();//resources
				out.println("profil"+cevap);
			}
			
			
			
			
			
			
			
			
			if(clientGelen.equals("info")){
				String cevap = "";
				if(player.getW_attack().get(0) != null){
					System.out.println("Info 1");
					for(int i = 0; i < player.getW_attack().size(); i++){
						System.out.println(i);
						for(int k = 0; i < player.getW_attack().get(i).length; i++){
						cevap = cevap + player.getW_attack().get(i)[k]+"-";
						}
					}
				
				}
			}
			
			if(clientGelen.equals("infoget")){
				String cevap = "";
				if(player.getW_def().get(0) != null){
					System.out.println("Infoget 1");
					for(int i = 0; i < player.getW_def().size(); i++){
						System.out.println(i);
						for(int k = 0; k < player.getW_def().get(i).length; k++){
						cevap = cevap + player.getW_def().get(i)[k]+"-";
						}
					}
				}
			}
			
			if(clientGelen.equals("gelenAttack")){
				String cevap = "";
				try{
				if(player.getGettingAttack().get(0) !=null){
					System.out.println("gelenAttack 1");
					for(int i=0; i < player.getGettingAttack().size(); i++){
						System.out.println(i);
						for(int k=0; k < player.getGettingAttack().get(i).length; k++){
							cevap = cevap + player.getGettingAttack().get(i)[k]+"-";
						}
					}
				}
			}catch(IndexOutOfBoundsException e){
				
			}
			}
			
			
			
			if(clientGelen.startsWith("X")){
				String cevap = "";
				int[] attack = new int[17];//012345asker; 6tür; 78xy,9101112ham/1314-1516fields
				clientGelen = clientGelen.substring(1);
				System.out.println("Client gelen: " + clientGelen);
				
				if(clientGelen.split("/").length == 17){
					if((int)(player.map[Integer.parseInt(clientGelen.split("/")[7])][Integer.parseInt(clientGelen.split("/")[8])]/10)
							!=0){
					int toplamA = 0;
					for(int m = 0; m < clientGelen.split("/").length - 11; m++){
						
						toplamA = toplamA + Integer.parseInt(clientGelen.split("/")[m]);
					}
					if(toplamA > 0){
						System.out.println("EchoServer 4");
			for(int i = 0; i < clientGelen.substring(1).split("/").length; i++){
				attack[i] = Integer.parseInt(clientGelen.split("/")[i]);
			}
			player.Attack(attack);
				}}
				}
				try {
					Thread.sleep(17);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(clientGelen.startsWith("S")){//012345asker67xy
				String cevap = "";
				int[] support = new int[8];
				clientGelen = clientGelen.substring(1);
				System.out.println("Client gelen: " + clientGelen);
				if(clientGelen.split("/").length == 8){
					
					int toplamA = 0;
					for(int m = 0; m < clientGelen.split("/").length - 2; m++){
						
						toplamA = toplamA + Integer.parseInt(clientGelen.split("/")[m]);
					}
					if(toplamA > 0){
						System.out.println("EchoServer Support");
			for(int i = 0; i < clientGelen.substring(1).split("/").length; i++){
				support[i] = Integer.parseInt(clientGelen.split("/")[i]);
			}}
					player.Support(support);
			}}
			
	
			
			
			
			if(clientGelen.startsWith("R")){//R/1hangiasker/2kaç tane
				System.out.println("Client gelen: " + clientGelen);
				if(clientGelen.split("/").length == 3){
					player.Recruit(Integer.parseInt(clientGelen.split("/")[1]),Integer.parseInt(clientGelen.split("/")[2]));
				}
			}
			if(clientGelen.startsWith("Guc")){//Guc/1hangiasker
				System.out.println("Client gelen: " + clientGelen);
				if(clientGelen.split("/").length == 2){
					player.Guclendirme(Integer.parseInt(clientGelen.split("/")[1]));
				}
			}
			

			
			
			
			if(clientGelen.startsWith("rapor")){//rapor
				String cevap = "";
				System.out.println("Client gelen :"+clientGelen);
				if(clientGelen.split("/").length == 1){
					cevap = player.rapor.get(player.rapor.size()-1);
				}
			}
			
			if(clientGelen.startsWith("union")){//union
				String cevap = "";
				System.out.println("Client gelen :"+clientGelen);
				if(clientGelen.split("/").length == 1){
					cevap = player.inAllianceName;
				}
			}
			
			if(clientGelen.startsWith("createUnion")){//createUnion/1Atlas
				System.out.println("createUnion");
				if(clientGelen.split("/").length == 2){
					player.newAlliance(clientGelen.split("/")[1]);
				}
			}
			if(clientGelen.startsWith("method")){//method/1invitename/2methodnumber/4assignnumber
				System.out.println("method");
				if(clientGelen.split("/").length == 4){
					player.AllianceMethods(clientGelen.split("/")[1],Integer.parseInt(clientGelen.split("/")[2]),
							Integer.parseInt(clientGelen.split("/")[3]));
				}
			}
			if(clientGelen.startsWith("join")){//join/1invitednumber
				System.out.println("join");
				if(clientGelen.split("/").length == 2){
					player.joinAlliance(Integer.parseInt(clientGelen.split("/")[1]));
				}
			}
			
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		out.close();
		in.close();
		socket.close();
		}catch(IOException e){
			client--;
			es.remove(this);
			System.out.println("Çýkýþ Yapýldý!!");
			
		}
	}
}
