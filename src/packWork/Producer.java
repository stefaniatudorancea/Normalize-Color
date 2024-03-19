package packWork;

import packMain.Main;

public class Producer extends Thread {
	private Buffer buffer;
	
	// constructor fara parametri
	Producer() {
		super();
	}
	public Producer(Buffer buf) {
		this.buffer = buf;
		System.out.println("S-a apelat constructorul pentru Producer");
	}
	
	public void run () {
		
		// retinem latimea imaginii
		int width = Main.ImageInit.getWidth(); 
		
		// retinem inaltimea imaginii
		int height = Main.ImageInit.getHeight(); 
		
		// impartim imaginea in 4 parti
		for (int i = 0; i < 4; i++) {
			
			// declaram o matrice prin care vom trimite un sfert de imagine
			int[][] pixels = new int[width][height];
			
			// alegem sfertul curent
			for (int j = height/4 * i; j < height/4 * (i + 1); j++) { 
				
				for (int k = 0; k < width; k++) {
					
					// retinem pixelul curent din imaginea initiala
					pixels[k][j] = Main.ImageInit.getRGB(k, j);
				}
			}
			
			// punem sfertul curent in Buffer
			buffer.put(pixels);
			
			System.out.println("Producatorul a pus sfertul cu numarul " + (i + 1) + " al imaginii.");
			
			try {
				
				sleep(1000);
			} catch (InterruptedException e) {
				
				System.out.println(e);
			}
		}
		
	}
}

