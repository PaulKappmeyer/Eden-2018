/*
 * 
 */
package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;

/**
 * 
 * @author Paul
 *
 */
public final class ImageLoader {

	public static BufferedImage loadImage(File path) throws Exception{
		if (!path.exists()) {
			throw new FileNotFoundException("This file could not be found: " + path.getAbsolutePath());
		}
		if (!path.isFile()) {
			throw new Exception("The given path is not a file: " + path.getAbsolutePath());
		}
		System.out.println("Start loading Image: " + path.getAbsolutePath());
		
		BufferedImage image = ImageIO.read(path);
		return image;
	}
	
}
