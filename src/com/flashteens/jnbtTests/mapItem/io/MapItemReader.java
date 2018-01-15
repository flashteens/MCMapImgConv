package com.flashteens.jnbtTests.mapItem.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.ShortTag;

import com.flashteens.jnbtTests.regionFile.RegionReader;

public class MapItemReader {

	public static BufferedImage read(File file) throws IOException {
		try {
			CompoundTag mapTag = (CompoundTag) RegionReader.readNBT_gzip(file);
			CompoundTag dataTag = (CompoundTag) mapTag.getValue().get("data");
			byte[] colors = ((ByteArrayTag) dataTag.getValue().get("colors")).getValue();
			short w = ((ShortTag) dataTag.getValue().get("width")).getValue();
			short h = ((ShortTag) dataTag.getValue().get("height")).getValue();
			return read(colors, w, h);
		} catch (RuntimeException e) {
			throw new IOException(e);
		}
	}

	public static BufferedImage read(byte[] colors, short w, short h) {
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
		img.setRGB(0, 0, w, h, colorIndex2RGB(colors), 0, w);
		return img;
	}

	private static int[] colorIndex2RGB(byte[] arr) {
		if (arr == null)
			return null;
		int[] intArr = new int[arr.length];
		for (int i = 0; i < arr.length; i++)
			intArr[i] = colorIndex2RGB(arr[i]);
		return intArr;
	}

	public static int colorIndex2RGB(byte index) {
		return ColorConverter.index2rgb(index);
	}

}
