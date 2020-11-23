package Core;

public class Movements {
	
	public String saldýran;
	public String savunan;
	public int[][] saldýrý;
	public int[] donenA;
	public long date;
	public int[] info;
	public String[] infoD;
	
	
	//Saldýrý için
	public Movements(String saldýran,String savunan,int[][] saldýrý,long date,int[] info, String[] infoD){
		this.saldýran = saldýran;
		this.savunan = savunan;
		this.saldýrý = saldýrý;
		this.date = date;
		this.info = info;
		this.infoD = infoD;
	}
	
	//Saldýrýdan geri gelen askerler için
	public Movements(String saldýran, int[] donenA, long donenTime){
		this.saldýran = saldýran;
		this.donenA = donenA;
		this.date = donenTime;
		
	}
}
