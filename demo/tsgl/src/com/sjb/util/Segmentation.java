package com.sjb.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.nio.channels.FileChannel;

/**
 * 文件分割
 * @author fxc
 *
 */
public class Segmentation {
	
	public static final String code = "UTF-8";
	
	public static final String suffix = ".txt";
	
	public static final int size = 10000;//分割大小
	
	
	/**
	 * 按行分割文件
	 * @param rows 为多少行一个文件 
	 * @param sourceFilePath 为源文件路径 
	 * @param targetDirectoryPath 文件分割后存放的目标目录
	 */
	public static void splitDataToSaveFile(int rows, String sourceFilePath,
			String targetDirectoryPath) {	
		File sourceFile = new File(sourceFilePath);
		File targetFile = new File(targetDirectoryPath);
		if (!sourceFile.exists() || rows <= 0 || sourceFile.isDirectory()) {
			return;
		}
		if (targetFile.exists()) {
			if (!targetFile.isDirectory()) {
				return;
			}
		} else {
			targetFile.mkdirs();
		}
		try {
			InputStreamReader in = new InputStreamReader(new FileInputStream(sourceFilePath),code);
			BufferedReader br=new BufferedReader(in);
			BufferedWriter bw = null;
			StringBuffer str = new StringBuffer();//最大上限为2147483647(Integer.MAX_VALUE). 2个G的char, 4个G字节. 当然要保证JVM 内存够用了.
			String tempData = br.readLine();
			int i = 1, s = 1;
			while (tempData != null) {
				str.append(tempData + "\r\n");
				if (i % rows == 0) {//合整的页
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream( 
							targetFile.getAbsolutePath() + "/" +  sourceFile.getName().split("\\.")[0] +"_" + s +suffix), code),str.length()); 
					bw.write(str.toString());
					bw.close();
					str = new StringBuffer();
					s++;
				}
				i++;
				tempData = br.readLine();
			}
			if ((i - 1) % rows != 0) {//（剩下最后）不合整的页
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream( 
						targetFile.getAbsolutePath() + "/" +  sourceFile.getName().split("\\.")[0] +"_" + s +suffix), code),str.length()); 
				bw.write(str.toString());
				bw.close();
				br.close();
				s++;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String getpath = "D:/file/yyy.txt";
		String putpath = "D:/file/fg";
		splitDataToSaveFile(size,getpath,putpath);
		
//		String path = "D:/file/xxx";
//		String name = "xx.txt";
//		String newPathName = "D:/file/xxx/all.txt";
//		try {
//			merge(path,name,newPathName);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 合并文件
	 * @param path 需要合并文件的根目录
	 * @param name 需要合并的文件名
	 * @param newPathName 需要合并到这个文件中
	 * @throws IOException 
	 */
	public static void merge(String path,String name,String newPathName) throws IOException{
		File sourceFile = new File(newPathName);
		if (!sourceFile.exists()) {
			sourceFile.createNewFile();
		}else{//清空文件
			FileWriter fileWriter =new FileWriter(sourceFile);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
		}
		
		File rootPath = new File(path);
		
		File[] files = rootPath.listFiles();// 获取目录下的所有文件或文件夹
		if (files != null) {// 如果目录为空，直接退出
			FileOutputStream out = new FileOutputStream(sourceFile);
			try {
				// 遍历，目录下的所有文件
				for (File f : files) {
					if (f.isDirectory()) {//是否文件夹
						File child = new File(path+"/"+f.getName()+"/"+name);//需要合并的子文件
						if(child.exists()){
							FileInputStream fc = new FileInputStream(child);
							byte[] b = new byte[(int) child.length()];
							fc.read(b);
							out.write(b);
							out.flush();
							fc.close();
						}else{
							throw new IOException("======注意:"+path+"/"+f.getName()+"/"+name+"不存在");
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				out.close();
			}
		}
	}

}
