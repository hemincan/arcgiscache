package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author riemann
 * @date 2019/05/24 23:42
 */
public class HttpURLConnectionUtil {

	/**
	 * Http get请求
	 * 
	 * @param httpUrl 连接
	 * @return 响应数据
	 */
	public static String doGet(String httpUrl) {
		// 链接
		HttpURLConnection connection = null;
		InputStream is = null;
		BufferedReader br = null;
		StringBuffer result = new StringBuffer();
		try {
			// 创建连接
			URL url = new URL(httpUrl);
			connection = (HttpURLConnection) url.openConnection();
			// 设置请求方式
			connection.setRequestMethod("GET");
			// 设置连接超时时间
			connection.setReadTimeout(15000);
			// 开始连接
			connection.connect();
			// 获取响应数据
			if (connection.getResponseCode() == 200) {
				// 获取返回的数据
				is = connection.getInputStream();
				if (null != is) {
					br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					String temp = null;
					while (null != (temp = br.readLine())) {
						result.append(temp);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 关闭远程连接
			connection.disconnect();
		}
		return result.toString();
	}

	/**
	 * Http post请求
	 * 
	 * @param httpUrl 连接
	 * @param param   参数
	 * @return
	 */
	public static String doPost(String httpUrl, String param) {
		String result = null;
		// 连接
		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			// 创建连接对象
			URL url = new URL(httpUrl);
			// 创建连接
			connection = (HttpURLConnection) url.openConnection();
			// 设置请求方法
			connection.setRequestMethod("POST");
			// 设置连接超时时间
			connection.setConnectTimeout(15000);
			// 设置读取超时时间
			connection.setReadTimeout(15000);
			// DoOutput设置是否向httpUrlConnection输出，DoInput设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
			// 设置是否可读取
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// 设置通用的请求属性
//            connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36");
//            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			connection.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");

			// 拼装参数
			if (null != param && param.equals("")) {
				// 设置参数
				os = connection.getOutputStream();
				// 拼装参数
				os.write(param.getBytes("UTF-8"));
			}
			// 设置权限
			// 设置请求头等
			// 开启连接
			// connection.connect();
			// 读取响应
			System.err.println(connection.getResponseCode());
			if (connection.getResponseCode() == 200) {
				is = connection.getInputStream();
				ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
				byte[] buff = new byte[100];
				int rc = 0;
				while ((rc = is.read(buff, 0, 100)) > 0) {
					swapStream.write(buff, 0, rc);
				}
				byte[] in2b = swapStream.toByteArray();
//                for (int i = 0; i < in2b.length; i++) {
//					System.err.println(in2b[i]);
//				}
//				String strString = new String(in2b);
				
//				System.err.println();
				result = bytes2Hex(in2b);
			}else {
				result = connection.getResponseCode()+"";
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 关闭连接
			connection.disconnect();
		}
		return result.toString();
	}

	public static String bytes2Hex(byte[] src) {
		if (src == null || src.length <= 0) {
			return null;
		}

		char[] res = new char[src.length * 2]; // 每个byte对应两个字符
		final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		for (int i = 0, j = 0; i < src.length; i++) {
			res[j++] = hexDigits[src[i] >> 4 & 0x0f]; // 先存byte的高4位
			res[j++] = hexDigits[src[i] & 0x0f]; // 再存byte的低4位
		}

		return new String(res);
	}

	public static void main(String[] args) {
		String message = doPost(
				"http://t5.tianditu.com/img_w/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=img&tileMatrixSet=w&TileMatrix=15&TileRow=14133&TileCol=26211&style=default&format=tiles&tk=d12861d485701e869992f652021881a7",
				"");
		System.out.println(message);
	}
}