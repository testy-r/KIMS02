package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	public List<DataSet> buildDatabase(String path) {
		//Erstellt die Datensaetze die zur Datenbasis (oder Testbasis) hinzugefuegt werden
		//Laedt Bilder aus den fuenf vorgegebenen Ordnern (Chevron, EinSieben, Kreis, Kreuz, Strich)
		//Der Pfad zu dem Ordner in dem sie sich befinden muessen beim Aufruf angegeben werden
		
		File dirDatabase = new File(path);
		
		List<DataSet> database = new ArrayList<DataSet>();
		BufferedImage image = null;
		
		for(File dir : dirDatabase.listFiles()) {
			for(File file : dir.listFiles()) {
				try {
					image = ImageIO.read(file);					
					database.add(new DataSet(getGrayscale(image), getTarget(dir), dir.getName()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return database;
	}
	
	public List<Double> getGrayscale(BufferedImage image) {
		
		Color color;
		
		List<Double> temp = new ArrayList<Double>();
		
		for(int i = 0; i < image.getHeight(); i++) {
			for(int j = 0; j < image.getWidth(); j++) {
				color = new Color(image.getRGB(i, j));
				temp.add(1 - ((double)(color.getRed() + color.getGreen() + color.getBlue())/765.0));
			}
		}
		
		return temp;
	}
	
	public double[] getTarget(File dir) {
		//Gibt je nach Kategorie ein anderes Target aus
		//Aufgrund der Struktur gibt dann der erste Output-Neuron an ob es ein Chevron ist, der zweite ob es ein EinSieben ist usw.
		
		switch(dir.getName()) {
		case "Chevron":		return new double[]{1.0, 0.0, 0.0, 0.0, 0.0};
		case "EinSieben":	return new double[]{0.0, 1.0, 0.0, 0.0, 0.0};
		case "Kreis":		return new double[]{0.0, 0.0, 1.0, 0.0, 0.0};
		case "Kreuz":		return new double[]{0.0, 0.0, 0.0, 1.0, 0.0};
		case "Strich":		return new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
		default:			return null;
		}
	}

}
