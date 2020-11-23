package Core;

import java.util.ArrayList;

public class Soldiers {

	public int[][] boga = {{0,0,60,120,0,0,0,0,0,0,0,0,0},//1234PUAN, 567891011ham, 12tahýl,13hýz,14yaðma,15süre
			{35,25,20,10,40,0,70,0,20,0,0,1,20,50,0},//wood,stone,iron,clay,ranch,crop,metal
			{25,20,45,40,110,0,45,0,25,0,0,1,16,30,0},
			{60,40,25,10,70,0,120,0,40,0,5,1,16,40,0},
			{55,30,20,5,50,0,60,0,90,0,0,2,32,90,0},
			{80,50,25,10,70,0,90,0,120,0,0,2,30,75,0},
			{120,90,50,30,100,0,130,0,150,0,15,3,28,60,0}};
	
	public int[] extraResource = {0,0,0,0,0,0,6,0,0,0};
	public float sldrSpeed = 1;
	public int extraSlot = 0;
	public int extraCranny = 2;
	public int yayaatli = 6;
	public ArrayList<int[][]> sldr = new ArrayList<>(); 
	
	public Soldiers(){
		sldr.add(boga);
	}
}
