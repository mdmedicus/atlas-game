package Core;

import java.util.Scanner;

public class Exit extends Thread{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(true){
		Scanner s = new Scanner(System.in);
		if("Exit".equals(s.nextLine())){
			System.exit(0);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
	}

}
