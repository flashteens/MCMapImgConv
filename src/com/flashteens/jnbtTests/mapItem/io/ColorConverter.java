package com.flashteens.jnbtTests.mapItem.io;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorConverter {

	/**
	 * The color array according to <a href=
	 * "https://minecraft.gamepedia.com/Map_item_format#1.12_Color_Table">this
	 * page on Minecraft Wiki</a>.
	 */
	private static final Integer[] index2rgbArray = { //
			0, //
			0, //
			0, //
			0, //
			rgb(89, 125, 39), //
			rgb(109, 153, 48), //
			rgb(127, 178, 56), //
			rgb(67, 94, 29), //
			rgb(174, 164, 115), //
			rgb(213, 201, 140), //
			rgb(247, 233, 163), //
			rgb(130, 123, 86), //
			rgb(140, 140, 140), //
			rgb(171, 171, 171), //
			rgb(199, 199, 199), //
			rgb(105, 105, 105), //
			rgb(180, 0, 0), //
			rgb(220, 0, 0), //
			rgb(255, 0, 0), //
			rgb(135, 0, 0), //
			rgb(112, 112, 180), //
			rgb(138, 138, 220), //
			rgb(160, 160, 255), //
			rgb(84, 84, 135), //
			rgb(117, 117, 117), //
			rgb(144, 144, 144), //
			rgb(167, 167, 167), //
			rgb(88, 88, 88), //
			rgb(0, 87, 0), //
			rgb(0, 106, 0), //
			rgb(0, 124, 0), //
			rgb(0, 65, 0), //
			rgb(180, 180, 180), //
			rgb(220, 220, 220), //
			rgb(255, 255, 255), //
			rgb(135, 135, 135), //
			rgb(115, 118, 129), //
			rgb(141, 144, 158), //
			rgb(164, 168, 184), //
			rgb(86, 88, 97), //
			rgb(106, 76, 54), //
			rgb(130, 94, 66), //
			rgb(151, 109, 77), //
			rgb(79, 57, 40), //
			rgb(79, 79, 79), //
			rgb(96, 96, 96), //
			rgb(112, 112, 112), //
			rgb(59, 59, 59), //
			rgb(45, 45, 180), //
			rgb(55, 55, 220), //
			rgb(64, 64, 255), //
			rgb(33, 33, 135), //
			rgb(100, 84, 50), //
			rgb(123, 102, 62), //
			rgb(143, 119, 72), //
			rgb(75, 63, 38), //
			rgb(180, 177, 172), //
			rgb(220, 217, 211), //
			rgb(255, 252, 245), //
			rgb(135, 133, 129), //
			rgb(152, 89, 36), //
			rgb(186, 109, 44), //
			rgb(216, 127, 51), //
			rgb(114, 67, 27), //
			rgb(125, 53, 152), //
			rgb(153, 65, 186), //
			rgb(178, 76, 216), //
			rgb(94, 40, 114), //
			rgb(72, 108, 152), //
			rgb(88, 132, 186), //
			rgb(102, 153, 216), //
			rgb(54, 81, 114), //
			rgb(161, 161, 36), //
			rgb(197, 197, 44), //
			rgb(229, 229, 51), //
			rgb(121, 121, 27), //
			rgb(89, 144, 17), //
			rgb(109, 176, 21), //
			rgb(127, 204, 25), //
			rgb(67, 108, 13), //
			rgb(170, 89, 116), //
			rgb(208, 109, 142), //
			rgb(242, 127, 165), //
			rgb(128, 67, 87), //
			rgb(53, 53, 53), //
			rgb(65, 65, 65), //
			rgb(76, 76, 76), //
			rgb(40, 40, 40), //
			rgb(108, 108, 108), //
			rgb(132, 132, 132), //
			rgb(153, 153, 153), //
			rgb(81, 81, 81), //
			rgb(53, 89, 108), //
			rgb(65, 109, 132), //
			rgb(76, 127, 153), //
			rgb(40, 67, 81), //
			rgb(89, 44, 125), //
			rgb(109, 54, 153), //
			rgb(127, 63, 178), //
			rgb(67, 33, 94), //
			rgb(36, 53, 125), //
			rgb(44, 65, 153), //
			rgb(51, 76, 178), //
			rgb(27, 40, 94), //
			rgb(72, 53, 36), //
			rgb(88, 65, 44), //
			rgb(102, 76, 51), //
			rgb(54, 40, 27), //
			rgb(72, 89, 36), //
			rgb(88, 109, 44), //
			rgb(102, 127, 51), //
			rgb(54, 67, 27), //
			rgb(108, 36, 36), //
			rgb(132, 44, 44), //
			rgb(153, 51, 51), //
			rgb(81, 27, 27), //
			rgb(17, 17, 17), //
			rgb(21, 21, 21), //
			rgb(25, 25, 25), //
			rgb(13, 13, 13), //
			rgb(176, 168, 54), //
			rgb(215, 205, 66), //
			rgb(250, 238, 77), //
			rgb(132, 126, 40), //
			rgb(64, 154, 150), //
			rgb(79, 188, 183), //
			rgb(92, 219, 213), //
			rgb(48, 115, 112), //
			rgb(52, 90, 180), //
			rgb(63, 110, 220), //
			rgb(74, 128, 255), //
			rgb(39, 67, 135), //
			rgb(0, 153, 40), //
			rgb(0, 187, 50), //
			rgb(0, 217, 58), //
			rgb(0, 114, 30), //
			rgb(91, 60, 34), //
			rgb(111, 74, 42), //
			rgb(129, 86, 49), //
			rgb(68, 45, 25), //
			rgb(79, 1, 0), //
			rgb(96, 1, 0), //
			rgb(112, 2, 0), //
			rgb(59, 1, 0), //
			rgb(147, 124, 113), //
			rgb(180, 152, 138), //
			rgb(209, 177, 161), //
			rgb(110, 93, 85), //
			rgb(112, 57, 25), //
			rgb(137, 70, 31), //
			rgb(159, 82, 36), //
			rgb(84, 43, 19), //
			rgb(105, 61, 76), //
			rgb(128, 75, 93), //
			rgb(149, 87, 108), //
			rgb(78, 46, 57), //
			rgb(79, 76, 97), //
			rgb(96, 93, 119), //
			rgb(112, 108, 138), //
			rgb(59, 57, 73), //
			rgb(131, 93, 25), //
			rgb(160, 114, 31), //
			rgb(186, 133, 36), //
			rgb(98, 70, 19), //
			rgb(72, 82, 37), //
			rgb(88, 100, 45), //
			rgb(103, 117, 53), //
			rgb(54, 61, 28), //
			rgb(112, 54, 55), //
			rgb(138, 66, 67), //
			rgb(160, 77, 78), //
			rgb(84, 40, 41), //
			rgb(40, 28, 24), //
			rgb(49, 35, 30), //
			rgb(57, 41, 35), //
			rgb(30, 21, 18), //
			rgb(95, 75, 69), //
			rgb(116, 92, 84), //
			rgb(135, 107, 98), //
			rgb(71, 56, 51), //
			rgb(61, 64, 64), //
			rgb(75, 79, 79), //
			rgb(87, 92, 92), //
			rgb(46, 48, 48), //
			rgb(86, 51, 62), //
			rgb(105, 62, 75), //
			rgb(122, 73, 88), //
			rgb(64, 38, 46), //
			rgb(53, 43, 64), //
			rgb(65, 53, 79), //
			rgb(76, 62, 92), //
			rgb(40, 32, 48), //
			rgb(53, 35, 24), //
			rgb(65, 43, 30), //
			rgb(76, 50, 35), //
			rgb(40, 26, 18), //
			rgb(53, 57, 29), //
			rgb(65, 70, 36), //
			rgb(76, 82, 42), //
			rgb(40, 43, 22), //
			rgb(100, 42, 32), //
			rgb(122, 51, 39), //
			rgb(142, 60, 46), //
			rgb(75, 31, 24), //
			rgb(26, 15, 11), //
			rgb(31, 18, 13), //
			rgb(37, 22, 16), //
			rgb(19, 11, 8) //
	};

	private static int rgb(int r, int g, int b) {
		return (255 << 24) | (r << 16) | (g << 8) | b;
	}

	private static final List<Integer> index2rgbList = //
			Arrays.asList(index2rgbArray);

	private static final Map<Integer, Integer> rgb2indexMap = makeInverseMap(index2rgbArray);

	public static int index2rgb(byte index) {
		int unsignedIndex = ((int) index) & 0xff;
		return (unsignedIndex >= index2rgbArray.length) ? 0 : index2rgbArray[unsignedIndex];
	}

	public static byte rgb2index(int rgb) {
		int minResultRgb = index2rgbList.parallelStream().min( //
				(rgb1, rgb2) -> {
					double d1 = rgbDist(rgb, rgb1);
					double d2 = rgbDist(rgb, rgb2);
					return Double.compare(d1, d2);
				}).orElse(0);
		Integer result = rgb2indexMap.get(minResultRgb);
		if (result == null)
			result = 0;
		return (byte) (int) result;
	}

	public static int quantizeRGB(int rgb) {
		return index2rgb(rgb2index(rgb));
	}

	private static double rgbDist(int rgb1, int rgb2) {
		int a1 = (rgb1 >> 24) & 0xff, a2 = (rgb2 >> 24) & 0xff;
		int r1 = (rgb1 >> 16) & 0xff, r2 = (rgb2 >> 16) & 0xff;
		int g1 = (rgb1 >> 8) & 0xff, g2 = (rgb2 >> 8) & 0xff;
		int b1 = (rgb1 >> 0) & 0xff, b2 = (rgb2 >> 0) & 0xff;
		int a = a1 - a2, r = r1 - r2, g = g1 - g2, b = b1 - b2;
		return Math.sqrt(a * a + r * r + g * g + b * b);
	}

	/**
	 * @throws NullPointerException
	 *             if {@code arr} is null.
	 */
	private static <T> Map<T, Integer> makeInverseMap(T[] arr) {
		HashMap<T, Integer> map = new HashMap<>();
		for (int i = 0; i < arr.length; i++) {
			T key = arr[i];
			if (!map.containsKey(key)) {
				map.put(key, i);
			}
		}
		return map;
	}

}
