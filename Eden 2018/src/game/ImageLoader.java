package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;

public class ImageLoader {

	public static BufferedImage loadImage(File path) throws Exception{
		if(!path.exists()) throw new FileNotFoundException("This file could not be found");
		if(!path.isFile()) throw new Exception("The given path is not a file");
		System.out.println("Start loading Image: " + path.getAbsolutePath());
		
		BufferedImage image = ImageIO.read(path);
		return image;
	}
	
}
