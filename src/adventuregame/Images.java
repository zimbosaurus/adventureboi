package adventuregame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Images {

	//directories
	private static final String[] directories = new String[] {
		"assets/sprites",
		"assets/animated_sprites",
	};

	public String[] getDirectories() {
		return directories;
	}

	//images
	private static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();

	//paths to images <Name, Path>
	private static HashMap<String, String> paths = new HashMap<String, String>();

	/** Index images from all directories into 'paths' hashmap. */
	public static void indexAllImages() {
		for (String dir : directories) {
			indexDirectory(dir);
		}
	}

	/** Index all images in specified directory, and put them into 'paths' hashmap. */
	private static void indexDirectory(String s) {
		File dir = new File(s);

		File[] files = dir.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".png");
			}
		});

		for (File f : files) {
			paths.put(f.getName(), dir.getPath());
		}
	}

	public static void printPaths() {
		for (String s : paths.keySet()) {
			System.out.println(paths.get(s) +  "\\" + s);
		}
	}
	
	/** Load images from 'paths' into 'images' */
	public static void loadAllImages() {
		try {
			for (String name : paths.keySet()) {
				String path = paths.get(name);
				images.put(name, ImageIO.read(new File(path + "\\" + name)));
			}
		} catch (Exception e) {e.printStackTrace();}
	}

	public static BufferedImage getImage(String key) {
		if (!key.endsWith(".png")) {
			key = key + ".png";
		}
		System.out.println(key);
		return images.get(key);
	}
}
