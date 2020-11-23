package Core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AllianceRun extends Thread{
	
	public static Map<String,Player> players;
	public static Map<Integer,String> p_name;
	public static Map<String, Alliance> alliances = new HashMap<String, Alliance>();
	public static int[][] map;
	public static Map<Integer, String> a_name = new HashMap<Integer, String>();
	public static ArrayList<ArrayList<int[]>> onWay = new ArrayList<ArrayList<int[]>>();
	
	
	
	public AllianceRun(int[][] map, Map<String,Player> players, Map<Integer,String> p_name){
		this.map = map;
		this.players = players;
		this.p_name = p_name;
	}
	
	
	/*                                    Alliance
	
				@Invite
				@Kick
				@join
				@make War
				@make ceaseFire
				@make union
	*/				
	public void newAlliance(String AllianceName, String name){
			if(!alliances.containsKey(AllianceName)){
				alliances.put(AllianceName, new Alliance(AllianceName,name));
				alliances.get(AllianceName).rapor.add("Say hello to the new Alliance:"+AllianceName);
				int allianceNo;
				while (true) {
					allianceNo = 0 - new Random().nextInt(100000);
					if(!a_name.containsKey(allianceNo)){
						break;
					}
					
				}
				alliances.get(AllianceName).AllianceNo = allianceNo;
				players.get(name).inAlliance = true;
				players.get(name).inAllianceName = AllianceName;
			}
		}
	
	
	public void sentInvit(String name, String invitname, String allianceName){
		boolean enough = true;
		if(!players.get(invitname).inAlliance && !alliances.get(allianceName).invitedPlayers.contains(invitname)){
			if(alliances.get(allianceName).preAssist[0].equals(name) || 
					alliances.get(allianceName).preAssist[1].equals(name) ||
					alliances.get(allianceName).preAssist[2].equals(name)){
				enough = true;
			}else{
		for(int i = 0; i < alliances.get(allianceName).diplomats.size();i++){
			if(name.equals(alliances.get(allianceName).diplomats.get(i))){
				enough = true;
				break;
			}
		}
			}
		
		if(enough){
			players.get(invitname).invited.add(allianceName);
			alliances.get(allianceName).invitedPlayers.add(invitname);
			alliances.get(allianceName).rapor.add(name+" invited the "+invitname+"to the alliance.");
		}
	}
	}
	public void acceptInvit(String name, String allianceName){
		if(alliances.containsKey(allianceName)){
		if(alliances.get(allianceName).invitedPlayers.contains(name)){
			alliances.get(allianceName).players.add(name);
			alliances.get(allianceName).invitedPlayers.remove(name);
			alliances.get(allianceName).rapor.add(name+" accepted the invite and join the Alliance");
			players.get(name).invited.remove(allianceName);
			players.get(name).inAlliance=true;
			players.get(name).inAllianceName = allianceName;
		}
		}
	}
	public void quit(String name, String allianceName){
		alliances.get(allianceName).players.remove(name);
		alliances.get(allianceName).rapor.add(name+" leave up the alliance.");
		players.get(name).inAlliance = false;
		players.get(name).inAllianceName = null;
		if(alliances.get(allianceName).players.isEmpty()){
			alliances.remove(allianceName);
		}else{
		if(alliances.get(allianceName).preAssist[0].equals(name)){
			if(alliances.get(allianceName).preAssist[1] != null){
				alliances.get(allianceName).preAssist[0] = alliances.get(allianceName).preAssist[1];
				alliances.get(allianceName).preAssist[1] = alliances.get(allianceName).preAssist[2];
				alliances.get(allianceName).preAssist[2] = null;
				
			}
		}
		for(int i = 0 ; i < alliances.get(allianceName).diplomats.size(); i++){
			if(alliances.get(allianceName).diplomats.get(i).equals(name)){
				alliances.get(allianceName).diplomats.remove(i);
				break;
			}
		}
		for(int i = 0 ; i < alliances.get(allianceName).Generals.size(); i++){
			if(alliances.get(allianceName).Generals.get(i).equals(name)){
				alliances.get(allianceName).Generals.remove(i);
				break;
			}
		}
		}
	}
	public void kick(String name, String kickname, String allianceName){
			
		/////////////////////////////
		if(alliances.get(allianceName).players.contains(kickname) && 
				!alliances.get(allianceName).preAssist[0].equals(kickname)){
			boolean enough = false;
			if(alliances.get(allianceName).preAssist[0].equals(name) || 
					alliances.get(allianceName).preAssist[1].equals(name) ||
					alliances.get(allianceName).preAssist[2].equals(name)){
				enough = true;
			}else{
			for(int i = 0; i < alliances.get(allianceName).diplomats.size() && i < 5;i++){
				if(alliances.get(allianceName).diplomats.get(i).equals(name)){
					enough = true;
					break;
				}
			}
			}
			
			if(enough && alliances.get(allianceName).preAssist[0].equals(name)){
				alliances.get(allianceName).players.remove(kickname);
				alliances.get(allianceName).rapor.add(name+" kicked "+kickname+" from the alliance.");
				players.get(kickname).inAlliance = false;
				players.get(kickname).inAllianceName = null;
				if(alliances.get(allianceName).preAssist[1].equals(kickname)){
					alliances.get(allianceName).preAssist[1] = "";
				}
				if(alliances.get(allianceName).preAssist[2].equals(kickname)){
					alliances.get(allianceName).preAssist[2] = "";
				}
				if(alliances.get(allianceName).diplomats.contains(kickname)){
					alliances.get(allianceName).diplomats.remove(kickname);
				}
				if(alliances.get(allianceName).Generals.contains(kickname)){
					alliances.get(allianceName).Generals.remove(kickname);
				}
			}else if(enough && alliances.get(allianceName).preAssist[1].equals(name) ||
					enough && alliances.get(allianceName).preAssist[2].equals(name)){
				if(!(enough && alliances.get(allianceName).preAssist[1].equals(kickname) || 
						enough && alliances.get(allianceName).preAssist[2].equals(kickname))){
					alliances.get(allianceName).players.remove(kickname);
					alliances.get(allianceName).rapor.add(name+" kicked "+kickname+" from the alliance.");
					players.get(kickname).inAlliance = false;
					players.get(kickname).inAllianceName = "";
					if(alliances.get(allianceName).diplomats.contains(kickname)){
						alliances.get(allianceName).diplomats.remove(kickname);
					}
					if(alliances.get(allianceName).Generals.contains(kickname)){
						alliances.get(allianceName).Generals.remove(kickname);
					}
				}
			}else if(enough && alliances.get(allianceName).diplomats.contains(name)){
				if(!(alliances.get(allianceName).preAssist[1].equals(kickname) || 
						alliances.get(allianceName).preAssist[2].equals(kickname) ||
						alliances.get(allianceName).diplomats.contains(kickname) ||
						alliances.get(allianceName).Generals.contains(kickname))){
					alliances.get(allianceName).players.remove(kickname);
					alliances.get(allianceName).rapor.add(name+" kicked "+kickname+" from the alliance.");
					players.get(kickname).inAlliance = false;
					players.get(kickname).inAllianceName = "";
				}
			}
		}////////////////////////////////////////
	}
	public void assign(String name, String assignName, String allianceName, int task){//0president/1assistant/2assistant/3diplomats/4generals
		if(alliances.get(allianceName).players.contains(assignName)){
		if(alliances.get(allianceName).preAssist[0].equals(name)){
			switch(task){
			case 0: alliances.get(allianceName).preAssist[0] = assignName;break;
			case 1: alliances.get(allianceName).preAssist[1] = assignName;break;
			case 2: alliances.get(allianceName).preAssist[2] = assignName;break;
			case 3: if(alliances.get(allianceName).diplomats.size() ==5){
				alliances.get(allianceName).diplomats.set(0, assignName);
			}break;
			case 4: if(alliances.get(allianceName).diplomats.size() ==5){
				alliances.get(allianceName).diplomats.set(1, assignName);
			}break;
			case 5: if(alliances.get(allianceName).diplomats.size() ==5){
				alliances.get(allianceName).diplomats.set(2, assignName);
			}break;
			case 6: if(alliances.get(allianceName).diplomats.size() ==5){
				alliances.get(allianceName).diplomats.set(3, assignName);
			}break;
			case 7: if(alliances.get(allianceName).diplomats.size() ==5){
				alliances.get(allianceName).diplomats.set(4, assignName);
			}break;
			case 8: if(alliances.get(allianceName).diplomats.size() <5){
				alliances.get(allianceName).diplomats.add(assignName);
			}break;
			case 9: if(alliances.get(allianceName).Generals.size() ==5){
				alliances.get(allianceName).Generals.set(0, assignName);
			}break;
			case 10: if(alliances.get(allianceName).Generals.size() ==5){
				alliances.get(allianceName).Generals.set(1, assignName);
			}break;
			case 11: if(alliances.get(allianceName).Generals.size() ==5){
				alliances.get(allianceName).Generals.set(2, assignName);
			}break;
			case 12: if(alliances.get(allianceName).Generals.size() ==5){
				alliances.get(allianceName).Generals.set(3, assignName);
			}break;
			case 13: if(alliances.get(allianceName).Generals.size() ==5){
				alliances.get(allianceName).Generals.set(4, assignName);
			}break;
			case 14: if(alliances.get(allianceName).Generals.size() <5){
				alliances.get(allianceName).Generals.add(assignName);
			}break;
			}
		}else if(alliances.get(allianceName).preAssist[1].equals(name) || 
				alliances.get(allianceName).preAssist[1].equals(name)){
			switch(task){
			case 4: if(alliances.get(allianceName).diplomats.size() ==5){
				alliances.get(allianceName).diplomats.set(1, assignName);
			} break;
			case 5: if(alliances.get(allianceName).diplomats.size() ==5){
				alliances.get(allianceName).diplomats.set(2, assignName);
			}break;
			case 6: if(alliances.get(allianceName).diplomats.size() ==5){
				alliances.get(allianceName).diplomats.set(3, assignName);
			}break;
			case 7: if(alliances.get(allianceName).diplomats.size() ==5){
				alliances.get(allianceName).diplomats.set(4, assignName);
			}break;
			case 8: if(alliances.get(allianceName).diplomats.size() <5){
				alliances.get(allianceName).diplomats.add(assignName);
			}break;
			case 9: if(alliances.get(allianceName).Generals.size() ==5){
				alliances.get(allianceName).Generals.set(0, assignName);
			}break;
			case 10: if(alliances.get(allianceName).Generals.size() ==5){
				alliances.get(allianceName).Generals.set(1, assignName);
			}break;
			case 11: if(alliances.get(allianceName).Generals.size() ==5){
				alliances.get(allianceName).Generals.set(2, assignName);
			}break;
			case 12: if(alliances.get(allianceName).Generals.size() ==5){
				alliances.get(allianceName).Generals.set(3, assignName);
			}break;
			case 13: if(alliances.get(allianceName).Generals.size() ==5){
				alliances.get(allianceName).Generals.set(4, assignName);
			}break;
			case 14: if(alliances.get(allianceName).Generals.size() <5){
				alliances.get(allianceName).Generals.add(assignName);
			}break;
			}
		}
		}
	}
	public void attackCommand(String name, String allianceName, ArrayList<int[]> way, int[] soldiers){//soldiers
		if(alliances.get(allianceName).preAssist[0].equals(name) || 
				alliances.get(allianceName).preAssist[1].equals(name) ||
				alliances.get(allianceName).preAssist[2].equals(name) ||
				alliances.get(allianceName).Generals.contains(name)){
			System.out.println("attackCommand 1");
			boolean enough1 = false;
			for(int i = 0 ; i < 6; i++){
				if(!(alliances.get(allianceName).soldiersForAttack[soldiers[0]][i]==soldiers[i+1])){
					enough1 = false;
					break;
				}else{
					enough1 = true;
				}
			}
			boolean enough2 = false;
			for(int i = 0; i < way.size(); i++){
				if(alliances.get(allianceName).withWar.contains(players.get(p_name.get((int)(map[way.get(i)[0]][way.get(i)[1]]/10))).inAllianceName) ||
						alliances.get(allianceName).ally.contains(players.get(p_name.get((int)(map[way.get(i)[0]][way.get(i)[1]])/10)).inAllianceName)){
					enough2 = true;
					break;
				}else{
					enough2 = false;
				}
			}
			boolean enough3 = false;
			for(int i = 0; i < way.size()-1;i++){
				if(Math.abs(way.get(i)[0]-way.get(i+1)[0]) > 1 || Math.abs(way.get(i)[1]-way.get(i+1)[1])>1){
					enough3 = false;
					break;
				}else{
					enough3 = true;
				}
			}
			if(enough1 && enough2&&enough3){
				for(int i = 0; i<6; i++){
					alliances.get(allianceName).soldiersForAttack[soldiers[0]][i] = 0;
				}
				soldiers[7] = alliances.get(allianceName).AllianceNo;
				way.add(soldiers);
				for(int i = 0; i < way.size()-1; i++){
				way.get(0)[2] = (int)(new Date().getTime()/1000) + 60*(i+1);
				}
				alliances.get(allianceName).soldiersOnTheWay.add(way);
				onWay.add(way);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public void WarSimulator(String name, int x, int y,int a){
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(true){
			int time = (int)(new Date().getTime()/1000);
			for(int i = 0; i < onWay.size(); i++){
				if(time > onWay.get(i).get(0)[2]){
					War(onWay,i);
					System.out.println();
				}
			}
		}
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
	}
	
	public void War(ArrayList<ArrayList<int[]>> onWay, int a){
		
		String savunan = players.get(p_name.get(map[onWay.get(a).get(0)[0]][onWay.get(a).get(0)[1]])).name;
		String allianceName = a_name.get(onWay.get(a).get(onWay.get(a).size()-1)[7]);
		
		if(alliances.get(a_name.get(onWay.get(a).get(onWay.size()-1)[7])).
				withWar.contains(players.get(p_name.get(map[onWay.get(a).get(0)[0]][onWay.get(a).get(0)[1]])).inAllianceName)){
			
			int atli_sayisi_S = 0;
			int yaya_sayisi_S = 0;
			int atli_vurus = 0;
			int yaya_vurus = 0;
			int atli_sayisi_D = 0;
			int yaya_sayisi_D = 0;
			int atli_def = 0;
			int yaya_def = 0;
			int[] olen_S = new int[6];
			int[] olen_D = new int[7];
			int[] kalan_D = new int[7];
			String rapor = "";
			
			int[] saldýrý = new int[6];
			for(int i = 1; i < 7; i++){
				saldýrý[i-1] = onWay.get(a).get(onWay.size()-1)[i];
			}
			
			
			
			
			int[][] soldiers_A = new int[7][12];
			int[][] soldiers_D = new int[7][12];
			switch(onWay.get(a).get(onWay.get(a).size()-1)[0]){
			case 0: soldiers_A = (new Soldiers()).sldr.get(0);
			}
			switch(players.get(p_name.get(map[onWay.get(a).get(0)[0]][onWay.get(a).get(0)[1]])).race){
			case 0: soldiers_D = (new Soldiers()).sldr.get(0);
			}
			atli_sayisi_S = saldýrý[3]+saldýrý[4]+saldýrý[5];
			yaya_sayisi_S = saldýrý[0]+saldýrý[1]+saldýrý[2];
			atli_vurus = saldýrý[0]*soldiers_A[1][1]+
					saldýrý[1]*soldiers_A[2][1]+
					saldýrý[2]*soldiers_A[3][1]+
					saldýrý[3]*soldiers_A[4][1]+
					saldýrý[4]*soldiers_A[5][1]+
					saldýrý[5]*soldiers_A[6][1];
			yaya_vurus = saldýrý[0]*soldiers_A[1][0]+
					saldýrý[1]*soldiers_A[2][0]+
					saldýrý[2]*soldiers_A[3][0]+
					saldýrý[3]*soldiers_A[4][0]+
					saldýrý[4]*soldiers_A[5][0]+
					saldýrý[5]*soldiers_A[6][0];
			
			atli_sayisi_D = players.get(savunan).getVillage().soldiersInVillage[4][0]+
					players.get(savunan).getVillage().soldiersInVillage[5][0]+
							players.get(savunan).getVillage().soldiersInVillage[6][0];
			yaya_sayisi_D = players.get(savunan).getVillage().soldiersInVillage[0][0]+
					players.get(savunan).getVillage().soldiersInVillage[1][0]+
					players.get(savunan).getVillage().soldiersInVillage[2][0]+
							players.get(savunan).getVillage().soldiersInVillage[3][0];
			atli_def =(int)(players.get(savunan).getVillage().soldiersInVillage[0][0]*soldiers_D[0][3]*(100 + players.get(savunan).getVillage().soldiersInVillage[0][1])/100)+ 
					(int)(players.get(savunan).getVillage().soldiersInVillage[1][0]*soldiers_D[1][3]*(100 + players.get(savunan).getVillage().soldiersInVillage[1][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[2][0]*soldiers_D[2][3]*(100 + players.get(savunan).getVillage().soldiersInVillage[2][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[3][0]*soldiers_D[3][3]*(100 + players.get(savunan).getVillage().soldiersInVillage[3][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[4][0]*soldiers_D[4][3]*(100 + players.get(savunan).getVillage().soldiersInVillage[4][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[5][0]*soldiers_D[5][3]*(100 + players.get(savunan).getVillage().soldiersInVillage[5][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[6][0]*soldiers_D[6][3]*(100 + players.get(savunan).getVillage().soldiersInVillage[5][1])/100);
			atli_def = (int)(atli_def * (100 + players.get(savunan).getVillage().bS[29][0])/100);
			yaya_def =(int)(players.get(savunan).getVillage().soldiersInVillage[0][0]*soldiers_D[0][2]*(100 + players.get(savunan).getVillage().soldiersInVillage[0][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[1][0]*soldiers_D[1][2]*(100 + players.get(savunan).getVillage().soldiersInVillage[1][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[2][0]*soldiers_D[2][2]*(100 + players.get(savunan).getVillage().soldiersInVillage[2][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[3][0]*soldiers_D[3][2]*(100 + players.get(savunan).getVillage().soldiersInVillage[3][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[4][0]*soldiers_D[4][2]*(100 + players.get(savunan).getVillage().soldiersInVillage[4][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[5][0]*soldiers_D[5][2]*(100 + players.get(savunan).getVillage().soldiersInVillage[5][1])/100)+
					(int)(players.get(savunan).getVillage().soldiersInVillage[6][0]*soldiers_D[6][2]*(100 + players.get(savunan).getVillage().soldiersInVillage[6][1])/100);
			yaya_def = (int)(yaya_def * (100 + players.get(savunan).getVillage().bS[29][0])/100);
	
			int savunan_def = (int)(atli_def*atli_sayisi_S +yaya_def*yaya_sayisi_S)/(atli_sayisi_S+yaya_sayisi_S);
			int saldýran_vurus = 0;
			if(savunan_def != 0){
			saldýran_vurus = (int)(atli_vurus*atli_sayisi_D + yaya_vurus*yaya_sayisi_D)/(atli_sayisi_D+yaya_sayisi_D);
			}
			///Savunan Yok
			if(savunan_def == 0){
				for(int i=0; i < kalan_D.length-1;i++){
					olen_S[i] = 0;
					olen_D[i] = 0;
					kalan_D[i] = 0;
					saldýrý[i] = saldýrý[i]-olen_S[i];
					System.out.println("Savunan Olmadý: "+olen_S[i]+"/");
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				rapor = allianceName+", Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+saldýrý[rpr]+"|"+olen_S[rpr]+"--";
				}
				rapor = rapor + savunan+", Kalan|Olen ";
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				alliances.get(allianceName).rapor.add(rapor);
				for(int i = 0 ; i < alliances.get(allianceName).soldiersOnTheWay.size(); i++){
					if(alliances.get(allianceName).soldiersOnTheWay.get(i).equals(onWay.get(a))){
						alliances.get(allianceName).soldiersOnTheWay.get(i).remove(0);
						break;
					}
				}
				alliances.get(players.get(savunan).inAllianceName).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
			
			//Saldýran kazandý
			if(saldýran_vurus > (int)(savunan_def*105)/100 && savunan_def != 0){
	
				for(int i=0; i < kalan_D.length-1;i++){
					float katsayi = ((float)8/10)*(((float)savunan_def)/saldýran_vurus);
					System.out.print(katsayi);
					olen_S[i] = (int) Math.floor(saldýrý[i]*katsayi);
					saldýrý[i] = saldýrý[i]-olen_S[i]; 
					olen_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0];
					kalan_D[i] = 0;
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					System.out.println("Saldýran Kazandý: Olen/Kalan "+olen_S[i]+"/"+saldýrý[i]);
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
			
				rapor = allianceName+", Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor =rapor+ " "+saldýrý[rpr]+"|"+olen_S[rpr]+"--";
				}
				rapor = rapor + savunan+", Kalan|Olen";
				for(int rpr = 0; rpr < 7; rpr++){
					rapor =rapor+ " "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				alliances.get(allianceName).rapor.add(rapor);
				for(int i = 0 ; i < alliances.get(allianceName).soldiersOnTheWay.size(); i++){
					if(alliances.get(allianceName).soldiersOnTheWay.get(i).equals(onWay.get(a))){
						alliances.get(allianceName).soldiersOnTheWay.get(i).remove(0);
						break;
					}
				}
				alliances.get(players.get(savunan).inAllianceName).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
			
			///Berabere
			if(saldýran_vurus <= (int)(savunan_def*105)/100 && saldýran_vurus >= (int)(savunan_def*95)/100 && saldýran_vurus !=0){
				
				for(int i=0; i < kalan_D.length-1;i++){
					olen_S[i] = saldýrý[i];
					olen_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0];
					saldýrý[i] = 0;
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					System.out.println("Berabere: "+olen_S[i]+"/");
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				rapor = allianceName+", Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+saldýrý[rpr]+"|"+olen_S[rpr]+"--";
				}
				rapor = savunan+", Kalan|Olen";
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				alliances.get(allianceName).rapor.add(rapor);
				for(int i = 0 ; i < alliances.get(allianceName).soldiersOnTheWay.size(); i++){
					if(alliances.get(allianceName).soldiersOnTheWay.get(i).equals(onWay.get(a))){
						alliances.get(allianceName).soldiersOnTheWay.remove(i);
						break;
					}
				}
				alliances.get(players.get(savunan).inAllianceName).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
				
			}
			
			
			
			
			//Savunan kazandý.
			if(saldýran_vurus < (int)(savunan_def*95)/100){
				
				for(int i=0; i < kalan_D.length-1;i++){
					float katsayi = ((float)8/10)*(((float)saldýran_vurus)/savunan_def);
					System.out.print(katsayi);
					olen_S[i] = saldýrý[i];
					olen_D[i] = (int) Math.floor(players.get(savunan).getVillage().soldiersInVillage[i+1][0]*katsayi);
					kalan_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0]-olen_D[i];
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					System.out.println("Savunan Kazandý. Olen/Kalan Def: "+olen_D[i]+"/"+kalan_D[i]);
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				rapor = allianceName+", Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+saldýrý[rpr]+"|"+olen_S[rpr]+"--";
				}
				rapor = rapor+savunan+", Kalan|Olen";
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				alliances.get(allianceName).rapor.add(rapor);
				for(int i = 0 ; i < alliances.get(allianceName).soldiersOnTheWay.size(); i++){
					if(alliances.get(allianceName).soldiersOnTheWay.get(i).equals(onWay.get(a))){
						alliances.get(allianceName).soldiersOnTheWay.remove(i);
						break;
					}
				}
				alliances.get(players.get(savunan).inAllianceName).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
		
		
		
		}else{
			for(int i = 0; i < alliances.get(allianceName).soldiersOnTheWay.size(); i++){
				if(alliances.get(allianceName).soldiersOnTheWay.get(i).equals(onWay.get(a))){
					alliances.get(allianceName).soldiersOnTheWay.remove(i);
				}
			}
			onWay.get(a).remove(0);
		}
	
		
}
}