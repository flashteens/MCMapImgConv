package com.flashteens.jnbtTests.regionFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.jnbt.CompoundTag;
import org.jnbt.LongTag;
import org.jnbt.NBTInputStream;
import org.jnbt.Tag;

public class RegionReader {
	
	public static Tag readNBT_gzip(File file) throws IOException {
		byte[] data = readRawFile(file);
		return readNBT_gzip(data);
	}
	
	public static Tag readNBT_raw(File file) throws IOException {
		byte[] data = readRawFile(file);
		return readNBT_raw(data);
	}
	
	public static Tag readNBT_raw(InputStream in) throws IOException {
		byte[] data = readRawFileFromInputStream(in);
		return readNBT_raw(data);
	}
	
	public static RegionFile readRegionFile(File file) throws IOException {
		return new RegionFile(file);
	}
	
	
	
	public static byte[] readRawFile(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		return readRawFileFromInputStream(in);
	}
	
	public static byte[] readRawFileFromInputStream(InputStream in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int readBytes = 0;
		while((readBytes = in.read(buffer)) >= 0){
			bos.write(buffer, 0, readBytes);
		}
		in.close();
		return bos.toByteArray();
	}
	
	public static Tag readNBT_gzip(byte[] data) throws IOException {
		NBTInputStream reader = new NBTInputStream(new ByteArrayInputStream(data));
		Tag tag = reader.readTag();
		reader.close();
		return tag;
	}
	public static Tag readNBT_zlib(byte[] data) throws IOException {
		return readNBT_gzip(gzipCompress(zlibDecompress(data)));
	}
	public static Tag readNBT_raw(byte[] data) throws IOException {
		return readNBT_gzip(gzipCompress(data));
	}
	
	public static byte[] gzipCompress(byte[] data) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream out = new GZIPOutputStream(bos);
		out.write(data);
		out.close();
		return bos.toByteArray();
	}
	
	public static byte[] gzipDecompress(byte[] data) throws IOException {
		GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(data));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int readBytes = 0;
		while((readBytes = in.read(buffer)) >= 0){
			bos.write(buffer, 0, readBytes);
		}
		return bos.toByteArray();
	}
	
	public static byte[] zlibDecompress(byte[] data) throws IOException {
		DeflaterInputStream in = new DeflaterInputStream(new ByteArrayInputStream(data));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int readBytes = 0;
		while((readBytes = in.read(buffer)) >= 0){
			bos.write(buffer, 0, readBytes);
		}
		return bos.toByteArray();
	}
	
	public static void test001() throws IOException{
		Tag nbt = readNBT_gzip(new File(System.getenv("APPDATA")
				+ "/.minecraft/saves/Republic of FlashTeens (1_12_new)/"
				+ "region/r.0.0.mca"
				//+ "data/idcounts.dat"
				));
		System.out.println(nbt);
	}
	
	public static void test002() throws IOException{
		byte[] data = readRawFile(new File(System.getenv("APPDATA")
				+ "/.minecraft/saves/Republic of FlashTeens (1_12_new)/"
				+ "region/r.0.0.mca"
				));
		byte[] encodedData = gzipCompress(data);
		byte[] decodedData = gzipDecompress(encodedData);
		System.out.println("Original Size = " + data.length);
		System.out.println("Compressed Size = " + encodedData.length);
		System.out.println("Decompressed Size = " + decodedData.length);
		System.out.println("Checking original==decompressed? (" + Arrays.equals(data, decodedData) + ")");
	}
	
	public static void test003() throws IOException, ClassCastException{
		RegionFile region = readRegionFile(new File(System.getenv("APPDATA")
				+ "/.minecraft/saves/Republic of FlashTeens (1_12_new)/"
				+ "region/r.0.0.mca"
				));
		DataInputStream chunk_in = region.getChunkDataInputStream(0, 0);
		Tag chunk_tag = readNBT_raw(chunk_in);
		
		System.out.println(chunk_tag);
		
		FileOutputStream logFileOut = new FileOutputStream("debug.txt");
		PrintWriter pw = new PrintWriter(logFileOut);
		pw.println(chunk_tag);
		pw.close();
		
		Map<String, Tag> chunk_data = ((CompoundTag)chunk_tag).getValue();
		Map<String, Tag> level_data = ((CompoundTag)chunk_data.get("Level")).getValue();
		long inhabitedTime = ((LongTag)level_data.get("InhabitedTime")).getValue();
		
		System.out.println();
		System.out.println("InhabitedTime: " + inhabitedTime);
		System.out.println("See \"debug.txt\" for the complete readable NBT message.");
	}
	
	
	public static void main(String[] args) throws IOException{
		test003();
	}
	
}
