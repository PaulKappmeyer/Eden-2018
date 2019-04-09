package game;

import java.awt.image.BufferedImage;
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
		System.out.println("Start loading Map: " + path.getAbsolutePath());

		Scanner scanner = new Scanner(path);

		//-------------------------------------------------------------Metadata
		scanner.nextLine();
		int mapWidth = Integer.parseInt(scanner.nextLine().substring(6));  //The width of the map in number of tiles
		int mapHeight = Integer.parseInt(scanner.nextLine().substring(7)); //The height of the map in number of tiles
		int tileWidth = Integer.parseInt(scanner.nextLine().substring(10));  //The width of the tiles
		int tileHeight = Integer.parseInt(scanner.nextLine().substring(11));  //The height of the tiles
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();
		
		//-------------------------------------------------------------Tileset
		String tileset_path = scanner.nextLine();
		tileset_path = tileset_path.substring(10);
		while (!tileset_path.endsWith(".png")) {
			tileset_path = tileset_path.substring(0, tileset_path.length()-1);
		}
		for (int i = tileset_path.length() - 1; i >= 0; i--) {
			if(tileset_path.charAt(i) == '/') {
				tileset_path = tileset_path.substring(i + 1, tileset_path.length());
				break;
			}
		}
		tileset_path = ".\\src\\gfx\\" + tileset_path;
		scanner.nextLine();
		
		BufferedImage tileset = ImageLoader.loadImage(new File(tileset_path));
		int tileset_height = tileset.getHeight() / tileHeight;
		int tileset_width = tileset.getWidth() / tileWidth;
		BufferedImage[]tileset_tiles = new BufferedImage[tileset_width * tileset_height];
		try {
			for (int y = 0; y < tileset_height; y++) {
				for (int x = 0; x < tileset_width; x++) {
					int index = y*tileset_width + x;
					tileset_tiles[index] = tileset.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//-------------------------------------------------------------Tiles
		Tile[][]tiles = new Tile[mapWidth][mapHeight];
		scanner.nextLine();
		scanner.nextLine();
		scanner.nextLine();

		String tileIds = scanner.nextLine();
		while(tileIds.endsWith(",")) {
			tileIds += scanner.nextLine();
		}
		String[] ids = tileIds.split(",");
		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				int index = y*mapWidth + x;
				int id = Integer.parseInt(ids[index]);
				tiles[x][y] = new Tile(x * tileWidth, y * tileHeight, tileWidth, tileHeight, id, tileset_tiles[id-1]);
			}
		}

		//-------------------------------------------------------------Objects
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
