package com.flashteens.jnbtTests.mapItem;

import static com.flashteens.jnbtTests.mapItem.io.Utils.childrenPathOf;
import static com.flashteens.jnbtTests.mapItem.io.Utils.relativePathOf;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.flashteens.jnbtTests.mapItem.exceptions.GUIGeneratedException;
import com.flashteens.jnbtTests.mapItem.exceptions.MapSplitException;
import com.flashteens.jnbtTests.mapItem.exceptions.UserAbortException;
import com.flashteens.jnbtTests.mapItem.gui.MapConversionProgressDialog;
import com.flashteens.jnbtTests.mapItem.gui.Progress;
import com.flashteens.jnbtTests.mapItem.io.MapItemWriter;

public class MapImageConverterMain {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			JOptionPane.showMessageDialog(null, "�Ъ����N����(.png)/�a���ɮ�(.dat)�첾�쥻�����ɤ���������!", //
					APP_TITLE, JOptionPane.WARNING_MESSAGE);
			return;
		}

		File fileArg = new File(args[0]);
		Map<String, List<File>> files = listAvailableFiles(args);
		int numMaps = files.get(MAPS).size();
		int numImgs = files.get(IMGS).size();
		int numOthers = files.get(OTHERS).size();

		if (numMaps == 0 && numImgs == 0) {
			if (numOthers == 0) {
				JOptionPane.showMessageDialog(null, "��Ƨ����䤣������ɮ�!", //
						APP_TITLE, JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "��Ƨ����䤣��i�Ϊ�����(.png)�Φa���ɮ�(.dat)!", //
						APP_TITLE, JOptionPane.ERROR_MESSAGE);
			}
			return;
		}

		String msgPre = "";
		if (numMaps > 0) {
			msgPre += "�z�ҿ�ܪ� " + numMaps + "�Ӧa����(.dat)�A�N�|�ഫ��PNG���ɡA���x�s�b�H�U�ؿ���:\n";
			msgPre += new File(getMap2ImgTargetPath(fileArg, fileArg)).getParent() + "\n\n";
		}
		if (numImgs > 0) {
			msgPre += "�z�ҿ�ܪ� " + numImgs + "�ӹϤ�(PNG/JPG/GIF)�A�N�|�ഫ���a����(.dat)�A���x�s�b�H�U�ؿ���:\n";
			msgPre += new File(getImg2MapTargetPath(fileArg, fileArg)).getParent() + "\n\n";
		}

		int confirm = JOptionPane.showConfirmDialog(null, msgPre + "�Ы��i�T�w�j�}�l�i���ഫ�C", //
				APP_TITLE, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

		if (confirm != JOptionPane.OK_OPTION) {
			return;
		}

		// TODO considering adding the resize feature

		boolean isAborted = false;
		try {
			convert(files, fileArg, true);
		} catch (UserAbortException e) {
			isAborted = true;
		}

		if (numImg2MapErrors == 0 && numMap2ImgErrors == 0) {
			JOptionPane.showMessageDialog(null, //
					(isAborted ? "���{������w���_�C" : "�Ҧ��a��/�Ϥ��Ҥw�ഫ���\!") + getSuccessInfo(), //
					APP_TITLE, JOptionPane.INFORMATION_MESSAGE);
		} else if (numImg2MapConverted == 0 && numMap2ImgConverted == 0) {
			JOptionPane.showMessageDialog(null, "�L�k�ഫ�ҫ��w���a��/�Ϥ��ɮ�!" + getSuccessInfo(), //
					APP_TITLE, JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, //
					(isAborted ? "���{������w���_�C" : "�����a��/�Ϥ����ഫ�o�Ͱ��D!") + getSuccessInfo(), //
					APP_TITLE, JOptionPane.WARNING_MESSAGE);
		}
		System.exit(0);
	}

	public static final String APP_TITLE = "Minecraft �a�Ϫ��~/�v������ �ഫ�{�� by FlashTeens";

	private static int numImg2MapConverted = 0;
	private static int numMap2ImgConverted = 0;
	private static int numImg2MapErrors = 0;
	private static int numMap2ImgErrors = 0;

	private static final String MAPS = "MAPS";
	private static final String IMGS = "IMGS";
	private static final String OTHERS = "OTHERS";

	private static final String MAP_PREFIX = "map_";

	public static Map<String, List<File>> listAvailableFiles(File... files) {
		Map<String, List<File>> list = new HashMap<>();
		list.put(IMGS, new ArrayList<>());
		list.put(MAPS, new ArrayList<>());
		list.put(OTHERS, new ArrayList<>());
		for (File file : files) {
			listAvailableFiles(file, list);
		}
		return list;
	}

	public static Map<String, List<File>> listAvailableFiles(String... files) {
		File[] fileArgs = new File[files.length];
		for (int i = 0; i < files.length; i++) {
			fileArgs[i] = new File(files[i]);
		}
		return listAvailableFiles(fileArgs);
	}

	private static void listAvailableFiles(File file, Map<String, List<File>> lists) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (File child : children) {
				listAvailableFiles(child, lists);
			}
		} else if (file.exists()) {
			String filename = file.getName();
			String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
			switch (ext) {
			case "dat":
				if (filename.startsWith(MAP_PREFIX)) {
					lists.get(MAPS).add(file);
				} else {
					lists.get(OTHERS).add(file);
				}
				break;
			case "png":
			case "jpg":
			case "jpeg":
			case "gif":
				lists.get(IMGS).add(file);
				break;
			default:
				lists.get(OTHERS).add(file);
			}
		}
	}

	static void convert(Map<String, List<File>> lists, File fileArg, //
			boolean enableProgressBarUI) throws UserAbortException {

		final MapConversionProgressDialog progressDialog;
		if (enableProgressBarUI) {
			progressDialog = new MapConversionProgressDialog();
			new Thread() {
				@Override
				public void run() {
					progressDialog.setVisible(true);
					progressDialog.setValue(0);
				}
			}.start();
		} else {
			progressDialog = null;
		}

		try {
			List<File> imgs = lists.get(IMGS);
			List<File> maps = lists.get(MAPS);

			Progress progress = new Progress(progressDialog);

			progress.setMessage("���b�p���ɮפj�p ...");
			Double[] progressWeights = calculateProgressWeights(imgs, maps);

			progress.pushTaskCount(getTotalTasksToPush(imgs, maps), progressWeights);

			// img2map
			for (File srcImgFile : imgs) {
				try {
					BufferedImage srcImg = ImageIO.read(srcImgFile);
					int w = srcImg.getWidth(), h = srcImg.getHeight();
					if (w == MapItemWriter.DEFAULT_MAP_WIDTH && h == MapItemWriter.DEFAULT_MAP_HEIGHT) {
						File dstMap = new File(getImg2MapTargetPath(srcImgFile, fileArg));
						dstMap.getParentFile().mkdirs();
						System.out.println("Converting to (img2map): " + dstMap);
						progress.setMessage("���b���ͦa���� '" + dstMap.getName() + "' ...");
						MapItemWriter.writeMapItem(srcImg, dstMap);
					} else if (w > 0 && h > 0 //
							&& w % MapItemWriter.DEFAULT_MAP_WIDTH == 0 //
							&& h % MapItemWriter.DEFAULT_MAP_HEIGHT == 0) {
						File dstMap = new File(getImg2SplitMapTargetFolderPath(srcImgFile, fileArg));
						dstMap.getParentFile().mkdirs();
						System.out.println("Converting to (img2map): " + dstMap);
						int numMapsExpected = (w / MapItemWriter.DEFAULT_MAP_WIDTH) * //
								(h / MapItemWriter.DEFAULT_MAP_HEIGHT);
						Object startNumInput;
						boolean isInputNotCompletedYet = true;
						int startNum;
						while (isInputNotCompletedYet) {
							try {
								startNumInput = JOptionPane.showInputDialog(progressDialog, //
										"�o�{���ɡu" + srcImgFile + "�v���e�Ҭ� 128 ������ " //
												+ "(" + w + "x" + h + ")�A" //
												+ "�N�|�Q�ഫ�� " + numMapsExpected + " �i�a���ɮסG\n" //
												+ "map_<N>.png ~ map_<N+" + (numMapsExpected - 1) + ">.png\n" //
												+ "�п�J�z�һݭn���_�l�� N " //
												+ "(�ݬ��D�t��ơA�B N+" + (numMapsExpected - 1) + " ����W�L 32767):", //
										APP_TITLE, JOptionPane.INFORMATION_MESSAGE, null, //
										null, "" + generateMapNum(srcImgFile.getName()));
								if (startNumInput != null && startNumInput instanceof String) {
									try {
										startNum = Integer.parseInt((String) startNumInput, 10);
										if (startNum >= Short.MAX_VALUE - numMapsExpected + 1 || //
												startNum < 0) {
											throw new GUIGeneratedException( //
													"�Ъ`�N�G�z�ҿ�J����ƥ����ŦX�d��A�_�h�L�k�b Minecraft �����Q����I");
										}
										isInputNotCompletedYet = false;
										dstMap.mkdirs();
									} catch (NumberFormatException e) {
										throw new GUIGeneratedException( //
												"�Ъ`�N�G�z������J��ơA�_�h�L�k�b Minecraft �����Q����I");
									}
								} else {
									throw new MapSplitException(MapSplitException.USER_CANCELLED, //
											new IOException(srcImgFile.toString()));
								}
								MapItemWriter.writeSplitMapItem(srcImg, dstMap, (short) startNum, progress);
							} catch (GUIGeneratedException e) {
								JOptionPane.showMessageDialog(progressDialog, e.getMessage(), //
										APP_TITLE, JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						throw new MapSplitException(MapSplitException.INVALID_IMG_SIZE, //
								new IOException(srcImgFile.toString()));
					}
					numImg2MapConverted++;
				} catch (IOException e) {
					numImg2MapErrors++;
					showErrorMessageIfNotIgnored(e, progressDialog);
				} catch (MapSplitException e) {
					numImg2MapErrors++;
					showErrorMessageIfNotIgnored(e, progressDialog);
				}
				progress.doneTask();
			}

			// map2img
			for (File srcMap : maps) {
				File dstImg = new File(getMap2ImgTargetPath(srcMap, fileArg));
				System.out.println("Converting to (map2img): " + dstImg);
				progress.setMessage("���b���͹Ϥ��� '" + dstImg.getName() + "' ...");
				try {
					dstImg.getParentFile().mkdirs();
					MapItemWriter.saveAsImage(srcMap, dstImg);
					numMap2ImgConverted++;
				} catch (IOException e) {
					numMap2ImgErrors++;
				}
				progress.doneTask();
			}

			progress.popTaskCount();

			progress.doneTask();

		} finally {
			if (progressDialog != null) {
				progressDialog.setVisible(false);
			}
		}
	}

	private static boolean isErrorMsgIgnored = false;
	private static final String ERRMSG_OPTION_CONT = "�~��";
	private static final String ERRMSG_OPTION_CONT_IGNORE = "�~��(���A��ܿ��~�T��)";
	private static final String ERRMSG_OPTION_ABORT = "���_����";
	private static final String[] ERRMSGS = //
			{ ERRMSG_OPTION_CONT, ERRMSG_OPTION_CONT_IGNORE, ERRMSG_OPTION_ABORT };

	private static void showErrorMessageIfNotIgnored(IOException e, Component parentGUI) {
		e.printStackTrace();
		if (isErrorMsgIgnored) {
			return;
		}
		handleErrorMsg(JOptionPane.showOptionDialog(parentGUI, "�o�Ϳ��~�G" + e.getMessage(), //
				APP_TITLE, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, //
				ERRMSGS, ERRMSG_OPTION_CONT));
	}

	private static void showErrorMessageIfNotIgnored(MapSplitException e, Component parentGUI) {
		e.printStackTrace();
		if (isErrorMsgIgnored) {
			return;
		}
		String msg;
		Throwable cause;
		switch (e.getMessage()) {
		case MapSplitException.INVALID_IMG_SIZE:
			msg = "�v���j�p (���פμe��) �������� 128 �����ơI";
			cause = e.getCause();
			if (cause != null && cause.getMessage() != null) {
				msg = "�ഫ���ɡu" + cause.getMessage() + "�v�o�Ϳ��~�C\n" + msg;
			}
			break;
		case MapSplitException.USER_CANCELLED:
			cause = e.getCause();
			if (cause != null && cause.getMessage() != null) {
				msg = "�w�����ഫ�ɮסu" + cause.getMessage() + "�v�C";
			}
			msg = "�w�����ഫ���ɮסC";
			break;
		default:
			msg = "�o�Ϳ��~�G" + e.getMessage();
		}
		handleErrorMsg(JOptionPane.showOptionDialog(parentGUI, msg, //
				APP_TITLE, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, //
				ERRMSGS, ERRMSG_OPTION_CONT));
	}

	private static void handleErrorMsg(int selectedErrMsgOption) {
		switch (ERRMSGS[selectedErrMsgOption]) {
		case ERRMSG_OPTION_CONT_IGNORE:
			isErrorMsgIgnored = true;
			break;
		case ERRMSG_OPTION_ABORT:
			throw new UserAbortException();
		}
	}

	private static Double[] calculateProgressWeights(List<File> imgFiles, List<File> mapFiles) {
		ArrayList<Double> weights = new ArrayList<>();
		for (int i = 0; i < imgFiles.size(); i++) {
			int xNum, yNum;
			try {
				BufferedImage img = ImageIO.read(imgFiles.get(i));
				xNum = img.getWidth() / MapItemWriter.DEFAULT_MAP_WIDTH;
				yNum = img.getHeight() / MapItemWriter.DEFAULT_MAP_HEIGHT;
			} catch (IOException e) {
				xNum = yNum = 1;
			}
			weights.add(Math.max(xNum * yNum, 0.0));
		}
		for (int i = 0; i < mapFiles.size(); i++) {
			weights.add(0.2);
		}
		// System.out.println(weights.toString());
		return weights.toArray(new Double[weights.size()]);
	}

	private static int getTotalTasksToPush(List<File> imgFiles, List<File> mapFiles) {
		return imgFiles.size() + mapFiles.size();
	}

	private static String getImg2MapTargetPath(File source, File fileArg) {
		String workpath = fileArg.getParentFile().toString();
		String targetPath = childrenPathOf(workpath, "FTMCMapImgConv", "img2map", //
				relativePathOf(workpath, source.getPath()));
		// targetPath == "xxx/yyy/zzz/FTMCMapImgConv/img2map/someImgFile.png"
		int dotPos = targetPath.lastIndexOf('.');
		dotPos = (dotPos >= 0) ? dotPos : targetPath.length();
		return targetPath.substring(0, dotPos) + ".dat";
	}

	private static String getImg2SplitMapTargetFolderPath(File source, File fileArg) {
		String workpath = fileArg.getParentFile().toString();
		String targetPath = childrenPathOf(workpath, "FTMCMapImgConv", "img2map", //
				relativePathOf(workpath, source.getPath()));
		// targetPath == "xxx/yyy/zzz/FTMCMapImgConv/img2map/someImgFile.png"
		int dotPos = targetPath.lastIndexOf('.');
		dotPos = (dotPos >= 0) ? dotPos : targetPath.length();
		return targetPath.substring(0, dotPos) + File.separator;
	}

	private static String getMap2ImgTargetPath(File source, File fileArg) {
		String workpath = fileArg.getParentFile().toString();
		String targetPath = childrenPathOf(workpath, "FTMCMapImgConv", "map2img", //
				relativePathOf(workpath, source.getPath()));
		// targetPath == "xxx/yyy/zzz/FTMCMapImgConv/map2img/someMapFile.dat"
		int dotPos = targetPath.lastIndexOf('.');
		dotPos = (dotPos >= 0) ? dotPos : targetPath.length();
		return targetPath.substring(0, dotPos) + ".png";
	}

	private static short generateMapNum(String filename) {
		try {
			String filenameTrimmed = filename.substring(filename.lastIndexOf(File.separator) + 1);
			if (filenameTrimmed.startsWith(MAP_PREFIX)) {
				filenameTrimmed = filenameTrimmed.substring(MAP_PREFIX.length());
			}
			short num = Short.parseShort(getBeginningDigits(filenameTrimmed), 10);
			return (short) Math.max(num, 0);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private static String getBeginningDigits(String str) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= '0' && c <= '9') {
				builder.append(c);
			} else {
				break;
			}
		}
		return builder.toString();
	}

	private static String getSuccessInfo() {
		return new StringBuilder() //
				.append("\n") //
				.append("�Ϥ� �� �a����(.dat): ") //
				.append("���\: " + numImg2MapConverted + ", ") //
				.append("����: " + numImg2MapErrors + ",\n") //
				.append("�a�� �� �Ϥ���(.png): ") //
				.append("���\: " + numMap2ImgConverted + ", ") //
				.append("����: " + numMap2ImgErrors + ",\n") //
				.append("\n\n").toString();
	}

	/** �ƥ� */
	static String getCurrentJARPath() {
		// Ref: https://stackoverflow.com/questions/320542
		try {
			return MapImageConverterMain.class.getProtectionDomain() //
					.getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			return "";
		}
	}

}
