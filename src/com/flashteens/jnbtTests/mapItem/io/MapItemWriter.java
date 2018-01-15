package com.flashteens.jnbtTests.mapItem.io;

import static com.flashteens.jnbtTests.mapItem.io.Utils.childrenPathOf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jnbt.ByteArrayTag;
import org.jnbt.ByteTag;
import org.jnbt.CompoundTag;
import org.jnbt.NBTOutputStream;
import org.jnbt.ShortTag;
import org.jnbt.Tag;

import com.flashteens.jnbtTests.mapItem.exceptions.MapSplitException;
import com.flashteens.jnbtTests.mapItem.gui.Progress;

public class MapItemWriter {

	public static final byte MAP_SCALE = 3;
	public static final byte MAP_DIMENSION = 10;
	public static final boolean MAP_ALLOW_TRACKPOS = false;
	public static final boolean MAP_UNLIMITED_TRACKING = false;
	public static final short MAP_XCENTER = 0;
	public static final short MAP_ZCENTER = 0;

	public static final int DEFAULT_MAP_WIDTH = 128;
	public static final int DEFAULT_MAP_HEIGHT = 128;

	public static void saveAsMapItem(File source, File target) throws IOException {
		writeMapItem(MapItemReader.read(source), target);
	}

	public static void writeImage(BufferedImage img, File target) throws IOException {
		ImageIO.write(img, "png", target);
	}

	public static void saveAsImage(File source, File target) throws IOException {
		writeImage(MapItemReader.read(source), target);
	}

	public static void writeMapItem(BufferedImage img, File target) throws IOException {
		writeMapItem(img, target, 0, 0, img.getWidth(), img.getHeight());
	}

	public static void writeSplitMapItem(BufferedImage img, File targetFolder, short startNum) //
			throws IOException, MapSplitException {
		writeSplitMapItem(img, targetFolder, startNum, null);
	}

	public static void writeSplitMapItem(BufferedImage img, File targetFolder, short startNum, //
			Progress progress) throws IOException, MapSplitException {
		int imgWidth = img.getWidth(), imgHeight = img.getHeight();
		int mapWidth = DEFAULT_MAP_WIDTH, mapHeight = DEFAULT_MAP_HEIGHT;
		if (imgWidth % mapWidth != 0 || imgHeight % mapHeight != 0) {
			throw new MapSplitException(MapSplitException.INVALID_IMG_SIZE);
		}

		int mapNum = startNum;

		if (progress == null) {
			//just to avoid NullPointerEception
			progress = new Progress();
		}

		progress.pushTaskCount((imgWidth / mapWidth) * (imgHeight / mapHeight));
		for (int x = 0; x < imgWidth; x += mapWidth) {
			for (int y = 0; y < imgHeight; y += mapHeight, mapNum++) {
				if (mapNum > ((int) Short.MAX_VALUE)) {
					throw new MapSplitException(MapSplitException.MAP_ID_OVERFLOW);
				}
				File target = new File(childrenPathOf(targetFolder.toString(), "map_" + mapNum + ".dat"));
				System.out.println("Now exporting '" + target.getName() + "' ...");
				progress.setMessage("正在產生地圖檔 '" + target.getName() + "' ...");
				writeMapItem(img, target, x, y, mapWidth, mapHeight);
				progress.doneTask();
			}
		}
		progress.popTaskCount();
	}

	public static void writeMapItem(BufferedImage img, File target, //
			int x, int y, int w, int h) throws IOException {
		int[] rgb = img.getRGB(x, y, w, h, new int[w * h], 0, w);

		Map<String, Tag> dataMap = new HashMap<>();
		String key;
		dataMap.put(key = "scale", new ByteTag(key, MAP_SCALE));
		dataMap.put(key = "dimension", new ByteTag(key, MAP_DIMENSION));
		dataMap.put(key = "height", new ShortTag(key, (short) h));
		dataMap.put(key = "width", new ShortTag(key, (short) w));
		dataMap.put(key = "trackingPosition", new ByteTag(key, (byte) (MAP_ALLOW_TRACKPOS ? 1 : 0)));
		dataMap.put(key = "unlimitedTracking", new ByteTag(key, (byte) (MAP_UNLIMITED_TRACKING ? 1 : 0)));
		dataMap.put(key = "xCenter", new ShortTag(key, MAP_XCENTER));
		dataMap.put(key = "zCenter", new ShortTag(key, MAP_ZCENTER));
		dataMap.put(key = "colors", new ByteArrayTag(key, colorRGB2Index(rgb)));

		Map<String, Tag> rootMap = new HashMap<>();
		rootMap.put("data", new CompoundTag("data", dataMap));

		CompoundTag outTag = new CompoundTag("", rootMap);
		NBTOutputStream out = new NBTOutputStream(new FileOutputStream(target));
		out.writeTag(outTag);
		out.close();
	}

	private static byte[] colorRGB2Index(int[] arr) {
		if (arr == null)
			return null;
		byte[] byteArr = new byte[arr.length];
		for (int i = 0; i < arr.length; i++)
			byteArr[i] = colorIndex2RGB(arr[i]);
		return byteArr;
	}

	public static byte colorIndex2RGB(int index) {
		return ColorConverter.rgb2index(index);
	}

	static void test_map2img(String mapNo) throws IOException {
		File source = new File("mapItems/test/map_" + mapNo + ".dat");
		File target = new File("mapItems/test/map_" + mapNo + "_out.dat");
		File targetImg = new File("mapItems/test/map_" + mapNo + ".png");
		File targetAgainImg = new File("mapItems/test/map_" + mapNo + "_out.png");

		saveAsMapItem(source, target);
		saveAsImage(source, targetImg);
		saveAsImage(target, targetAgainImg);
	}

	static void test_img2map(String mapNo) throws IOException {
		File source = new File("mapItems/test/map_" + mapNo + ".png");
		File targetMap = new File("mapItems/test/map_" + mapNo + ".dat");
		File target = new File("mapItems/test/map_" + mapNo + "_out.png");

		BufferedImage sourceImg = ImageIO.read(source);
		writeMapItem(sourceImg, targetMap);
		saveAsImage(targetMap, target);
	}

	public static void main(String[] args) throws IOException {
		// test_map2img("200old");
		// test_img2map("200");
		test_img2map("1234");
	}

}
