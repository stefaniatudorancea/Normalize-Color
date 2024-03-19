package packWork;
public class Buffer {
	private int[][] pixels;
	private boolean available = false;
	
	public synchronized int[][] get() {
		while (!available) {
			
			// asteaptam pana cand resursa devine disponibila pentru citire
			try {
				
				wait();
				
			} catch (InterruptedException e) {
				
				e.printStackTrace(); 
			}
		}
		
		// setam disponibilitatea
		available = false;
		
		// anuntam incheierea procedurii
		notifyAll();
		
		// returnam matricea updatata
		return this.pixels;
	}
	
	public synchronized void put(int[][] pixels) {
		while (available) {
			
			// asteaptam pana cand resursa nu mai este disponibila pentru citire
			try {
				
				wait();
				
			} catch (InterruptedException e) {
				
				e.printStackTrace(); 
			}
		}
		
		// retinem matricea primita
		this.pixels = pixels;
		
		// setam disponibilitatea
		available = true;
		
		// anuntam incheierea procedurii
		notifyAll();
	}
}