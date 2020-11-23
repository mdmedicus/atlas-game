package Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmallTown {

	//coordinates
	public int x;
	public int y;
	//Soldiers
			public int[][] soldiersInVillage = {{0,0},{500,2},{500,2},{0,0},{0,0},{0,0},{0,0}};
			public int[][] soldiersFriends = {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}};
			public int[][] soldiersinTheDef = {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}};
	//Resource Fields
	public Map<String, float[]> resources = new HashMap<String, float[]>();//Woods,StoneePit,IronMine,ClayPit,Ranch,Crop,MetalMine,SilverMine
	ArrayList<String> resourcesX = new ArrayList<>();
	
	public static int[][] map;
	public static int playerNo;
	public static Village village;
	
	public SmallTown(int x, int y,int[][] map, int playerNo, Village village){
		this.x = x;
		this.y = y;
		this.map = map;
		this.playerNo = playerNo;
		this.village = village;
		
		takeRFields();
	}
	
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
					if(resources.containsKey(""+xf+","+yf)){
						//Do nothing.
					} else {
						if((int)(map[xf][yf]/10) == 0){
							resourcesX.add(""+xf+","+yf);
							float[] deger = {map[xf][yf],0};
							resources.put(""+xf+","+yf, deger);
							map[xf][yf] = playerNo*10 + map[xf][yf];
							System.out.println(xf+" "+yf+" "+map[xf][yf]);
						}
					}
				}
			}
		
	}
}
