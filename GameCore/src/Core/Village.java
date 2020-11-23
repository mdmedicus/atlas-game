package Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Village {

		//nüfus
		public int nufus = 10;
		public int refah = 0;
		//UnionDepot
		public int unionDepot = 0;
		
		//Soldiers
		public int[][] soldiersInVillage = {{0,0},{20000,1},{50,0},{0,0},{0,0},{0,0},{0,0}};
		public int[][] soldiersFriends = {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}};
		public int[][] soldiersinTheDef = {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}};
		public int[] guclendirme = {0,0,0,0,0,0,0};
		//Soldiers can ecruit
		public int[] scRecruit = {0,0,0,0,0,0};
		
		////////////////GOOOOOOOOOOOOOLLLLLLLLLLLLLLDDDDDDDDDDDDDDDDD///////////
		public int gold = 0;
		
		//Resource Fields
		public Map<String, float[]> resources = new HashMap<String, float[]>();//Woods,StoneePit,IronMine,ClayPit,Ranch,Crop,MetalMine,SilverMine
		public ArrayList<String> resourcesX = new ArrayList<>();
		//Resources in Village
		public int[] resource = {500,500,500,500,500,300,300,10};//wood,stone,iron,clay,ranch,crop,metal,silver
		//Resource Production
				public int[] rb = {360,360,360,360,60,60,20,2};//lumberman,stonepit,ironsmith,claypit,ranch,cropland,metalminer,silver
				public int[] rbG = {360,360,360,360,60,60,20,2}; 
				public int[][] bS= new int[35][2];
				public int[][] bN= {{80,40,20,40,20,0,0,0,5,1},{40,80,40,20,20,0,0,0,300,1},{40,40,80,20,20,0,0,0,300,1},
						{40,20,40,80,20,0,0,0,300,1},{80,40,20,20,40,0,0,0,300,1},{80,20,20,80,40,0,0,0,300,1},
						{40,40,120,20,40,0,0,0,300,1},{40,80,100,40,100,0,0,0,300,1},{50,60,40,50,10,0,0,0,6,2},
						{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},
						{120,160,100,80,80,0,0,0,15,3},{130,200,100,140,90,0,0,0,20,3},{150,190,140,120,100,0,0,0,960,3},
						{120,100,80,90,60,0,0,0,720,4},{180,120,200,40,80,0,0,0,900,3},{160,160,120,180,240,0,0,0,1260,4},
						{80,150,200,80,120,0,80,0,1260,4}, {90,60,85,30,40,0,0,0,240,3},{110,75,100,40,70,0,0,0,360,4},
						{240,170,205,120,130,0,130,0,1500,8},{120,60,90,20,70,0,0,0,720,2},{170,130,110,80,10,0,0,0,960,2},
						{120,160,210,100,90,0,40,10,1440,10},{280,210,220,140,105,0,90,0,1500,9},{120,150,120,90,50,0,0,0,300,3},
						{170,230,120,180,100,0,0,0,600,3},{280,320,240,220,160,0,220,40,1800,6},{540,620,780,560,400,200,140,20,300,0},
						{50,70,40,40,20,0,0,0,240,1},{0,0,0,0,0,0,0,0,0,0}};//01234567ham, 8 time, 9 nufus
		//Buildings
		//0TownHall,1Shelter,2ECenter,3MarketPlace,4School,5Akademi,6College,7MilitarySchool,8ProductionS,9Mining
		//10Barracks,11CavalrySchool,12WarFactory,13PractiseRange,14WatchTower,15Treasury,16TechnoCenter,17Embassy
		//18UnionDepot,19AR-GECenter,20SecretService,21CityWall,22Warehouse,23Cold Store,24Granary,25 saray,2627Yerleþimyeri123456
		//Building needed
				
		//DEPO
		public int[] depo = {900,600,600};
		
		//Locations
		public int[] location = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

		//Other STUFFS
		public int[] stuffs = {1,100,0,0,0,0,0,0,0};//Silk,rudstone,lumber,cement,powder,rope,shaft,Zn,leather
		//Gadgets
		public int[] gadgets = {0,0,0,0,0,0,0,0,0,0};//Awall,Artillery Arrow,Caisson, Stell Shell,DiamondBit,parchment,
		//windmill,bigsaw,mining-machine,phaeton
		public int[][] gadgetsN = {{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0}};
}
