package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author riemann
 * @date 2019/05/24 23:42
 */
public class HttpURLConnectionUtil {

	/**
	 * Http get����
	 * 
	 * @param httpUrl ����
	 * @return ��Ӧ����
	 */
	public static String doGet(String httpUrl) {
		// ����
		HttpURLConnection connection = null;
		InputStream is = null;
		BufferedReader br = null;
		StringBuffer result = new StringBuffer();
		try {
			// ��������
			URL url = new URL(httpUrl);
			connection = (HttpURLConnection) url.openConnection();
			// ��������ʽ
			connection.setRequestMethod("GET");
			// �������ӳ�ʱʱ��
			connection.setReadTimeout(15000);
			// ��ʼ����
			connection.connect();
			// ��ȡ��Ӧ����
			if (connection.getResponseCode() == 200) {
				// ��ȡ���ص�����
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
			// �ر�Զ������
			connection.disconnect();
		}
		return result.toString();
	}

	/**
	 * Http post����
	 * 
	 * @param httpUrl ����
	 * @param param   ����
	 * @return
	 */
	public static String doPost(String httpUrl, String param) {
		String result = null;
		// ����
		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			// �������Ӷ���
			URL url = new URL(httpUrl);
			// ��������
			connection = (HttpURLConnection) url.openConnection();
			// �������󷽷�
			connection.setRequestMethod("POST");
			// �������ӳ�ʱʱ��
			connection.setConnectTimeout(15000);
			// ���ö�ȡ��ʱʱ��
			connection.setReadTimeout(15000);
			// DoOutput�����Ƿ���httpUrlConnection�����DoInput�����Ƿ��httpUrlConnection���룬���ⷢ��post�����������������
			// �����Ƿ�ɶ�ȡ
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// ����ͨ�õ���������
//            connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36");
//            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			connection.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");

			// ƴװ����
			if (null != param && param.equals("")) {
				// ���ò���
				os = connection.getOutputStream();
				// ƴװ����
				os.write(param.getBytes("UTF-8"));
			}
			// ����Ȩ��
			// ��������ͷ��
			// ��������
			// connection.connect();
			// ��ȡ��Ӧ
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
			} else {
				result = connection.getResponseCode() + "";
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// �ر�����
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
			// �ر�����
			connection.disconnect();
		}
		return result.toString();
	}

	public static String doPostProxy(String httpUrl, String param, String proxyIp, int proxyPort) {
		String result = null;
		// ����
		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		BufferedReader br = null;
		HttpGet httpget =null;
		try {


			HttpHost proxy222 = new HttpHost(proxyIp, proxyPort, "HTTP");

			RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy222).build();

			// 实例化CloseableHttpClient对象
			CloseableHttpClient httpclient2 = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();

			httpget = new HttpGet(
					httpUrl);
			
//			httpget.setHeader("User-Agent",
//					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
//			httpget.setHeader("Proxy-Connection", "Keep-Alive");
//			httpget.setHeader("Accept",
//					"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
//			httpget.setHeader("Accept-Encoding", "gzip, deflate");
//			httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.9");

			CloseableHttpResponse response = httpclient2.execute(httpget);
			System.out.println(response.getStatusLine().getStatusCode());

			if (response.getStatusLine().getStatusCode() == 200) {
				is = response.getEntity().getContent();
				ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
				byte[] buff = new byte[100];
				int rc = 0;
				while ((rc = is.read(buff, 0, 100)) > 0) {
					swapStream.write(buff, 0, rc);
				}
				byte[] in2b = swapStream.toByteArray();

				result = bytes2Hex(in2b);
			} else {
				result = response.getStatusLine().getStatusCode() + "";
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// �ر�����
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
			// �ر�����
			if(httpget!=null) {
				httpget.releaseConnection();
			}
			
		}
		return result.toString();
	}

	public static String bytes2Hex(byte[] src) {
		if (src == null || src.length <= 0) {
			return null;
		}

		char[] res = new char[src.length * 2]; // ÿ��byte��Ӧ�����ַ�
		final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		for (int i = 0, j = 0; i < src.length; i++) {
			res[j++] = hexDigits[src[i] >> 4 & 0x0f]; // �ȴ�byte�ĸ�4λ
			res[j++] = hexDigits[src[i] & 0x0f]; // �ٴ�byte�ĵ�4λ
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