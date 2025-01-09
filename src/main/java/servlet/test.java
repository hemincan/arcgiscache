package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class test
 */
@WebServlet(description = "get arcgis offline tile", urlPatterns = { "/testtest" })
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public test() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.err.println("???????");
//		HttpURLConnection connection = null;
//		InputStream is = null;
//		BufferedReader br = null;
//		StringBuffer result = new StringBuffer();
//		try {
//			// 创建连接
//			URL url = new URL("http://t0.tianditu.gov.cn/img_w/wmts?tk=d12861d485701e869992f652021881a7");
//			connection = (HttpURLConnection) url.openConnection();
//			// 设置请求方式

		// 连接
		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		BufferedReader br = null;
		Map<String, String[]> map = request.getParameterMap();
		for (String kk : map.keySet()) {
			System.err.println(kk + "  " + map.get(kk)[0].toString());
		}
		StringBuffer result = new StringBuffer();
		try {
			// 创建连接对象
			URL url = new URL(
					"http://t0.tianditu.gov.cn/img_w/wmts?tk=d12861d485701e869992f652021881a7&SERVICE=WMTS&VERSION=1.0.0&REQUEST=GetCapabilities");
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

//			// 拼装参数
//			if (null != param && param.equals("")) {
//				// 设置参数
//				os = connection.getOutputStream();
//				// 拼装参数
//				os.write(param.getBytes("UTF-8"));
//			}
			// 设置权限
			// 设置请求头等
			// 开启连接
			// connection.connect();
			// 读取响应
			System.err.println(connection.getResponseCode());
			if (connection.getResponseCode() == 200) {
				InputStream is2 = connection.getInputStream();

				response.setHeader("Content-Type", "text/xml;charset=UTF-8");
//				// 获取返回的数据
//				// 将字节流转化成字符流，并指定字符集
//				InputStreamReader isr = new InputStreamReader(is2, "UTF-8");
//				// 将字符流以缓存的形式一行一行输出
//				BufferedReader bf = new BufferedReader(isr);
//				String results = "";
//				String newLine = "";
//				while ((newLine = bf.readLine()) != null) {
//					System.err.println(newLine);
//					results += newLine + "\n";
//				}
//				System.err.println(results);
				response.getWriter().write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Capabilities    xsi:schemaLocation=\"http://www.opengis.net/wmts/1.0 http://schemas.opengis.net/wmts/1.0.0/wmtsGetCapabilities_response.xsd\"    version=\"1.0.0\" xmlns=\"http://www.opengis.net/wmts/1.0\"    xmlns:ows=\"http://www.opengis.net/ows/1.1\"    xmlns:gml=\"http://www.opengis.net/gml\"    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">    <ows:ServiceIdentification>        <ows:Title>在线地图服务</ows:Title>        <ows:Abstract>基于OGC标准的地图服务</ows:Abstract>        <ows:Keywords>            <ows:Keyword>OGC</ows:Keyword>        </ows:Keywords>        <ows:ServiceType codeSpace=\"wmts\"/>        <ows:ServiceTypeVersion>1.0.0</ows:ServiceTypeVersion>        <ows:Fees>none</ows:Fees>        <ows:AccessConstraints>none</ows:AccessConstraints>    </ows:ServiceIdentification>    <ows:ServiceProvider>        <ows:ProviderName>天地图有限公司</ows:ProviderName>        <ows:ProviderSite>http://www.tianditu.com</ows:ProviderSite>        <ows:ServiceContact>            <ows:IndividualName>Mr Liu</ows:IndividualName>            <ows:PositionName>Software Engineer</ows:PositionName>            <ows:ContactInfo>                <ows:Phone>                    <ows:Voice>010-88187700</ows:Voice>                    <ows:Facsimile>010-88187700</ows:Facsimile>                </ows:Phone>                <ows:Address>                    <ows:DeliveryPoint>北京市顺义区国家地理信息科技产业园天地图大厦（机场东路与龙塘路交叉口）</ows:DeliveryPoint>                    <ows:City>北京市</ows:City>                    <ows:AdministrativeArea>北京市</ows:AdministrativeArea>                    <ows:Country>中国</ows:Country>                    <ows:PostalCode>101399</ows:PostalCode>                    <ows:ElectronicMailAddress>tianditu.com</ows:ElectronicMailAddress>                </ows:Address>                <ows:OnlineResource xlink:type=\"simple\" xlink:href=\"http://www.tianditu.com\"/>            </ows:ContactInfo>        </ows:ServiceContact>    </ows:ServiceProvider>    <ows:OperationsMetadata>        <ows:Operation name=\"GetCapabilities\">            <ows:DCP>                <ows:HTTP>                    <ows:Get xlink:href=\"http://t0.tianditu.gov.cn/img_w/wmts?\">                        <ows:Constraint name=\"GetEncoding\">                            <ows:AllowedValues>                                <ows:Value>KVP</ows:Value>                            </ows:AllowedValues>                        </ows:Constraint>                    </ows:Get>                </ows:HTTP>            </ows:DCP>        </ows:Operation>        <ows:Operation name=\"GetTile\">            <ows:DCP>                <ows:HTTP>                    <ows:Get xlink:href=\"http://t0.tianditu.gov.cn/img_w/wmts?\">                        <ows:Constraint name=\"GetEncoding\">                            <ows:AllowedValues>                                <ows:Value>KVP</ows:Value>                            </ows:AllowedValues>                        </ows:Constraint>                    </ows:Get>                </ows:HTTP>            </ows:DCP>        </ows:Operation>    </ows:OperationsMetadata>    <Contents>        <Layer>            <ows:Title>img</ows:Title>            <ows:Abstract>img</ows:Abstract>            <ows:Identifier>img</ows:Identifier>            <ows:WGS84BoundingBox>                <ows:LowerCorner>-20037508.3427892 -20037508.3427892</ows:LowerCorner>                <ows:UpperCorner>20037508.3427892 20037508.3427892</ows:UpperCorner>            </ows:WGS84BoundingBox>            <ows:BoundingBox>                <ows:LowerCorner>-20037508.3427892 -20037508.3427892</ows:LowerCorner>                <ows:UpperCorner>20037508.3427892 20037508.3427892</ows:UpperCorner>            </ows:BoundingBox>            <Style>                <ows:Identifier>default</ows:Identifier>            </Style>            <Format>tiles</Format>            <TileMatrixSetLink>                <TileMatrixSet>w</TileMatrixSet>            </TileMatrixSetLink>        </Layer>        <TileMatrixSet>            <ows:Identifier>w</ows:Identifier>            <ows:SupportedCRS>urn:ogc:def:crs:EPSG::900913</ows:SupportedCRS>            <TileMatrix>                <ows:Identifier>1</ows:Identifier>                <ScaleDenominator>2.958293554545656E8</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>2</MatrixWidth>                <MatrixHeight>2</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>2</ows:Identifier>                <ScaleDenominator>1.479146777272828E8</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>4</MatrixWidth>                <MatrixHeight>4</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>3</ows:Identifier>                <ScaleDenominator>7.39573388636414E7</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>8</MatrixWidth>                <MatrixHeight>8</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>4</ows:Identifier>                <ScaleDenominator>3.69786694318207E7</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>16</MatrixWidth>                <MatrixHeight>16</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>5</ows:Identifier>                <ScaleDenominator>1.848933471591035E7</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>32</MatrixWidth>                <MatrixHeight>32</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>6</ows:Identifier>                <ScaleDenominator>9244667.357955175</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>64</MatrixWidth>                <MatrixHeight>64</MatrixHeight>            </TileMatrix>			    <TileMatrix>                <ows:Identifier>7</ows:Identifier>                <ScaleDenominator>4622333.678977588</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>128</MatrixWidth>                <MatrixHeight>128</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>8</ows:Identifier>                <ScaleDenominator>2311166.839488794</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>256</MatrixWidth>                <MatrixHeight>256</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>9</ows:Identifier>                <ScaleDenominator>1155583.419744397</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>512</MatrixWidth>                <MatrixHeight>512</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>10</ows:Identifier>                <ScaleDenominator>577791.7098721985</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>1024</MatrixWidth>                <MatrixHeight>1024</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>11</ows:Identifier>                <ScaleDenominator>288895.85493609926</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>2048</MatrixWidth>                <MatrixHeight>2048</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>12</ows:Identifier>                <ScaleDenominator>144447.92746804963</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>4096</MatrixWidth>                <MatrixHeight>4096</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>13</ows:Identifier>                <ScaleDenominator>72223.96373402482</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>8192</MatrixWidth>                <MatrixHeight>8192</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>14</ows:Identifier>                <ScaleDenominator>36111.98186701241</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>16384</MatrixWidth>                <MatrixHeight>16384</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>15</ows:Identifier>                <ScaleDenominator>18055.990933506204</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>32768</MatrixWidth>                <MatrixHeight>32768</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>16</ows:Identifier>                <ScaleDenominator>9027.995466753102</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>65536</MatrixWidth>                <MatrixHeight>65536</MatrixHeight>            </TileMatrix>            <TileMatrix>                <ows:Identifier>17</ows:Identifier>                <ScaleDenominator>4513.997733376551</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>131072</MatrixWidth>                <MatrixHeight>131072</MatrixHeight>				</TileMatrix>            <TileMatrix>                <ows:Identifier>18</ows:Identifier>                <ScaleDenominator>2256.998866688275</ScaleDenominator>                <TopLeftCorner>20037508.3427892 -20037508.3427892</TopLeftCorner>                <TileWidth>256</TileWidth>                <TileHeight>256</TileHeight>                <MatrixWidth>262144</MatrixWidth>                <MatrixHeight>262144</MatrixHeight>            </TileMatrix>        </TileMatrixSet>    </Contents></Capabilities>");
//				ServletOutputStream out = response.getOutputStream();
//				byte[] buffer = new byte[1024];
//				int len = is.read(buffer);
//				while (len != -1) {
//				    out.write(buffer, 0, len);
//				    len = is.read(buffer);
//				}
//				out.close();
//				if (null != is) {
//					br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//					String temp = null;
//					while (null != (temp = br.readLine())) {
//						result.append(temp);
//					}
//				}
			} else {

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
		System.err.println(result.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
