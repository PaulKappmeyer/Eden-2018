package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import enemies.Enemy;
import enemies.ZombieEnemy;
public class MapLoader {

	public static Map loadMap(File path) throws Exception {
		if(!path.exists()) throw new FileNotFoundException("This file could not be found");
		if(!path.isFile()) throw new Exception("The given path is not a file");
		System.out.println("Start load File: " + path.getAbsolutePath());
		
		Scanner scanner = new Scanner(path);
		
		//Metadata
		scanner.nextLine();
		int mapWidth = Integer.parseInt(scanner.nextLine().substring(6));  //The width of the map in number of tiles
		int mapHeight = Integer.parseInt(scanner.nextLine().substring(7)); //The height of the map in number of tiles
		int tileWidth = Integer.parseInt(scanner.nextLine().substring(10));  //The width of the tiles
		int tileHeight = Integer.parseInt(scanner.nextLine().substring(11));  //The height of the tiles
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();

		//Tiles
		Tile[][]tiles = new Tile[mapWidth][mapHeight];
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();
		
		String tileIds = scanner.nextLine();
		while(tileIds.endsWith(",")) {
			tileIds += scanner.nextLine();
		}
		String[] ids = tileIds.split(",");
		
		for (int i = 0; i < mapHeight; i++) {
			for (int j = 0; j < mapWidth; j++) {
				tiles[j][i] = new Tile(j * tileWidth, i * tileHeight, tileWidth, tileHeight, Integer.parseInt(ids[i*mapWidth + j]));
			}
		}
		
		//Objects
		int playerX = 0, playerY = 0;
		ArrayList<Obstacle>obstacles = new ArrayList<>();
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		
		scanner.nextLine();
		while (scanner.nextLine().equals("[Objekte]")) {
			String type = scanner.nextLine();

			//Player
			if(type.equals("# Player")) {
				scanner.nextLine();
				String[] data = scanner.nextLine().substring(9).split(",");
				playerX = Integer.parseInt(data[0]) * tileWidth;
				playerY = Integer.parseInt(data[1]) * tileHeight;
				scanner.nextLine();
			}
			else if(type.equals("# ZombieEnemy")) {
				scanner.nextLine();
				String[] data = scanner.nextLine().substring(9).split(",");
				int x = Integer.parseInt(data[0]) * tileWidth;
				int y = Integer.parseInt(data[1]) * tileHeight;
				enemies.add(new ZombieEnemy(x, y));
				scanner.nextLine();
			}
			else if(type.equals("# Stone")) {
				scanner.nextLine();
				String[] data = scanner.nextLine().substring(9).split(",");
				int x = Integer.parseInt(data[0]) * tileWidth;
				int y = Integer.parseInt(data[1]) * tileHeight;
				int w = Integer.parseInt(data[2]) * tileWidth;
				int h = Integer.parseInt(data[3]) * tileHeight;
				obstacles.add(new Stone(x, y, w, h));
				scanner.nextLine();
			}
			else if(type.equals("# Sign")) {
				scanner.nextLine();
				String[] data = scanner.nextLine().substring(9).split(",");
				int x = Integer.parseInt(data[0]) * tileWidth;
				int y = Integer.parseInt(data[1]) * tileHeight;
				int w = Integer.parseInt(data[2]) * tileWidth;
				int h = Integer.parseInt(data[3]) * tileHeight;
				String text[] = new String[]{scanner.nextLine().substring(5)};
				obstacles.add(new Sign(x, y, w, h, text));
				scanner.nextLine();
			}
			
			if(!scanner.hasNextLine()) break;
		}
		
		scanner.close();
		Map map = new Map(playerX, playerY, obstacles, enemies, tiles);
		return map;
	}
	
}
