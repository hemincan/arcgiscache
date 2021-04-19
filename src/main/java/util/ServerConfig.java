package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * ������ŷ�������Դ�Ļ���·��
 * @author hemincan
 * @date:   2018��9��14�� ����10:08:02
 */
public  class ServerConfig {


	
	private static String arcgisBasePath;
	
	public static String getArcgisBasePath() {
		if(arcgisBasePath == null){
			arcgisBasePath = getProper("arcgisBasePath");
		}		
		return arcgisBasePath;
	}
	
	public static String getProper(String name){
		String value;
		Properties properties = new Properties();
		String configPath = Thread.currentThread().getContextClassLoader().getResource("config.properties").getPath();
		try {
			properties.load(new FileInputStream(configPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		value = properties.getProperty(name);
		try {
			value = new String(value.getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	
}
