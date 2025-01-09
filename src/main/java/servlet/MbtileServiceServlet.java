package servlet;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import util.MBTilesUtils;
import util.ServerConfig;

/**
 * Servlet implementation class AgsOffTilesServiceServlet
 */
@WebServlet(description = "get arcgis offline tile", urlPatterns = { "/mbtiles" })
public class MbtileServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, MBTilesUtils> layer2Mbtile = new HashMap<String, MBTilesUtils>();
	private String rootPath = "";
	private static String base64Blank = "iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAYAAABccqhmAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NkZFQUUzNjgyRjJBMTFFNEFBQ0JGMEMyRjFFNUE0MUYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NkZFQUUzNjkyRjJBMTFFNEFBQ0JGMEMyRjFFNUE0MUYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo2RkVBRTM2NjJGMkExMUU0QUFDQkYwQzJGMUU1QTQxRiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo2RkVBRTM2NzJGMkExMUU0QUFDQkYwQzJGMUU1QTQxRiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pij7ZYAAAAJdSURBVHja7NQxAQAACMMwwL/nYQAHJBJ6tJMU8NNIAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAGAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAYABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAEABgAYAGAAgAGAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAQAGABgAYACAAYABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAAYAGABgAIABAJcVYADnygT9CIf4ngAAAABJRU5ErkJggg==";

	/**
	 * Default constructor.
	 */
	public MbtileServiceServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		long maxAgeInSeconds = 3600*24*30;
		// 设置缓存控制头部，如"Cache-Control: max-age=3600"
	    response.setHeader("Cache-Control", "public,max-age=" + maxAgeInSeconds);
	 
	    // 设置过期时间头部，如"Expires: Thu, 01 Dec 1994 16:00:00 GMT"
	    long expiresTime = System.currentTimeMillis() + maxAgeInSeconds * 1000;
	    response.setDateHeader("Expires", expiresTime);
		
//		System.err.println("?????????????????");
		// TODO Auto-generated method stub
		String x = request.getParameter("x");
		String y = request.getParameter("y");
		String z = request.getParameter("z");
		String layer = request.getParameter("layer");
//		无瓦片时返回什么，可以设置返回404
		String notFound404 = request.getParameter("notFound404");
		MBTilesUtils mbtile = null;
		if(layer2Mbtile.containsKey(layer)) {
			 mbtile = layer2Mbtile.get(layer);
			 if(mbtile.getIsClose()==true) {
				 mbtile = null;
			 }
		}
		if(mbtile==null) {
			 mbtile = new MBTilesUtils(ServerConfig.getMbtilesBasePath()+"/"+layer);
				layer2Mbtile.put(layer, mbtile);
		}
		
		
		byte[] b= mbtile.getTiles(Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z));
		OutputStream os = response.getOutputStream();
		byte[] output = null;
		int outlength = 0;
		if(b==null) {
			if("yes".equals(notFound404)) {
//				客户端要求瓦片为空时返回404
				response.sendError(404);
			}else {
				byte[] blankImg = Base64.decodeBase64(base64Blank);
				InputStream is = new ByteArrayInputStream(blankImg);
				int count = 0;
				while ((count = is.read(blankImg)) != -1) {
					output = blankImg;
					outlength = count;
				}
				os.write(output, 0, outlength);
			}
		}else {
			output = b;
			outlength = b.length;
			os.write(output, 0, outlength);
		}
		
		
		
		os.flush();
		os.close();
	}

	
}

