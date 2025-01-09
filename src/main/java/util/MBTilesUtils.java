package util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class MBTilesUtils {

	private static final ConcurrentHashMap<String, MBTilesUtils> INSTANCES = new ConcurrentHashMap<String, MBTilesUtils>();

	private final Connection conn;

	private final PreparedStatement ps;

	public Connection getConn() {
		return conn;
	}

	public MBTilesUtils(String db) {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		if (db == null || !new File(db).exists()) {
			throw new RuntimeException("No database");
		}

		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + db);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			ps = conn.prepareStatement(
					"SELECT tile_data FROM tiles " + "WHERE zoom_level = ? AND tile_column = ? " + "AND tile_row = ?;");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
//		Statement stmt;
//		try {
//			stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM tiles;");
//			while (rs.next()) {
//				String  name = rs.getString("zoom_level");
//				System.out.println(name);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

//	static Map<String, String> getDatabases() {
//		Properties configuration = new Properties();
//		try {
//			configuration.load(MBTilesUtils.class.getClassLoader()
//					.getResourceAsStream("mbtiles4j.properties"));
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//
//		HashMap<String, String> result = new HashMap<String, String>();
//
//		String dbs = configuration.getProperty("tile-dbs");
//		if (StringUtils.isEmpty(dbs)) {
//			return result;
//		}
//
//		String[] split = dbs.split(Pattern.quote(","));
//		for (String entry : split) {
//			String path = configuration.getProperty(entry + ".path");
//			if (!StringUtils.isEmpty(path)) {
//				result.put(entry, path);
//			}
//		}
//
//		return result;
//	}

//	public static MBTilesUtils getInstance(String db) {
//		return INSTANCES.get(db);
//	}

	public synchronized byte[] getTiles(int x, int y, int z) {

		int index = 1;

		ResultSet rs = null;
		try {
			ps.setInt(index++, z);
			ps.setInt(index++, x);
			ps.setInt(index++, y);

			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getBytes(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public boolean getIsClose() {
		try {
			return conn.isClosed();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private synchronized void close() {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

//	public static void connect() {
//		Map<String, String> dbs = getDatabases();
//		for (String db : dbs.keySet()) {
//			if (!INSTANCES.containsKey(db)) {
//				INSTANCES.put(db, new MBTilesUtils(dbs.get(db)));
//			}
//		}
//	}
//
//	public static void disconnect() {
//		for (MBTilesUtils entry : INSTANCES.values()) {
//			entry.close();
//		}
//	}
	public void insertBatch(Connection connect,List<Map<String, Integer>> list) {
		try {
			connect.setAutoCommit(false);
			Statement stmt = connect.createStatement();
			String sqlbatch = "";
			for (Map<String, Integer> map : list) {
				String sql = "insert into tiles(tile_column,tile_row,zoom_level) values("+map.get("tile_column")+","+map.get("tile_row")+","+map.get("zoom_level")+");";
				sqlbatch+=sql;
			}
			int count = stmt.executeUpdate(sqlbatch);
//			System.err.println(sql);
			if (count > 0) {
				System.out.println("update data success:"+count);
			} else {
				System.out.println("update data fail");
			}
			connect.commit();

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public  void insert(Connection connect, Integer tile_column, Integer tile_row, Integer zoom_level) {
		try {
			connect.setAutoCommit(false);
			Statement stmt = connect.createStatement();
		
			String sql = "insert into tiles(tile_column,tile_row,zoom_level) values("+tile_column+","+tile_row+","+zoom_level+")";
			int count = stmt.executeUpdate(sql);
//			System.err.println(sql);
			if (count > 0) {
				System.out.println("update data success");
			} else {
				System.out.println("update data fail");
			}
			connect.commit();

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public  void updateBatch(Connection connect, List<Map<String,Object>> list) {
		
		try {
			connect.setAutoCommit(false);
			Statement stmt = connect.createStatement();
//			System.err.println(bytes.length());
			String sqlbatch="";
			for (Map<String, Object> map : list) {
				Integer tile_column = (Integer) map.get("tile_column");
				Integer tile_row = (Integer) map.get("tile_row");
				Integer zoom_level = (Integer) map.get("zoom_level");
				String bytes = (String) map.get("bytes");
				String sql = "update  tiles  set tile_data = X'" + bytes + "',status='200' where zoom_level= " + zoom_level
						+ " and tile_row=" + tile_row + " and tile_column=" + tile_column+";";
				
				sqlbatch+=sql;
			}
		
			int count = stmt.executeUpdate(sqlbatch);
//			System.err.println(sqlbatch);
			if (count > 0) {
				System.out.println("update data success");
			} else {
				System.out.println("update data fail");
			}
			connect.commit();

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public  void update(Connection connect, Integer tile_column, Integer tile_row, Integer zoom_level, String bytes) {
		try {
			connect.setAutoCommit(false);
			Statement stmt = connect.createStatement();
			System.err.println(bytes.length());
			String sql = "update  tiles  set tile_data = X'" + bytes + "',status='200' where zoom_level= " + zoom_level
					+ " and tile_row=" + tile_row + " and tile_column=" + tile_column;
			int count = stmt.executeUpdate(sql);
//			System.err.println(sql);
			if (count > 0) {
				System.out.println("update data success");
			} else {
				System.out.println("update data fail");
			}
			connect.commit();

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public  void update405(Connection connect, Integer tile_column, Integer tile_row, Integer zoom_level, String status) {
		try {
			connect.setAutoCommit(false);
			Statement stmt = connect.createStatement();
			
			String sql = "update  tiles  set status = '" + status + "',tile_data = null where zoom_level= " + zoom_level
					+ " and tile_row=" + tile_row + " and tile_column=" + tile_column;
			int count = stmt.executeUpdate(sql);
//			System.err.println(sql);
			if (count > 0) {
				System.out.println("update "+status+" success");
			} else {
				System.out.println("update data fail");
			}
			connect.commit();

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		MBTilesUtils mbtile = new MBTilesUtils(
//				"E:\\nongkeywebsystem\\18105-nginx-1.14.0-for-cesium-terrain\\html\\1_3\\lsjdfsdf2.mbtiles");
//		byte[] b = mbtile.getTiles(1, 1, 1);
//		System.err.println(b);

		Map<Integer, Integer> levelMaxYSize = new HashMap<Integer, Integer>();
		levelMaxYSize.put(0, 1);
		levelMaxYSize.put(1, 2);
		levelMaxYSize.put(2, 4);
		levelMaxYSize.put(3, 8);
		levelMaxYSize.put(4, 16);
		levelMaxYSize.put(5, 32);
		levelMaxYSize.put(6, 64);
		levelMaxYSize.put(7, 128);
		levelMaxYSize.put(8, 256);
		levelMaxYSize.put(9, 512);
		levelMaxYSize.put(10, 1024);
		levelMaxYSize.put(11, 2048);
		levelMaxYSize.put(12, 4096);
		levelMaxYSize.put(13, 8192);
		levelMaxYSize.put(14, 16384);
		levelMaxYSize.put(15, 32768);
		levelMaxYSize.put(16, 65536);
		levelMaxYSize.put(17, 131072);
		levelMaxYSize.put(18, 262144);
		levelMaxYSize.put(19, 524288);
		levelMaxYSize.put(20, 1048576);
		
//		args =new String[4];
		
//		args[0] = "google";
//		args[1] = "E:\\\\sssss\\\\谷歌地图下载\\\\333333333333.mbtiles";
//		args[2] = "127.0.0.1";
//		args[3] = "7890";
	
		if(args==null||args.length==0) {
			System.out.println("参数1：google"+"参数2：mbtileUrl"+"参数3：proxyIp"+"参数4：proxyport");
			return;
		}
		String mbtileUrl = args[1];
		String proxyIp = args[2];
		String proxyport = args[3];
		Integer sleep;
		try {
			sleep = Integer.parseInt(args[4]);
		} catch (Exception e) {
			// TODO: handle exception
			sleep = 300;
		}
		System.out.println("mbtileUrl:"+mbtileUrl);
		System.out.println("proxyIp:"+proxyIp);
		System.out.println("proxyport:"+proxyport);
		if("google".equals(args[0])) {
			MBTilesUtils mbtile = new MBTilesUtils(mbtileUrl);
			while(true) {

				Random random = new Random();
				Statement stmt;
				try {
					stmt = mbtile.getConn().createStatement();
					ResultSet rs = stmt
							.executeQuery("SELECT * from tiles where status is null  ORDER BY zoom_level asc LIMIT "
									+ 1000 + " offset "+0+";");
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					while (rs.next()) {
						int zoom_level = rs.getInt("zoom_level");
						int tile_row = rs.getInt("tile_row");
						int tile_column = rs.getInt("tile_column");
						int tile_row_org = tile_row;
						Integer maxY = levelMaxYSize.get(zoom_level);
						tile_row = (maxY - tile_row - 1);
						System.out.println(zoom_level + "  " + tile_row + "  " + tile_column);
						Integer server = random.nextInt(3);
//						String url = "http://mt"+server+".google.com/vt/lyrs=s&hl=en&x="+tile_column+"&y="+tile_row+"&z="+zoom_level+"";
						
						String url = "http://gac-geo.googlecnapps.cn/maps/vt?lyrs=s&x="+tile_column+"&y="+tile_row+"&z="+zoom_level+"";
						System.out.println(list.size()+":"+url);
						boolean is405 = false;
						try {
							String bytehex;
							if("no".equals(proxyIp)) {
								bytehex = HttpURLConnectionUtil.doPost(url, "");
							}else {
								bytehex = HttpURLConnectionUtil.doPostProxy(url, "",proxyIp, Integer.parseInt(proxyport));
							}
							
//							System.err.println(bytehex);
							if(bytehex.length()>5) {
								Map<String,Object> map = new HashMap<String, Object>();
								 map.put("tile_column",tile_column);
								map.put("tile_row",tile_row_org);
								 map.put("zoom_level",zoom_level);
								 map.put("bytes",bytehex);
								 list.add(map);
//								mbtile.update(mbtile.getConn(), tile_column, tile_row_org, zoom_level, bytehex);
							}else {
								mbtile.update405(mbtile.getConn(), tile_column, tile_row_org, zoom_level, bytehex);
							}
							
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							is405=true;
							
						}
						try {
							Thread.sleep(sleep);
							
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
					}
					System.err.println("update batch:"+list.size());
					mbtile.updateBatch(mbtile.getConn(), list);
					list.clear();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(300);
					
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}else {
			MBTilesUtils mbtile = new MBTilesUtils(mbtileUrl);
			while(true) {

				Random random = new Random();
				Statement stmt;
				try {
					stmt = mbtile.getConn().createStatement();
					ResultSet rs = stmt
							.executeQuery("SELECT * from tiles where status is null  ORDER BY zoom_level asc LIMIT "
									+ 1000 + " offset "+0+";");
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					while (rs.next()) {
						int zoom_level = rs.getInt("zoom_level");
						int tile_row = rs.getInt("tile_row");
						int tile_column = rs.getInt("tile_column");
						int tile_row_org = tile_row;
						Integer maxY = levelMaxYSize.get(zoom_level);
						tile_row = (maxY - tile_row - 1);
						System.out.println(zoom_level + "  " + tile_row + "  " + tile_column);
						Integer server = random.nextInt(7);
						//天地图注记
						String url = "http://t" + server
							+ ".tianditu.com/cia_w/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=cia&tileMatrixSet=w&TileMatrix="
							+ zoom_level + "&TileRow=" + tile_row + "&TileCol=" + tile_column
							+ "&style=default.jpg&tk=0f89328ea24d54b9003757c2584da549";
						//天地图影像
//						String url = "http://t" + server
//								+ ".tianditu.com/img_w/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=cia&tileMatrixSet=w&TileMatrix="
//								+ zoom_level + "&TileRow=" + tile_row + "&TileCol=" + tile_column
//								+ "&style=default.jpg&tk=0f89328ea24d54b9003757c2584da549";
						
//						陈俊雄的秘钥 0f89328ea24d54b9003757c2584da549
//						原来的秘钥 d12861d485701e869992f652021881a7
						System.out.println(list.size()+":"+url);
						boolean is405 = false;
						try {
							String bytehex;
							if("no".equals(proxyIp)) {
								bytehex = HttpURLConnectionUtil.doPost(url, "");
							}else {
								bytehex = HttpURLConnectionUtil.doPostProxy(url, "",proxyIp, Integer.parseInt(proxyport));
							}
							
//							System.err.println(bytehex);
							if(bytehex.length()>5) {
								Map<String,Object> map = new HashMap<String, Object>();
								 map.put("tile_column",tile_column);
								map.put("tile_row",tile_row_org);
								 map.put("zoom_level",zoom_level);
								 map.put("bytes",bytehex);
								 list.add(map);
//								mbtile.update(mbtile.getConn(), tile_column, tile_row_org, zoom_level, bytehex);
							}else {
								mbtile.update405(mbtile.getConn(), tile_column, tile_row_org, zoom_level, bytehex);
							}
							
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							is405=true;
							
						}
						try {
							Thread.sleep(sleep);
							
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
					}
					System.err.println("update batch:"+list.size());
					mbtile.updateBatch(mbtile.getConn(), list);
					list.clear();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(300);
					
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
//			MBTilesUtils mbtile = new MBTilesUtils("M:\\sssss\\��������\\������ַ\\test16_1.mbtiles");
//			for (int i = 0; i < 20000; i++) {
////				try {
////					
////						Thread.sleep(5000);
////					
////					
////				} catch (InterruptedException e1) {
////					// TODO Auto-generated catch block
////					e1.printStackTrace();
////				}
//				Random random = new Random();
//				Statement stmt;
//				try {
//					stmt = mbtile.getConn().createStatement();
//					ResultSet rs = stmt
//							.executeQuery("SELECT * from tiles where status is null  ORDER BY zoom_level desc LIMIT "
//									+ 100 + " offset "+100*i+";");
//					while (rs.next()) {
//						int zoom_level = rs.getInt("zoom_level");
//						int tile_row = rs.getInt("tile_row");
//						int tile_column = rs.getInt("tile_column");
//						int tile_row_org = tile_row;
//						Integer maxY = levelMaxYSize.get(zoom_level);
//						tile_row = (maxY - tile_row - 1);
//						System.out.println(zoom_level + "  " + tile_row + "  " + tile_column);
//						Integer server = random.nextInt(7);
////						String url = "http://t"+server+".tianditu.com/cia_w/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=cia&tileMatrixSet=w&TileMatrix="+zoom_level+"&TileRow="+tile_row+"&TileCol="+tile_column+"&style=default&format=tiles&tk=d12861d485701e869992f652021881a7";
//						String url = "http://t" + server
//								+ ".tianditu.com/cia_w/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=cia&tileMatrixSet=w&TileMatrix="
//								+ zoom_level + "&TileRow=" + tile_row + "&TileCol=" + tile_column
//								+ "&style=default.jpg&tk=d12861d485701e869992f652021881a7";
////						String url = "http://t1.tianditu.com/cia_w/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=cia&tileMatrixSet=w&TileMatrix=12&TileRow=1780&TileCol=3276&style=default.jpg&tk=d12861d485701e869992f652021881a7";
////						System.err.println(url);
//						boolean is405 = false;
//						try {
//							String bytehex = HttpURLConnectionUtil.doPost(url, "");
////							System.err.println(bytehex);
//							if(bytehex.length()>5) {
//								mbtile.update(mbtile.getConn(), tile_column, tile_row_org, zoom_level, bytehex);
//							}else {
//								mbtile.update405(mbtile.getConn(), tile_column, tile_row_org, zoom_level, bytehex);
//							}
//							
//						} catch (Exception e) {
//							// TODO: handle exception
//							e.printStackTrace();
//							is405=true;
//							
//						}
//						try {
//							Thread.sleep(300);
//							
//						} catch (InterruptedException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}

	}
}