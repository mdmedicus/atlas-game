package Core;

public class Movements {
	
	public String sald�ran;
	public String savunan;
	public int[][] sald�r�;
	public int[] donenA;
	public long date;
	public int[] info;
	public String[] infoD;
	
	
	//Sald�r� i�in
	public Movements(String sald�ran,String savunan,int[][] sald�r�,long date,int[] info, String[] infoD){
		this.sald�ran = sald�ran;
		this.savunan = savunan;
		this.sald�r� = sald�r�;
		this.date = date;
		this.info = info;
		this.infoD = infoD;
	}
	
	//Sald�r�dan geri gelen askerler i�in
	public Movements(String sald�ran, int[] donenA, long donenTime){
		this.sald�ran = sald�ran;
		this.donenA = donenA;
		this.date = donenTime;
		
	}
}
