package Core;

import java.util.ArrayList;

public class Alliance {
	public String AllianceName;
	public int AllianceNo;
	public ArrayList<String> players = new ArrayList<>();
	public ArrayList<String> invitedPlayers = new ArrayList<>();
	
	public ArrayList<String> rapor = new ArrayList<>();
	
	public String[] preAssist = {"","",""};
	public ArrayList<String> diplomats = new ArrayList<>(5);
	public ArrayList<String> Generals = new ArrayList<>(5); 
	
	public ArrayList<String> withWar = new ArrayList<>();
	public ArrayList<String[]> ceaseFire = new ArrayList<>();
	public ArrayList<String> ally = new ArrayList<>();
	
	public int AttackPoint = 0;
	public int DefendPoint = 0;
	public int TotalPoint(){
		return AttackPoint + DefendPoint;
	}

	public int[][] soldiers = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},
			{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
	
	public int[][] soldiersForAttack = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
	
	public int[][] soldiersForDefend = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
	
	public ArrayList<ArrayList<int[]>> soldiersOnTheWay = new ArrayList<ArrayList<int[]>>();
	
	public Alliance(String Alliance,String name){
		AllianceName = Alliance;
		preAssist[0] = name;
		players.add(name);
	}
	
	
}
