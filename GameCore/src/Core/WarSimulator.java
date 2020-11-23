package Core;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class WarSimulator extends Thread{
	
	private ArrayList<Movements> war = new ArrayList<>();
	private ArrayList<Movements> defAfterA = new ArrayList<>(); 
	private ArrayList<Movements> support = new ArrayList<>();
	private Map<String, Player> players;
	private int[][] soldiers_A;
	private int[][] soldiers_D;
	private Soldiers stype = new Soldiers();
	
	public WarSimulator(Map<String, Player> players){
		this.players = players;
		System.out.println("War Simulator");
	}
	
	public void WarSimulator(String saldýran,String savunan,int[][] saldýrý){
		long date = 0;
		//123456 asker; 7 saldýrýtipi; 89 xy; 10 datessayisi;
		int[] info = {0,0,0,0,0,0,0,0,0,0};
		System.out.println(saldýran+" "+savunan);
		date = (long)(new Date().getTime()/100);
		System.out.println(date);
		date = date+ (long)saldýrý[6][1]*10; 
		System.out.println(date);
		for(int i = 0; i < 6;i++){
			players.get(saldýran).getVillage().soldiersInVillage[i+1][0] = 
					players.get(saldýran).getVillage().soldiersInVillage[i+1][0] - saldýrý[i][0];
		}
		for(int i = 0; i<info.length-2;i++){
			info[i] = saldýrý[i][0];
		}
		info[8] = saldýrý[7][1];
		info[9] = (int)(date/10);
		String[] infoD ={saldýran, Integer.toString(info[9])};
		war.add(new Movements(saldýran,savunan,saldýrý,date,info,infoD));
		System.out.println(saldýrý[9][1]);
		players.get(saldýran).getW_attack().add(info);//Saldýrana saldýrýnýn gidiþiyle ilgili bilgi ver
		
		players.get(savunan).gettingAttack(infoD);//Savunana saldýrýnýn geliþiyle ilgili bilgi ver
		System.out.println("WarSimulator 1");
		System.out.println(players.get(saldýran).getW_attack().get(0)[0]);//Deðiþti
	}
	
	public void Support(String gonderen, String friend, int[][] destek){
		//123456askervegüçleri ; 7gönderilenxy ; 8gönderenxy ; 9time,race
		long date = 0;
		//123456 asker; 78 xy; 9 date;
		int[] info = {0,0,0,0,0,0,0,0,0};
		System.out.println(gonderen+" "+friend);
		date = (long)(new Date().getTime()/100);
		System.out.println(date);
		date = date+ (long)destek[8][0]*10; 
		System.out.println(date);
		for(int i = 0; i < 6;i++){
			players.get(gonderen).getVillage().soldiersInVillage[i][0] = 
					players.get(gonderen).getVillage().soldiersInVillage[i][0] - destek[i][0];
		}
		for(int i = 0; i<info.length-2;i++){
			info[i] = destek[i][0];
		}
		info[7] = destek[6][1];
		info[8] = (int)(date/10);
		System.out.println(destek[6][1]);
		String[] infoD = null;
		support.add(new Movements(gonderen, friend, destek,date,info,infoD));
		players.get(gonderen).getGoingToSupport().add(info);
		players.get(friend).getW_def().add(info);
		System.out.println("Support simulator");
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(true){
			
		for(int i = 0; i < defAfterA.size(); i++){//defleri hallet
			if((long)(new Date().getTime()/100) > defAfterA.get(i).date){
				finishDAA(defAfterA.get(i).saldýran, defAfterA.get(i).donenA);
				defAfterA.remove(i);
				i--;
			}
		}
		
		for(int i = 0; i < support.size();i++){//giden destekler
			if((long)(new Date().getTime()/100) > support.get(i).date){
				finishSupport(support.get(i).saldýran, support.get(i).savunan, support.get(i).saldýrý);
				support.remove(i);
				players.get(war.get(i).saldýran).d_Support(war.get(i).info);
				i--;
			}
		}
		for(int i = 0; i < war.size();i++){//saldýrýlarý hallet
			if((long)(new Date().getTime()/100) > war.get(i).date){
				War(war.get(i).saldýran,war.get(i).savunan,war.get(i).saldýrý);
				players.get(war.get(i).saldýran).d_Attack(war.get(i).info);
				players.get(war.get(i).savunan).d_gettingAttack(war.get(i).infoD);
				war.remove(i);
				i--;
			}
		}
		try {
			Thread.sleep(17);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	private void War(String saldýran,String savunan,int[][] saldýrý){//savaþ aný//saldýrý: 123456asker; 7atakcinsi ve süre
		System.out.println((int)(new Date().getTime()/1000));//8savunanxy ; 9saldýranxy; 10ýrkvesaldýrýsayýsý; 11hammdadde 
		String report;
		int atli_sayisi_S = 0;
		int yaya_sayisi_S = 0;
		int atli_vurus = 0;
		int yaya_vurus = 0;
		int atli_sayisi_D = 0;
		int yaya_sayisi_D = 0;
		int atli_def = 0;
		int yaya_def = 0;
		int[] loot = {0,0,0,0};
		boolean donen = false;
		
		
		//DonenA 123456: asker; 78910 hammaddemik; 11121314 hammaddecinsi; 15yaðmavar; 16 date
		int[] donenA = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		long donenTime = 0;
		int[] olen_S = {0,0,0,0,0,0,0};
		int[] olen_D = {0,0,0,0,0,0,0};
		int[] kalan_S = {0,0,0,0,0,0,0};
		int[] kalan_D = {0,0,0,0,0,0,0};
		System.out.println(saldýrý[9][0]);
		
		
	
		
		soldiers_A = stype.sldr.get(saldýrý[9][0]);
			atli_sayisi_S = saldýrý[3][0]+saldýrý[4][0]+saldýrý[5][0];
			yaya_sayisi_S = saldýrý[0][0]+saldýrý[1][0]+saldýrý[2][0];
			atli_vurus = (int)(saldýrý[0][0]*soldiers_A[1][1]*(100+saldýrý[0][1])/100)+
					(int)(saldýrý[1][0]*soldiers_A[2][1]*(100+saldýrý[1][1])/100)+
					(int)(saldýrý[2][0]*soldiers_A[3][1]*(100+saldýrý[2][1])/100)+
					(int)(saldýrý[3][0]*soldiers_A[4][1]*(100+saldýrý[3][1])/100)+
					(int)(saldýrý[4][0]*soldiers_A[5][1]*(100+saldýrý[4][1])/100)+
					(int)(saldýrý[5][0]*soldiers_A[6][1]*(100+saldýrý[5][1])/100);
			yaya_vurus = (int)(saldýrý[0][0]*soldiers_A[1][0]*(100+saldýrý[0][1])/100)+
					(int)(saldýrý[1][0]*soldiers_A[2][0]*(100+saldýrý[1][1])/100)+
					(int)(saldýrý[2][0]*soldiers_A[3][0]*(100+saldýrý[2][1])/100)+
					(int)(saldýrý[3][0]*soldiers_A[4][0]*(100+saldýrý[3][1])/100)+
					(int)(saldýrý[4][0]*soldiers_A[5][0]*(100+saldýrý[4][1])/100)+
					(int)(saldýrý[5][0]*soldiers_A[6][0]*(100+saldýrý[5][1])/100);
			System.out.println("War simulator Switch 1");
		
		soldiers_D = stype.sldr.get(players.get(savunan).race);
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
			System.out.println("War simulator Switch 2");
		
		
		int savunan_def = (int)(atli_def*atli_sayisi_S +yaya_def*yaya_sayisi_S)/(atli_sayisi_S+yaya_sayisi_S);
		int saldýran_vurus = 0;
		if(savunan_def != 0){
		saldýran_vurus = (int)(atli_vurus*atli_sayisi_D + yaya_vurus*yaya_sayisi_D)/(atli_sayisi_D+yaya_sayisi_D);
		}
		System.out.println(saldýran_vurus + " "+ savunan_def);
		
		//Normal saldýrý
		if(saldýrý[6][0] == 1){
			
			///Savunan Yok
			if(savunan_def == 0){
				donen = true;
				donenTime = (long)(new Date().getTime()/100) + (long)saldýrý[6][1]*10;
				donenA[15] = (int)(donenTime/10);
				for(int i=0; i < kalan_D.length-1;i++){
					olen_S[i] = 0;
					olen_D[i] = 0;
					kalan_D[i] = 0;
					donenA[i] = saldýrý[i][0];
					System.out.println("Savunan Olmadý: "+olen_S[i]+"/");
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				DefAfterA(saldýran,donenA, donenTime);
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
			
			//Saldýran kazandý
			if(saldýran_vurus > (int)(savunan_def*105)/100 && savunan_def != 0){
				donen = true;
				donenTime = (long)(new Date().getTime()/100) + (long)saldýrý[6][1]*10;
				donenA[15] = (int)(donenTime/10);
				for(int i=0; i < kalan_D.length-1;i++){
					float katsayi = ((float)8/10)*(((float)savunan_def)/saldýran_vurus);
					System.out.print(katsayi);
					olen_S[i] = (int) Math.floor(saldýrý[i][0]*katsayi);
					kalan_S[i] = saldýrý[i][0]-olen_S[i]; 
					olen_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0];
					kalan_D[i] = 0;
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					donenA[i] = kalan_S[i];
					System.out.println("Saldýran Kazandý: Olen/Kalan "+olen_S[i]+"/"+kalan_S[i]);
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				DefAfterA(saldýran, donenA, donenTime);
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor =rapor+ " "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor =rapor+ " "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
			
			///Berabere
			if(saldýran_vurus <= (int)(savunan_def*105)/100 && saldýran_vurus >= (int)(savunan_def*95)/100 && saldýran_vurus !=0){
				donen = false;
				for(int i=0; i < kalan_D.length-1;i++){
					olen_S[i] = saldýrý[i][0];
					olen_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0];
					kalan_D[i] = 0;
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					System.out.println("Berabere: "+olen_S[i]+"/");
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
				
			}
			
			
			
			
			//Savunan kazandý.
			if(saldýran_vurus < (int)(savunan_def*95)/100){
				donen = false;
				for(int i=0; i < kalan_D.length-1;i++){
					float katsayi = ((float)8/10)*(((float)saldýran_vurus)/savunan_def);
					System.out.print(katsayi);
					olen_S[i] = saldýrý[i][0];
					olen_D[i] = (int) Math.floor(players.get(savunan).getVillage().soldiersInVillage[i+1][0]*katsayi);
					kalan_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0]-olen_D[i];
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					System.out.println("Savunan Kazandý. Olen/Kalan Def: "+olen_D[i]+"/"+kalan_D[i]);
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(savunan).rapor.add(rapor);
			}
		}
		
		if(saldýrý[6][0] == 2){
			///Savunan Yok
			if(savunan_def == 0){
				donen = true;
				donenTime = (long)(new Date().getTime()/100) + (long)saldýrý[6][1]*10;
				donenA[15] = (int)(donenTime/10);
				for(int i=0; i < kalan_D.length-1;i++){
					olen_S[i] = 0;
					olen_D[i] = 0;
					kalan_D[i] = 0;
					donenA[i] = saldýrý[i][0];
					System.out.println("Savunan Olmadý: "+olen_S[i]+"/");
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				int can = 0;
				for(int z = 0; z < donenA.length-10; z++){
				can = can + soldiers_A[z+1][9]*donenA[z];
				}
				can = (int)can/4;
				for(int a = 0; a < 4; a++){
					donenA[10+a] = saldýrý[10][a];
					if(players.get(savunan).getVillage().resource[a+saldýrý[10][a]] >= can){
						donenA[a+6] = can;
						players.get(savunan).getVillage().resource[a+saldýrý[10][a]] = 
								players.get(savunan).getVillage().resource[a+saldýrý[10][a]] - can;
					} else{
						donenA[a+6] = (int)players.get(savunan).getVillage().resource[a+saldýrý[10][a]];
						players.get(savunan).getVillage().resource[a+saldýrý[10][a]] = 0 ;
					}
				}
				donenA[14] = 1;
				DefAfterA(saldýran,donenA, donenTime);
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
			
			//Saldýran kazandý
			if(saldýran_vurus >= (int)(savunan_def*55)/100 && savunan_def != 0){
				donen = true;
				donenTime = (long)(new Date().getTime()/100) + (long)saldýrý[6][1]*10;
				donenA[15] = (int)(donenTime/10);
				for(int i=0; i < kalan_D.length-1;i++){
					float katsayiS = ((float)savunan_def)/(savunan_def+saldýran_vurus);
					float katsayiD = ((float)saldýran_vurus)/(savunan_def+saldýran_vurus);
					System.out.print(katsayiS);
					olen_S[i] = (int) Math.floor(saldýrý[i][0]*katsayiS);
					kalan_S[i] = saldýrý[i][0]-olen_S[i]; 
					olen_D[i] = (int)Math.floor(players.get(savunan).getVillage().soldiersInVillage[i+1][0]*katsayiD);
					kalan_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0]-olen_D[i];
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					donenA[i] = kalan_S[i];
					System.out.println("Saldýran Kazandý: Olen/Kalan "+olen_S[i]+"/"+kalan_S[i]);
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				int can = 0;
				for(int z = 0; z < donenA.length-10; z++){
				can = can + soldiers_A[z+1][13]*donenA[z];
				}
				can = (int)can/4;
				for(int a = 0; a < 4; a++){
					donenA[10+a] = saldýrý[10][a];
					if(players.get(savunan).getVillage().resource[saldýrý[10][a]] >= can){
						donenA[a+6] = can;
						players.get(savunan).getVillage().resource[saldýrý[10][a]] = 
								players.get(savunan).getVillage().resource[saldýrý[10][a]] - can;
					} else{
						donenA[a+6] = (int)players.get(savunan).getVillage().resource[saldýrý[10][a]];
						players.get(savunan).getVillage().resource[saldýrý[10][a]] = 0 ;
					}
					System.out.println(donenA[a+6]);
				}
				donenA[14] = 1;
				DefAfterA(saldýran, donenA, donenTime);
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
			
			//Savunan kazandý.
			if(saldýran_vurus < (int)(savunan_def*55)/100){
				donen = false;
				for(int i=0; i < kalan_D.length-1;i++){
					float katsayi = ((float)saldýran_vurus)/(savunan_def+saldýran_vurus);
					System.out.print(katsayi);
					olen_S[i] = saldýrý[i][0];
					olen_D[i] = (int) Math.floor(players.get(savunan).getVillage().soldiersInVillage[i+1][0]*katsayi);
					kalan_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0]-olen_D[i];
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					System.out.println("Savunan Kazandý. Olen/Kalan Def: "+olen_D[i]+"/"+kalan_D[i]);
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(savunan).rapor.add(rapor);
			}
			
		}
		
		if(saldýrý[6][0] == 3){
			///Savunan Yok
			if(savunan_def == 0){
				donen = true;
				donenTime = (long)(new Date().getTime()/100) + (long)saldýrý[6][1]*10;
				donenA[15] = (int)(donenTime/10);
				for(int i=0; i < kalan_D.length-1;i++){
					olen_S[i] = 0;
					olen_D[i] = 0;
					kalan_D[i] = 0;
					donenA[i] = saldýrý[i][0];
					System.out.println("Savunan Olmadý: "+olen_S[i]+"/");
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				DefAfterA(saldýran,donenA, donenTime);
				for(int i = 0; i < 2; i++){
					if(players.get(savunan).getVillage().resources.containsKey(""+saldýrý[11][0]+","+saldýrý[11][1])){
						if( Math.abs(saldýrý[11+i][0]-players.get(saldýran).x) <= (int)(players.get(saldýran).getVillage().bS[8][0]/5+1)&&
								Math.abs(saldýrý[11+i][1]-players.get(saldýran).y) <= (int)(players.get(saldýran).getVillage().bS[8][0]/5+1)){
						players.get(savunan).getVillage().resources.remove(""+saldýrý[11+i][0]+","+saldýrý[11+i][1]);
						players.get(savunan).getVillage().resourcesX.remove(""+saldýrý[11+i][0]+","+saldýrý[11+i][1]);
						players.get(savunan).map[saldýrý[11+i][0]][saldýrý[11+i][1]] = 
								players.get(savunan).map[saldýrý[11+i][0]][saldýrý[11+i][1]] -10*players.get(savunan).playerNo;
						players.get(saldýran).takeRFields();
						}
					}
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
			
			//Saldýran kazandý
			if(saldýran_vurus > (int)(savunan_def*105)/100 && savunan_def != 0){
				donen = true;
				donenTime = (long)(new Date().getTime()/100) + (long)saldýrý[6][1]*10;
				donenA[15] = (int)(donenTime/10);
				for(int i=0; i < kalan_D.length-1;i++){
					float katsayi = ((float)savunan_def)/saldýran_vurus;
					System.out.print(katsayi);
					olen_S[i] = (int) Math.floor(saldýrý[i][0]*katsayi);
					kalan_S[i] = saldýrý[i][0]-olen_S[i]; 
					olen_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0];
					kalan_D[i] = 0;
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					donenA[i] = kalan_S[i];
					System.out.println("Saldýran Kazandý: Olen/Kalan "+olen_S[i]+"/"+kalan_S[i]);
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				DefAfterA(saldýran, donenA, donenTime);
				for(int i = 0; i < 2; i++){
					if(players.get(savunan).getVillage().resources.containsKey(""+saldýrý[11][0]+","+saldýrý[11][1])){
						if( Math.abs(saldýrý[11+i][0]-players.get(saldýran).x) <= (int)(players.get(saldýran).getVillage().bS[8][0]/5+1)&&
								Math.abs(saldýrý[11+i][1]-players.get(saldýran).y) <= (int)(players.get(saldýran).getVillage().bS[8][0]/5+1)){
						players.get(savunan).getVillage().resources.remove(""+saldýrý[11+i][0]+","+saldýrý[11+i][1]);
						players.get(savunan).getVillage().resourcesX.remove(""+saldýrý[11+i][0]+","+saldýrý[11+i][1]);
						players.get(savunan).map[saldýrý[11+i][0]][saldýrý[11+i][1]] = 
								players.get(savunan).map[saldýrý[11+i][0]][saldýrý[11+i][1]] -10*players.get(savunan).playerNo;
						players.get(saldýran).takeRFields();
						}
					}
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
			
			///Berabere
			if(saldýran_vurus <= (int)(savunan_def*105)/100 && saldýran_vurus >= (int)(savunan_def*95)/100 && saldýran_vurus !=0){
				donen = false;
				for(int i=0; i < kalan_D.length-1;i++){
					olen_S[i] = saldýrý[i][0];
					olen_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0];
					kalan_D[i] = 0;
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					System.out.println("Berabere: "+olen_S[i]+"/");
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
				
			}
			
			
			
			
			//Savunan kazandý.
			if(saldýran_vurus < (int)(savunan_def*95)/100){
				donen = false;
				for(int i=0; i < kalan_D.length-1;i++){
					float katsayi = ((float)saldýran_vurus)/savunan_def;
					System.out.print(katsayi);
					olen_S[i] = saldýrý[i][0];
					olen_D[i] = (int) Math.floor(players.get(savunan).getVillage().soldiersInVillage[i+1][0]*katsayi);
					kalan_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0]-olen_D[i];
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					System.out.println("Savunan Kazandý. Olen/Kalan Def: "+olen_D[i]+"/"+kalan_D[i]);
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(savunan).rapor.add(rapor);
			}
		}
		
		if(saldýrý[6][0] == 4){
			///Savunan Yok
			if(savunan_def == 0){
				donen = true;
				donenTime = (long)(new Date().getTime()/100) + (long)saldýrý[6][1]*10;
				donenA[15] = (int)(donenTime/10);
				for(int i=0; i < kalan_D.length-1;i++){
					olen_S[i] = 0;
					olen_D[i] = 0;
					kalan_D[i] = 0;
					donenA[i] = saldýrý[i][0];
					System.out.println("Savunan Olmadý: "+olen_S[i]+"/");
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				DefAfterA(saldýran,donenA, donenTime);
				for(int i = 0; i < players.get(savunan).getSmallTown().size(); i++){
					if(players.get(savunan).getSmallTown().get(i).x == saldýrý[7][0]&& 
							players.get(savunan).getSmallTown().get(i).y == saldýrý[7][1]&&
							players.get(saldýran).getVillage().bS[33][0]>players.get(saldýran).getSmallTown().size()){
						players.get(savunan).getSmallTown().remove(i);
						players.get(saldýran).newSmallTown(saldýrý[7][0], saldýrý[7][1]);
					}
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
			
			//Saldýran kazandý
			if(saldýran_vurus > (int)(savunan_def*105)/100 && savunan_def != 0){
				donen = true;
				donenTime = (long)(new Date().getTime()/100) + (long)saldýrý[6][1]*10;
				donenA[15] = (int)(donenTime/10);
				for(int i=0; i < kalan_D.length-1;i++){
					float katsayi = ((float)savunan_def)/saldýran_vurus;
					System.out.print(katsayi);
					olen_S[i] = (int) Math.floor(saldýrý[i][0]*katsayi);
					kalan_S[i] = saldýrý[i][0]-olen_S[i]; 
					olen_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0];
					kalan_D[i] = 0;
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					donenA[i] = kalan_S[i];
					System.out.println("Saldýran Kazandý: Olen/Kalan "+olen_S[i]+"/"+kalan_S[i]);
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				DefAfterA(saldýran, donenA, donenTime);
				for(int i = 0; i < players.get(savunan).getSmallTown().size(); i++){
					if(players.get(savunan).getSmallTown().get(i).x == saldýrý[7][0]&& 
							players.get(savunan).getSmallTown().get(i).y == saldýrý[7][1]&&
							players.get(saldýran).getVillage().bS[33][0]>players.get(saldýran).getSmallTown().size()){
						players.get(savunan).getSmallTown().remove(i);
						players.get(saldýran).newSmallTown(saldýrý[7][0], saldýrý[7][1]);
					}
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
			}
			
			///Berabere
			if(saldýran_vurus <= (int)(savunan_def*105)/100 && saldýran_vurus >= (int)(savunan_def*95)/100 && saldýran_vurus !=0){
				donen = false;
				for(int i=0; i < kalan_D.length-1;i++){
					olen_S[i] = saldýrý[i][0];
					olen_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0];
					kalan_D[i] = 0;
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					System.out.println("Berabere: "+olen_S[i]+"/");
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(saldýran).rapor.add(rapor);
				players.get(savunan).rapor.add(rapor);
				
			}
			
			
			
			
			//Savunan kazandý.
			if(saldýran_vurus < (int)(savunan_def*95)/100){
				donen = false;
				for(int i=0; i < kalan_D.length-1;i++){
					float katsayi = ((float)saldýran_vurus)/savunan_def;
					System.out.print(katsayi);
					olen_S[i] = saldýrý[i][0];
					olen_D[i] = (int) Math.floor(players.get(savunan).getVillage().soldiersInVillage[i+1][0]*katsayi);
					kalan_D[i] = players.get(savunan).getVillage().soldiersInVillage[i+1][0]-olen_D[i];
					players.get(savunan).getVillage().soldiersInVillage[i+1][0] = kalan_D[i];
					System.out.println("Savunan Kazandý. Olen/Kalan Def: "+olen_D[i]+"/"+kalan_D[i]);
					System.out.println("Def: "+ players.get(savunan).getVillage().soldiersInVillage[i+1][0]);
				}
				String rapor = "Saldiran, Kalan|Olen";
				for(int rpr = 0; rpr < 6; rpr++){
				rapor = rapor+" "+donenA[rpr]+"|"+olen_S[rpr]+"--";
				}
				for(int rpr = 0; rpr < 7; rpr++){
					rapor = rapor+" "+kalan_D[rpr]+"|"+olen_D[rpr]+"--";
					}
				players.get(savunan).rapor.add(rapor);
			}
		}
		
	}
	
	public void DefAfterA(String name,int[] donenA, long donenTime){//Ataktan sonra def gelimi
		//DonenA 123456: asker; 78910 hammaddemik; 11121314 hammaddecinsi; 15yaðmavar 16date
		System.out.println((int)(new Date().getTime()/1000));
		defAfterA.add(new Movements(name, donenA, donenTime));
		players.get(name).yourDef(donenA);
	}
	
	public void finishDAA(String name, int[] donenA){//Ataktan sonra gelen deflerin köye giriþi
		System.out.println((int)(new Date().getTime()/1000));//donenA 15'li; 123456asker ;78910hammadde ; 11121314hcinsi;//15 yaðma var mý;
		for(int i =0; i<6; i++){
			players.get(name).getVillage().soldiersInVillage[i+1][0] =
					players.get(name).getVillage().soldiersInVillage[i+1][0] + donenA[i];
		}
		if(donenA[14] == 1){
		for(int i = 6; i < 10; i++){
			players.get(name).getVillage().resource[donenA[i+4]] = 
					players.get(name).getVillage().resource[donenA[i+4]]+donenA[i];
		}
		}
		players.get(name).d_yourDef(donenA);
	}
	
	public void finishSupport(String gonderen, String friend, int[][] destek){
		//123456askervegüçleri ; 7gönderilenxy ; 8gönderenxy ; 9time,race
		System.out.println((int)(new Date().getTime()/1000));
		for(int i =0; i<6; i++){
			players.get(friend).getVillage().soldiersFriends[i+1][0] =
					players.get(friend).getVillage().soldiersFriends[i+1][0] + destek[i][0];
			players.get(gonderen).getVillage().soldiersinTheDef[i+1][0] = 
					players.get(gonderen).getVillage().soldiersInVillage[i+1][0] + destek[i][0];
		}
	}


}
