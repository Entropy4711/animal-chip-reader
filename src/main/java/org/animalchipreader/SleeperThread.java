package org.animalchipreader;

public class SleeperThread extends Thread {

	@Override
	public void run() {
		while (true) {
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}