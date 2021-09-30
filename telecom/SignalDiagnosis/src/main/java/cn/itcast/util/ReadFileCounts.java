package cn.itcast.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

import cn.itcast.entity.NWQuality;

@Component
public class ReadFileCounts {
	//返回文件的行数
	public int getFileCounts(String tableName) {
		String fileName = null;
		int count = 0;
		if (tableName.equals("NWQuality")) {
			//fileName ="C:/CNItcast/NWQuality.txt";
			fileName ="/opt/php/NWQuality.txt";
		}else if (tableName.equals("DataConnection")) {
			//fileName ="C:/CNItcast/DataConnection.txt";
			fileName ="/opt/php/DataConnection.txt";
		}else {
			//fileName ="C:/CNItcast/SignalStrength.txt";
			fileName ="/opt/php/SignalStrength.txt";
		}
		File file = new File(fileName);
		try {
			if (file.exists()) {
				InputStream input = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(input));
				String value = br.readLine();
				if (value != null) {
					while (value != null) {
						count ++;
						value = br.readLine();
					}
				}
				br.close();
				input.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}

	public  List<NWQuality> freshMapPoint(long nWquaCount) {


		RandomAccessFile accessFile=null;
		List<NWQuality> nwQualities=new ArrayList<NWQuality>();
		File file = new File("A:/NWQuality.txt");
		try {
			
			if (file.exists()) {
			accessFile=new RandomAccessFile(file, "rw");
			accessFile.seek(nWquaCount);
			
			String value=null;
			
			String[] strNWQuality=null;
			
				while ((value = accessFile.readLine())!= null) {
					strNWQuality=value.split("\t");
					NWQuality nWQuality=new NWQuality();
					nWQuality.setGpslat(strNWQuality[6]);
					nWQuality.setGpslon(strNWQuality[7]);
					System.out.println("@@@@@@@@@@@@@@@@@@@"+strNWQuality[6]+":"+strNWQuality[7]+":"+strNWQuality[14]);
					nWQuality.setNwOperator(strNWQuality[14]);
					nwQualities.add(nWQuality);
				}
			}
			//br.close();

			accessFile.close();
			//input.close();
			
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return nwQualities;
	}
	
public  long fileLength() {
		
	 RandomAccessFile randomFile=null;
	 File file=new File("A:/NWQuality.txt");
	try {
		if (file.exists()) {
			randomFile = new RandomAccessFile(file, "rw");
			
		}
		
		return  randomFile.length();
	} catch (FileNotFoundException e) {

		e.printStackTrace();
	} catch (IOException e) {

		e.printStackTrace();
	}
	return 0l;
    }

public List<NWQuality> freshMapOldPoint(long l, long nWquaCount) {
	
	//InputStream input=null;
			//BufferedReader br=null;
			RandomAccessFile accessFile=null;
			List<NWQuality> nwQualities=new ArrayList<NWQuality>();
			File file = new File("A:/NWQuality.txt");
			try {
				
				if (file.exists()) {
				//input = new FileInputStream(file);
				//br = new BufferedReader(new InputStreamReader(input));
				accessFile=new RandomAccessFile(file, "rw");
				accessFile.seek(l);
				
				String value=null;
				
				String[] strNWQuality=null;
				
					while ((value = accessFile.readLine())!= null) {
						if (accessFile.getFilePointer()<=nWquaCount) {
							strNWQuality=value.split("\t");
							NWQuality nWQuality=new NWQuality();
							nWQuality.setGpslat(strNWQuality[6]);
							nWQuality.setGpslon(strNWQuality[7]);
							System.out.println("#################"+strNWQuality[6]+":"+strNWQuality[7]+":"+strNWQuality[14]);
							nWQuality.setNwOperator(strNWQuality[14]);
							nwQualities.add(nWQuality);
						}else{
							break;
						}
						
					}
				}
				//br.close();
				//input.close();
				accessFile.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
			return nwQualities;
		}

	
	}
