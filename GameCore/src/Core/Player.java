package Core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Player extends Thread{

	
	//Date
	public int time;
	public int date;
	public int recruitTime;
	
	//Specifity
	
	//Trade
	public int[] ftrade = {0,0,0};
	
	//Reports
	public ArrayList<String> rapor = new ArrayList<>(); 
	
	//Alliance
	public boolean inAlliance = false;
	public String inAllianceName = " ";
	public List<String> invited = new ArrayList<>();
	
	//PlayerRank
	public int points = 700;
	
	public int[][] soldier;
	public int[] extraResource;
	public float sldrSpeed;
	public int extraSlot;
	public int extraCranny;
	public int yayaatli;
	
	//Player Specification
	public int[] forrand = {2,7,12,17,22,27,32,37,42,47};
	public int x;
	public int y;
	public int playerNo;
	public String name;
	public int race = -1;
	public int ssayisi = 0;
	public int dsayisi = 0;
	
	//Village Resources
	private Village village = new Village();
	private ArrayList<SmallTown> smallTown = new ArrayList<>();
	
	//attacks going from your village
	private ArrayList<int[]> w_attack = new ArrayList<int[]>();
	//def coming to the village your soldiers
	private ArrayList<int[]> w_def = new ArrayList<int[]>();
	//coming attack
	private ArrayList<String[]> gettingAttack = new ArrayList<>();
	//def to friends
	private ArrayList<int[]> goingToSupport = new ArrayList<>();
	
	//upgrading
	public ArrayList<int[]> upgrading = new ArrayList<>();
	//guclendirme
	public ArrayList<int[]> guclendirme = new ArrayList<>();
	//Gadgets
	public ArrayList<int[]> gadgets = new ArrayList<>();
	//Recruiting
	private int[] recruit = {0,0,0,0,0,0,0,0,0,0,0,0};//number,type,time
	//Marketplace
	public ArrayList<int[]> comingMarket = new ArrayList<>();
	
	public static WarSimulator ws;
	public static Map<Integer,String> p_name;
	public static int[][] map;
	public static ArrayList<HashMap<Integer,int[]>> marketplace;
	public static AllianceRun allianceRun;
	
	public Player(int race, int[] coordinate,int[][] map, ArrayList<Integer> playersNo,
			Map<Integer,String> p_name, String nickname, WarSimulator ws,
			ArrayList<HashMap<Integer,int[]>> marketplace,AllianceRun allianceRun){
		this.ws = ws;
		this.p_name = p_name;
		this.map = map;
		this.race = race;
		this.marketplace = marketplace;
		this.allianceRun = allianceRun;
		name = nickname;
		//Race qualifications.
		
		Soldiers sldr = new Soldiers();
		soldier = sldr.sldr.get(race);
		extraResource = sldr.extraResource;
		sldrSpeed = sldr.sldrSpeed;
		extraSlot = sldr.extraSlot;
		extraCranny = sldr.extraCranny;
		yayaatli = sldr.yayaatli;
		
		

		
		
		
		
		//Coordinate ve specifik numara veriliþi
		while(true){
			playerNo = (new Random()).nextInt(90000)+10000;
			if(!playersNo.contains(playerNo)){
				playersNo.add(playerNo);
				while(true){
					x = forrand[(new Random()).nextInt(10)];
					y = forrand[(new Random()).nextInt(10)];
					if(map[x][y]==0){
						map[x][y] = playerNo;
						p_name.put(playerNo, nickname);
						takeRFields();
						
						break;
					}
				}
				break;
			}
		}
		
	}
	
	@Override
	public void run(){
		super.run();
		time = (int)(new Date().getTime()/1000);
		
		while(true){
			
			updateFields();
			if(!upgrading.isEmpty()){
				if(time >= upgrading.get(0)[1]){
					village.bS[upgrading.get(0)[0]][0] = village.bS[upgrading.get(0)[0]][0]+1;
					village.nufus = village.nufus + village.bS[upgrading.get(0)[0]][0]*village.bN[upgrading.get(0)[0]][9];
					Nufus();
					BinaEtkileri(upgrading.get(0)[0]);
					System.out.println("uPGRADED: "+village.bS[upgrading.get(0)[0]][0]);
					upgrading.remove(0);
					
				}
			}
			for(int i = 0; i < yayaatli;i=i+2){//þurayý race e ayarla
				if(recruit[i+1]>0){
					if(time>=recruitTime){
						village.soldiersInVillage[recruit[i]][0]++;
						recruit[i+1]--;
						break;
					}
				}
			}
			
			for(int i = yayaatli; i < 10;i=i+2){//þurayý race e ayarla
				if(recruit[i+1]>0){
					if(time>=recruitTime){
						village.soldiersInVillage[recruit[i]][0]++;
						recruit[i+1]--;
						break;
					}
				}
			}
			
			if(!guclendirme.isEmpty()){
				if(time >= guclendirme.get(0)[1]){
					village.guclendirme[guclendirme.get(0)[0]]++;
					guclendirme.remove(0);
					updateSBonus();
					System.out.println("Güçlendirildi.");
				}
			}
			
			if(!gadgets.isEmpty()){
				if(time >= gadgets.get(0)[1]){
					village.gadgets[gadgets.get(0)[0]]++;
					updateBonus();
					updateSBonus();
					gadgets.remove(0);
				}
			}
			
			for(int i = 0; i < comingMarket.size(); i++){
				if(time >= comingMarket.get(i)[5]){
					village.resource[comingMarket.get(i)[1]] = village.resource[comingMarket.get(i)[1]] + comingMarket.get(i)[2];
					comingMarket.remove(i);
					i--;
				}
			}
			
			for(int i = 0; i < w_attack.size(); i++){
				System.out.println(w_attack.get(i)[0]+"/"+i);
			}
			for(int i = 0; i < w_def.size(); i++){
				System.out.println(w_def.get(i)[0]+"/"+i);
			}
			for(int i = 0; i < goingToSupport.size(); i++){
				System.out.println(goingToSupport.get(i)[0]+"/"+i);
			}
			for(int i = 0; i < gettingAttack.size(); i++){
				System.out.println(gettingAttack.get(i)[0]+"/"+i);
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//Get resources in fields to village
	public void topla(int x, int y){
		if(village.resources.containsKey(""+x+","+y)){
		try{
		village.resource[(int)village.resources.get(""+x+","+y)[0]-1] = village.resource[(int)village.resources.get(""+x+","+y)[0]-1]+
				(int)village.resources.get(""+x+","+y)[1];
		if((int)village.resources.get(""+x+","+y)[0]<5 || (int)village.resources.get(""+x+","+y)[0] ==7){
			if(village.resource[(int)village.resources.get(""+x+","+y)[0]-1] > village.depo[0]){
				village.resource[(int)village.resources.get(""+x+","+y)[0]-1] = village.depo[0];
			}
		}
		if((int)village.resources.get(""+x+","+y)[0]==5){
				if(village.resource[(int)village.resources.get(""+x+","+y)[0]-1] > village.depo[1]){
					village.resource[(int)village.resources.get(""+x+","+y)[0]-1] = village.depo[1];
				}
			}
		if((int)village.resources.get(""+x+","+y)[0]==6){
				if(village.resource[(int)village.resources.get(""+x+","+y)[0]-1] > village.depo[2]){
					village.resource[(int)village.resources.get(""+x+","+y)[0]-1] = village.depo[2];
				}
			}
		System.out.println(village.resources.get(""+x+","+y)[1]);
		village.resources.get(""+x+","+y)[1] = 0;
		}catch(NullPointerException e){
			;
		}
		}else{
			for(int i = 0; i < smallTown.size();i++){
				if(smallTown.get(i).resources.containsKey(""+x+","+y)){
					try{
						village.resource[(int)smallTown.get(i).resources.get(""+x+","+y)[0]-1] = village.resource[(int)smallTown.get(i).resources.get(""+x+","+y)[0]-1]+
								(int)smallTown.get(i).resources.get(""+x+","+y)[1];
						if((int)village.resources.get(""+x+","+y)[0]<5 || (int)village.resources.get(""+x+","+y)[0] ==7){
							if(village.resource[(int)village.resources.get(""+x+","+y)[0]-1] > village.depo[0]){
								village.resource[(int)village.resources.get(""+x+","+y)[0]-1] = village.depo[0];
							}
						}
						if((int)village.resources.get(""+x+","+y)[0]==5){
								if(village.resource[(int)village.resources.get(""+x+","+y)[0]-1] > village.depo[1]){
									village.resource[(int)village.resources.get(""+x+","+y)[0]-1] = village.depo[1];
								}
							}
						if((int)village.resources.get(""+x+","+y)[0]==6){
								if(village.resource[(int)village.resources.get(""+x+","+y)[0]-1] > village.depo[2]){
									village.resource[(int)village.resources.get(""+x+","+y)[0]-1] = village.depo[2];
								}
							}
						System.out.println(smallTown.get(i).resources.get(""+x+","+y)[1]);
						smallTown.get(i).resources.get(""+x+","+y)[1] = 0;
						}catch(NullPointerException e){
							;
						}
				}
			}
		}
	}
	
	//Resource fields update
	public void takeRFields(){
		
		//Townhall
			//Burada eðer ki village.resourceX.size deðiþmezse problem yaþanabilir.
		int level = 0;
			for(int i = 0; i < village.bS[8][0]+1; i++){
				if(i%5 == 0){
					level++;
				}
			}
			for(int i = -1; i < level+1; i++){
				for(int m = -1; m < level+1; m++){
					int xf = x+i, yf = y+m;
					if(village.resources.containsKey(""+xf+","+yf)){
						//Do nothing.
					} else {
						if((int)(map[xf][yf]/10) == 0){
							village.resourcesX.add(""+xf+","+yf);
							float[] deger = {map[xf][yf],0};
							village.resources.put(""+xf+","+yf, deger);
							map[xf][yf] = playerNo*10 + map[xf][yf];
							System.out.println(xf+" "+yf+" "+map[xf][yf]);
						}
					}
				}
			}
		
	}
	public void checkRFields(int[][] map,int x,int y){
		int level = 0;
		for(int i = 0; i < village.bS[8][0]; i++){
			if(i%5 == 0){
				level++;
			}
		}
		for(int i = 0; i < village.resourcesX.size(); i++){
			int c_x = Integer.parseInt(village.resourcesX.get(i).split(",")[0]);
			int c_y = Integer.parseInt(village.resourcesX.get(i).split(",")[1]);
			if((c_x - x)>level || (c_y-y)>level){
				village.resources.remove(""+c_x+","+c_y);
				village.resourcesX.remove(i);
				i--;
			}
		}
	
	}
	
	//Fields resource update
	public void updateFields(){
		date =(int)(new Date().getTime()/1000);
		
		for(int i=0; i< village.resources.size();i++){
			if(village.resources.get(village.resourcesX.get(i))[1] < 600){
			village.resources.get(village.resourcesX.get(i))[1] = 
					village.resources.get(village.resourcesX.get(i))[1] +
					(float)village.rb[(int)village.resources.get(village.resourcesX.get(i))[0]-1]/3600*(date-time);
			}
		}
		for(int k = 0; k < smallTown.size();k++){
			for(int i=0; i< smallTown.get(k).resources.size();i++){
				if(smallTown.get(k).resources.get(smallTown.get(k).resourcesX.get(i))[1] < 600){
					smallTown.get(k).resources.get(smallTown.get(k).resourcesX.get(i))[1] = 
							smallTown.get(k).resources.get(smallTown.get(k).resourcesX.get(i))[1] +
						(float)village.rb[(int)smallTown.get(k).resources.get(smallTown.get(k).resourcesX.get(i))[0]-1]/7200*(date-time);
				}
			}
		}
		time = date;
	}
	
	public void updateBonus(){
		for(int i = 0; i < 2; i++){
			village.rb[i] = (int)((float)(village.rbG[i]+village.bS[i][0]*24)*(100 + village.bS[16][0]*2+village.bS[23][0]*2+(int)village.refah/5)/100);
		}
		for(int i = 3; i < 6; i++){
			village.rb[i] = (int)((village.rbG[i]+village.bS[i][0]*24)*(100 + village.bS[16][0]*2+village.bS[23][0]*2+(int)village.refah/5)/100);
		}
		village.rb[2] = (int)((village.rbG[2]+village.bS[2][0]*24)*(100 + village.bS[17][0]*2+village.bS[23][0]*2+(int)village.refah/5)/100);
		for(int i = 6; i < 7; i++){
			village.rb[i] = (int)((village.rbG[i]+village.bS[i][0]*24)*(100 + village.bS[17][0]*2+village.bS[23][0]*2+(int)village.refah/5)/100);
		}
		village.rb[0] = (int)(village.rb[0]*(100+village.gadgets[7]*2)/100);
		village.rb[5] = (int)(village.rb[5]*(100+village.gadgets[6]*5)/100);
		village.rb[6] = (int)(village.rb[6]*(100+village.gadgets[8]*5)/100);
	}
	public void updateSBonus(){
		for(int i = 0; i < 3; i++){
			village.soldiersInVillage[i][1] = village.guclendirme[i]*2 + village.gadgets[1]*5+village.gadgets[3]*10+village.gadgets[4]*3;
		}
		for(int i = 3; i < 7; i++){
			village.soldiersInVillage[i][1] = village.guclendirme[i]*2 + village.gadgets[2]*5+village.gadgets[3]*10;
		}
		
	}
	public void Construct(int a,int b){//hangi bina, location
		System.out.println("Constructa girildi.");
		boolean enough = false, enough2 = false;
		for(int i = 0; i < 8; i++){
			if(!(village.resource[i] >= village.bN[a][i])){
				System.out.println("If 1");
				enough = false;
				break;
			} else{
				enough = true;
			}
		}
		for(int i = 0; i < upgrading.size(); i++){
			if(upgrading.get(i)[0] == a){
				System.out.println("if 2");
				enough2 = false;
				break;
			} else{
				enough2 = true;
			}
		}
		if(upgrading.size() == 0){
			enough2 = true;
		}
		System.out.println("Constructa girildi.");
		if(enough ==true && enough2 &&village.location[b] == 0 && CheckC(a)){
			System.out.println("Construct enough!");
			int datee;
			if(!upgrading.isEmpty()){
				datee = upgrading.get(upgrading.size()-1)[1] + village.bN[a][8];
			}else{
				datee = (int)(new Date().getTime()/1000) + village.bN[a][8];
			}
			
			for(int i = 0 ; i< 8 ; i++){
				village.resource[i] = village.resource[i]- village.bN[a][i];
			}
			int[] up = new int[3];
			up[0] = a; up[1] = datee; up[2]=b;
			village.location[b] = a+1;
			upgrading.add(up);
		}
	}
	public void iptalC(int a){
		float give = (upgrading.get(a)[1]-time)/village.bN[upgrading.get(a)[0]][8];
		for(int i =0; i < 8; i++){
			village.resource[i] = village.resource[i] + (int)(village.bN[upgrading.get(a)[0]][i]*give);
		}
		village.location[upgrading.get(a)[2]] = 0;
		upgrading.remove(a);
		 
	}
	public void Upgrade(int upgrade){//hangi bina
		System.out.println("Upgrade 1");
		boolean enough = false;
		int ekUpgrade = 0;
		if(village.bS[upgrade][0] != 0){
			System.out.println("Upgrade 2");
			if(!upgrading.isEmpty()){
				for(int i = 0; i < upgrading.size(); i++){
					if(upgrading.get(i)[0] == upgrade){
						ekUpgrade++;
					}
				}
			}
		for(int i = 0; i < 8; i++){
			if(!(village.resource[i] >= (int)(float)village.bN[upgrade][i]*(1+(float)(village.bS[upgrade][0]+ekUpgrade+1)*
					(village.bS[upgrade][0]+ekUpgrade)/4))){
				System.out.println("Upgrade 2.5");
				enough = false;
				break;
			} else{
				enough = true;
			}
		}
		if(enough == true && CheckU(upgrade)){
			System.out.println("Upgrade 3");
			int datee;
			if(!upgrading.isEmpty()){
				datee = upgrading.get(upgrading.size()-1)[1] +
						(int)(float)(village.bN[upgrade][8]*(1+(float)(village.bS[upgrade][0]+ekUpgrade+1)*
								(village.bS[upgrade][0]+ekUpgrade)/4)*(100-village.bS[8][0]*2)/100);
				
			} else{
				datee = (int)(new Date().getTime()/1000) +
						(int)(float)(village.bN[upgrade][8]*(1+(float)(village.bS[upgrade][0]+ekUpgrade+1)*
								village.bS[upgrade][0]/4)*(100+village.bS[8][0]*2)/100);
			}
			
			for(int i = 0; i < 8; i++){
				village.resource[i] =  (int) (village.resource[i]- (int)(float)village.bN[upgrade][i]*(1+(float)(village.bS[upgrade][0]+ekUpgrade+1)*
						(village.bS[upgrade][0]+ekUpgrade)/4));
			}
		 
		int[] up = new int[3];
		up[0] = upgrade; up[1] = datee; up[2] = -1;
		upgrading.add(up);
		}
		}
	}
	public void iptalU(int a){
		float give = (upgrading.get(a)[1] - time)/(int)(village.bN[upgrading.get(a)[0]][8]*(village.bS[upgrading.get(a)[0]][0]*
				village.bS[upgrading.get(a)[0]][0]/4)*(100+village.bS[8][0]*2)/100);
		for(int i = 0; i < 8; i++){
			village.resource[i] = village.resource[i] + (int)(village.bN[upgrading.get(a)[0]][i]*give);
		}
	}
	public boolean CheckC(int a){
		boolean enough = false;
		switch(a){
		case 0: enough = true;break;
		case 1: enough = true;break;
		case 2: enough = true;break;
		case 3: enough = true;break;
		case 4: enough = true;break;
		case 5: enough = true;break;
		case 6: enough = true;break;
		case 7: enough = true;break;
		case 8: enough = true;break;
		case 11: if(village.bS[8][0] > 0)enough = true; break;//marketplace
		case 12: if(village.bS[8][0]>2)enough = true;break;//school
		case 13: if(village.bS[12][0]>4)enough = true;break;//akademi
		case 14: if(village.bS[12][0]>4)enough = true;break;//college
		case 15: if(village.bS[13][0]>4)enough = true;break;//MS
		case 16: if(village.bS[14][0]>4)enough = true;break;//productinS
		case 17: if(village.bS[14][0]>9)enough = true;break;//mining
		case 18: enough = true;break;
		case 19: enough = true;break;
		case 20: if(village.bS[15][0]==20 && village.bS[27][0]>0)enough = true;break;//warfactory
		case 21: if(village.bS[15][0]>4)enough = true;break;//practiceRange
		case 22: if(village.bS[15][0]>9)enough = true;break;//watchtower
		case 23: if(village.bS[8][0]==20)enough = true;break;//threasury
		case 24: if(village.bS[14][0]==20 && village.bS[27][0]>0)enough = true;break;//TechnoCenter
		case 25: enough = true;break;
		case 26: if(village.bS[13][0]>9)enough = true;break;//UnionDepot
		case 27: if(village.bS[14][0]>9)enough = true;break;//AR-GECenter
		case 28: if(village.bS[22][0]>9 && village.bS[13][0]>9)enough = true;break;//SecretService
		case 29: enough = true;break;//citywall
		case 30: enough = true;break;//Warehouse
		case 31: if(village.bS[8][0] >= 25) enough = true;break;//saray
		default: enough = false;break;
		}
		return enough;
	}
	public boolean CheckU(int a){
		boolean enough = false;
		switch(a){
		case 0: if(village.bS[8][0] > village.bS[0][0])enough = true;break;
		case 1: if(village.bS[8][0]> village.bS[1][0])enough = true;break;
		case 2: if(village.bS[8][0]> village.bS[2][0])enough = true;break;
		case 3: if(village.bS[8][0]> village.bS[3][0])enough = true;break;
		case 4: if(village.bS[8][0]> village.bS[4][0])enough = true;break;
		case 5: if(village.bS[8][0]> village.bS[5][0])enough = true;break;
		case 6: if(village.bS[8][0]> village.bS[6][0])enough = true;break;
		case 7: if(village.bS[8][0]> village.bS[7][0])enough = true;break;
		case 8: if(20>village.bS[8][0])enough = true;break;//townhall
		case 9:	if(20>village.bS[9][0])enough = true;break;//shelter
		case 10: if(20>village.bS[10][0])enough = true;break;//Ecenter
		case 11: if(10>village.bS[11][0])enough = true;break;//marketplace
		case 12: if(5>village.bS[12][0])enough = true;break;//school
		case 13: if(10>village.bS[13][0])enough = true;break;//akademi
		case 14: if(20>village.bS[14][0])enough = true;break;//college
		case 15: if(20>village.bS[15][0])enough = true;break;//MS
		case 16: if(10>village.bS[16][0])enough = true;break;//productinS
		case 17: if(10>village.bS[17][0])enough = true;break;//mining
		case 18: if(20>village.bS[18][0])enough = true;break;//barrack
		case 19: if(20>village.bS[19][0])enough = true;break;//cavalry
		case 20: if(10>village.bS[20][0])enough = true;break;//warfactory
		case 21: if(20>village.bS[21][0])enough = true;break;//practiceRange
		case 22: if(10>village.bS[22][0])enough = true;break;//watchtower
		case 23: if(20>village.bS[23][0])enough = true;break;//threasury
		case 24: if(10>village.bS[24][0])enough = true;break;//TechnoCenter
		case 25: if(5>village.bS[25][0])enough = true;break;//Embassy
		case 26: if(10>village.bS[26][0])enough = true;break;//UnionDepot
		case 27: if(6>village.bS[27][0])enough = true;break;//AR-GECenter
		case 28: if(50>village.bS[28][0])enough = true;break;//SecretService
		case 29: if(20>village.bS[29][0])enough = true;break;//CityWall
		case 30: if(30>village.bS[30][0])enough =true;break;//Warehouse
		case 31: if(50>village.bS[31][0])enough =true;break;//Saray
		}
		return enough;
	}
	public void Recruit(int a,int b){
		System.out.println("Recruit 1");
		boolean enough = false;
		for(int i = 0; i < 7;i++){
			if( !((int)village.resource[i] >= soldier[a][i+4]*b)){
				System.out.println("Recruit 1.5");
				enough = false;
				break;
			} else{enough = true;}
		}
		if(enough&&CheckR(a)){
			System.out.println("Recruit 2");
			for(int i = 0; i < 5; i++){
				village.resource[i] = village.resource[i] - soldier[a][i+4]*b;
			}
			recruit[0] = a;
			recruit[1] = b;
			recruit[2] = soldier[a][14];
			recruitTime = (int)(new Date().getTime()/1000) + recruit[a+1];
		}
	}
	public boolean CheckR(int a){
		boolean enough = false;
		switch(a){
		case 1: if(village.bS[18][0] >= 1){enough=true;break;}
		case 2: if(village.bS[18][0] >= 3){enough=true;break;}
		case 3: if(village.bS[18][0] >= 5){enough=true;break;}
		case 4: if(race ==1 || race==2){
			if(village.bS[19][0]>=1){enough=true;break;}
		}else{
			if(village.bS[18][0]>=10){enough=true;break;}
		}
		case 5: if(village.bS[19][0] >=5){enough=true;break;}
		case 6: if(village.bS[19][0] >=10){enough=true;break;}
		}
		return enough;
	}
	
	public void Attack(int[] attack){
		if(!p_name.get(map[attack[7]][attack[8]]).equals(name)){
		System.out.println("Attack 1");
		boolean enough = false;
		for(int i =0; i < attack.length-11; i++){
			if(!(village.soldiersInVillage[i+1][0] >= attack[i])){
				//Yeterli asker yok.
				System.out.println("Attack 2");
				enough = false;
				break;
			} else{
				enough = true;
			}
		}
		
		if(enough == true){
			System.out.println("Attack 3");
			int speed = 100;
			for(int k = 0; k < 6; k++){
				if( attack[k] > 0){
					if(speed > soldier[k+1][12]){
						speed = soldier[k+1][12];
					}
				}
			}
			int time = 10;
			System.out.println(time);
			//123456asker; 7saldýrýtipivezaman; 8xy(düþman); 9xy(sen); 10ýrkvekacýncýsaldýrý; 11ham;12-13kaynak 
			int[][] newAttack = {{attack[0],village.soldiersInVillage[1][1]},{attack[1],village.soldiersInVillage[2][1]},
					{attack[2],village.soldiersInVillage[3][1]},
					{attack[3],village.soldiersInVillage[4][1]},{attack[4],village.soldiersInVillage[5][1]},
					{attack[5],village.soldiersInVillage[6][1]},{attack[6],time},{attack[7],attack[8]},{x,y},
					{race,ssayisi},{attack[9],attack[10],attack[11],attack[12]},{attack[13],attack[14]},
					{attack[15],attack[16]}};
			ssayisi++;
			ws.WarSimulator(name, p_name.get(map[attack[7]][attack[8]]), newAttack);
		}
		}
	}
	public void Support(int[] support){//123456asker ; 78friendxy
		System.out.println("Support 1");
		boolean enough = false;
		for(int i =0; i < support.length-2; i++){
			if(!(village.soldiersInVillage[i+1][0] >= support[i])){
				//Yeterli asker yok.
				System.out.println("Support 2");
				enough = false;
				break;
			} else{
				enough = true;
			}
		}
		if(enough == true){
			System.out.println("Support 3");
			int speed = 100;
			for(int k = 0; k < 6; k++){
				if( support[k] > 0){
					if(speed > soldier[k+1][12]){
						speed = soldier[k+1][12];
					}
				}
			}
			int time = 10;
			System.out.println(time);
			int[][] newSupport = {//123456askervegüçleri ; 7gönderilenxy ; 8gönderenxy ; 9time,race
					{support[0], village.soldiersInVillage[0][1]},{support[1],village.soldiersInVillage[1][1]},
					{support[2],village.soldiersInVillage[2][1]},{support[3],village.soldiersInVillage[3][1]},
					{support[4],village.soldiersInVillage[4][1]},{support[5], village.soldiersInVillage[5][1]},
					{support[6],support[7]},{x,y},{time,race}};
			ws.Support(name, p_name.get(map[support[6]][support[7]]), newSupport);
		}
	}
	public void d_Attack(int[] remove){
		w_attack.remove(remove);
	}
	public void d_gettingAttack(String[] infoD){
		gettingAttack.remove(infoD);
	}
	public ArrayList<String[]> getGettingAttack() {
		return gettingAttack;
	}
	public void gettingAttack(String[] infoD){
		gettingAttack.add(infoD);
	}
	public void yourDef(int[] def){
		//DonenA 123456: asker; 78910 hammaddemik; 11121314 hammaddecinsi; 15yaðmavar
		w_def.add(def);
	}
	public void d_yourDef(int[] def){//def:
		//DonenA 123456: asker; 78910 hammaddemik; 11121314 hammaddecinsi; 15yaðmavar
		w_def.remove(def);
	}
	public ArrayList<int[]> getW_attack() {
		return w_attack;
	}
	public void setW_attack(ArrayList<int[]> w_attack) {
		this.w_attack = w_attack;
	}
	public ArrayList<int[]> getW_def() {
		return w_def;
	}
	public ArrayList<int[]> getGoingToSupport() {
		return goingToSupport;
	}
	public void d_Support(int[] remove){
		goingToSupport.remove(remove);
	}

	
	public void Nufus(){
		System.out.println("Nufus girildi.");
		Refah();
		for(int i = 0; i< 6; i++){
			soldier[i+1][14] = new Soldiers().sldr.get(race)[i+1][14]-(int)Math.sqrt(village.nufus/50);
			System.out.println(soldier[i+1][14]);
		}
	}
	public void Refah(){
		System.out.println("Refah girildi.");
		village.refah = (int) (float)((float)((float)village.stuffs[0]/(village.nufus+1))*100);
		updateBonus();
	}

	
	public int getTime() {
		return time;
	}
	public Village getVillage() {
		return village;
	}

	public void BinaEtkileri(int i){
		switch(i){
		case 0: updateBonus();break;
		case 1: updateBonus();break;
		case 2: updateBonus();break;
		case 3: updateBonus();break;
		case 4: updateBonus();break;
		case 5: updateBonus();break;
		case 6: updateBonus();break;
		case 7: updateBonus();break;
		case 8: if(village.bS[8][0]%5 == 0){
			takeRFields();
		};break;
		case 16:updateBonus();;break;
		case 17:updateBonus();break;
		case 23: village.refah = village.refah+2; Refah();break;
		case 26: village.unionDepot = (int) (village.unionDepot + Math.pow(20, village.bS[26][0]));break;
		case 30: village.depo[0] = village.depo[0]+(village.bS[30][0]+1)*village.bS[30][0]*(village.bS[30][0]+1)*100;break;
		case 31: village.depo[1] = village.depo[1]+(village.bS[31][0]+1)*village.bS[31][0]*(village.bS[31][0]+1)*50;break;
		case 32: village.depo[2] = village.depo[2]+(village.bS[32][0]+1)*village.bS[32][0]*(village.bS[32][0]+1)*50;break;
		}
	}
	
	public void Sell(int a/*satýlanmadde*/, int b/*satýlanmaddemiktarý*/, int c/*istenenmadde*/,int d/*miktar*/){
		if(village.resource[a] >= b){
			int i;
			boolean enough = false;
			for(i = 0; i<3;i++){
				if(ftrade[i]==0){
					ftrade[i]=1;
					enough = true;
					break;
				}else{
					enough = false;
				}
			}
			if(enough){
			int[] sell = {playerNo*10 +i,a,b,c,d};
			marketplace.get(a).put(playerNo*10+i, sell);
			village.resource[a] = village.resource[a] - b;
			}
		}
	}
	public void Buy(int a, int b,Map<String, Player> players){//a:alýnacakmaddecinsi//b:buyno
		try{
		if(village.resource[marketplace.get(a).get(b)[3]] >= marketplace.get(a).get(b)[4]){
			int[] k = marketplace.get(a).get(b);
			marketplace.get(a).remove(b);
			village.resource[k[3]] = village.resource[k[3]] - k[4];
			int[] m = new int[6];
			for(int i = 0; i < 5;i++){
				m[i] = k[i];
			}
			k[0] = k[0]%10 + playerNo*10;
			players.get(p_name.get((int)(b/10))).Satýldý(k);
			m[5] = (int)(new Date().getTime()/1000) +600;
			comingMarket.add(m);
			

		}
		}catch(NullPointerException e){
			
		}
	}
	public void Satýldý(int[] k){
		System.out.println("Satýldý.");
		int[] m = new int[6];
		m[0] = k[0];
		m[1] = k[3];
		m[2] = k[4];
		m[3] = m[1];
		m[4] = m[2];
		m[5] = (int)(new Date().getTime()/1000) +600;
		comingMarket.add(m);
		ftrade[k[0]%10] = 0;
	}
	public void SatýsIptalEt(int a,int b){
		village.resource[a] = village.resource[a] + marketplace.get(a).get(b)[2];
		marketplace.get(a).remove(b);
		ftrade[b%10] = 0;
	}
	
	
	public void Guclendirme(int a){
		if(village.bS[21][0] > village.guclendirme[a]){
			int metal = ((village.guclendirme[a]+1)*(village.guclendirme[a])/2)*10+5;
			if(village.resource[6] >= metal){
				village.resource[6] = village.resource[6] - metal;
/*DÜZELT*/		int[] up = {a, (int)(new Date().getTime()/1000)+0*((village.guclendirme[a]+1)*(village.guclendirme[a])/2)};
				guclendirme.add(up);
			}
		}
	}
	public void Gadget(int a){
		boolean enough = false;
		for(int i=0; i < 9; i++){
			if(!(village.stuffs[i] >= village.gadgetsN[a][i])){
				enough = false;
				break;
			}else{
				enough = true;
			}
		}
		if(enough && CheckG(a)){
			for(int i = 0 ; i < 9;i++){
				village.stuffs[i] = village.stuffs[i]-village.gadgetsN[a][i];
				int[] up = {a, (int)(new Date().getTime()/1000)+7200*(a+1)};
				gadgets.add(up);
			}
		}
	}
	public boolean CheckG(int a){
		boolean enough = false;
		if(village.bS[27][0]>a){
			switch(a){
			case 0: if(village.bS[20][0]>=1){
				enough = true;break;
			}else{
				break;
			}
			case 1: if(village.bS[20][0]>=5){
				enough = true;break;
			}else{
				break;
			}
			case 2: if(village.bS[20][0]>=10){
				enough = true;break;
			}else{
				break;
			}
			case 3: if(village.bS[20][0]>=15){
				enough = true;break;
			}else{
				break;
			}
			case 4: if(village.bS[20][0]>=20){
				enough = true;break;
			}else{
				break;
			}
			case 5: if(village.bS[20][0]>=20){
				enough = true;break;
			}else{
				break;
			}
			case 6: if(village.bS[24][0]>=1){
				enough = true;break;
			}else{
				break;
			}
			case 7: if(village.bS[24][0]>=5){
				enough = true;break;
			}else{
				break;
			}
			case 8: if(village.bS[24][0]>=10){
				enough = true;break;
			}else{
				break;
			}
			case 9: if(village.bS[24][0]>=15){
				enough = true;break;
			}else{
				break;
			}
			}
		}
		return enough;
	}

	
	public void newSmallTown(int x, int y){
		smallTown.add(new SmallTown(x,y,map,playerNo,village));
		map[x][y] = playerNo;
	}
	public ArrayList<SmallTown> getSmallTown() {
		return smallTown;
	}

	public void newAlliance(String AllianceName){
		village.bS[25][0] = 5;
		if(village.bS[25][0] == 5){
			allianceRun.newAlliance(AllianceName, name);
		}
	}
	public void joinAlliance(int i){
		if(village.bS[25][0] >=1){
			if(invited.size()>i){
			allianceRun.acceptInvit(name, invited.get(i));
			}
			}
	}
	public void AllianceMethods(String invitname, int i, int k){
		switch(i){
		case 0: allianceRun.quit(name,inAllianceName);break;
		case 1: allianceRun.sentInvit(name, invitname, inAllianceName);break;
		case 2: allianceRun.kick(name,invitname,inAllianceName);break;
		case 3: allianceRun.assign(name, invitname, inAllianceName,k);break;
		}
	}
	public void atackCommand(ArrayList<int[]> way, int[] saldýrý){
		allianceRun.attackCommand(name,inAllianceName,way,saldýrý);
	}
}
