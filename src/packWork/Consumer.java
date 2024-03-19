package packWork;
import packMain.Main;

public class Consumer extends Thread {
	private Buffer buffer;
	
	Consumer() {
		super();
	}
	public Consumer(Buffer buf) {
		this.buffer=buf;
		System.out.println("S-a apelat constructorul pentru Consumer");
	}
	
	public void run () {
		
		//retinem latimea imaginii
		int width = Main.ImageInit.getWidth();
		
		// retinem inaltimea imaginii
		int height = Main.ImageInit.getHeight();
		
		// impartim imaginea in 4 parti
		for (int i = 0; i < 4; i++) {
	
			// declaram matricea in care se stocheaza sfertul de imagine primit
			int[][] pixels = new int[width][height/4+3];
	
			// preluam sfertul curent din Buffer
			pixels = buffer.get();
			
			// alegem sfertul curent
			for (int j = height/4 * i; j < height/4 * (i + 1); j++) {
				 
				
				for (int k = 0; k < width; k++) {
					// setam pixelul curent in imaginea pe care o vom trimite spre procesare
					Main.preProcessingImage.setRGB(k, j, pixels[k][j]);
				}
			}
			
			System.out.println("Consumatorul a preluat sfertul cu numarul " + (i + 1) + " al imaginii.");
			try {
	
				sleep(1000);
				
			} catch (InterruptedException e) {
				
				System.out.println(e);
			}
		}
		
	}
	
}
