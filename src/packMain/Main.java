package packMain;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import packWork.Buffer;
import packWork.Consumer;
import packWork.Producer;

public class Main {
	
	//imaginea citita din fisier 
	public static BufferedImage ImageInit = null;
	
	//imaginea inainte de procesare dupa trimiterea in sferturi
	public static BufferedImage preProcessingImage = null; 
	
	//imaginea procesata
	public static BufferedImage ImageFin = null; 

	public static void main(String[] args) {
		String FileInput = null;
		String FileOutput = null;
		try {
			
			//locatia imaginii initiale 
			FileInput = args[0]; 
			
			//locatia imaginii modificate
			FileOutput = args[1]; 
			
		} catch (ArrayIndexOutOfBoundsException e) {
			
			// eroarea in cazul in care nu s-au declarat in RunConfiguration locatiile fisierelor
            System.out.println("Argumente insuficiente la rulare");
            
        }
		
		
		File file = null;
		
		try {
			
			file = new File(FileInput); 
			
			// citirea imaginii initiale
			ImageInit = ImageIO.read(file); 
			
		} catch(IOException e) {
			
			// eroarea in cazul in care nu s-a citit imaginea
			System.out.println("Error: " + e);
		}
		
		preProcessingImage = new BufferedImage(
				ImageInit.getWidth(), ImageInit.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		//convertirea imaginii in RGB pentru a permite procesarea ei
		ColorConvertOp op = new ColorConvertOp(ImageInit.getColorModel().getColorSpace(), preProcessingImage.getColorModel().getColorSpace(), null);
        op.filter(ImageInit, preProcessingImage);

		
		ImageFin = new BufferedImage(
                ImageInit.getWidth(), ImageInit.getHeight(), BufferedImage.TYPE_INT_RGB); 
		
		// declararea unui obiect de tip buffer
		Buffer buffer = new Buffer(); 
		
		// declararea unui obiect de tip producer
		Producer producer = new Producer(buffer); 
		
		// declararea unui obiect de tip consumer
		Consumer consumer = new Consumer(buffer); 
		
		// inceputul thread-ul pentru producer
		producer.start();
		
		// inceputul thread-ul pentru consumer
		consumer.start(); 
		
		try {
			
			// unire thread-ului curent cu cel al consumer-ului,astfel nu se executa main-ul pana ce consumerul nu isi termina propria executie
			consumer.join();
			
		} catch (InterruptedException e) {
			
			// afisearea erorii in cazul in care nu s-a reusit unirea celor doua thread-uri
			e.printStackTrace(); 
		}
		
		
		// algoritmul normalize color
		
		// se parcurge fiecare pixel al imaginii
		for(int i = 0; i < preProcessingImage.getWidth(); ++i) {
			for(int j = 0; j < preProcessingImage.getHeight(); ++j) {
				  int pixel = preProcessingImage.getRGB(i, j);

				  
				  	// se extrage valoarea culorii pixelului curent
			        int red = (pixel >> 16) & 0xff;
			        int green = (pixel >> 8) & 0xff;
			        int blue = (pixel) & 0xff;

			        // se calculeaza media celor trei valor
			        int average = (red + green + blue) / 3;

			        // centrarea distributiei culorii in jurul lui zero
			        red -= average;
			        green -= average;
			        blue -= average;
			        
			        //marirea intensitatii culorilor
			        red *= 4;
	               green *= 4;
	                blue *= 4;
	                
	                //fixarea valorilor in intervalul valid (0.255)
	                red = Math.max(0, Math.min(255, red));
	                green = Math.max(0, Math.min(255, green));
	                blue = Math.max(0, Math.min(255, blue));

	                //setarea noii culori pentru pixelul curent
			        int newPixel = (red << 16) | (green << 8) | blue;

			        ImageFin.setRGB(i, j, newPixel);
			}
		}
		
		
		//scrierea noii imagini in fisier
		try {
			file = new File(FileOutput); 
			
			ImageIO.write(ImageFin, "bmp", file);
			
			System.out.println("\nScrierea noii imagini a fost efectuata cu succes!");
			 
			
		} catch(IOException e) {
			
			// mesajul in cazul aparitiei unei erori la scrierea in fisier
			System.out.println("Error: " + e); 
		}

	}
}
