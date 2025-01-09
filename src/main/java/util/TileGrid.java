package util;

import java.util.HashMap;
import java.util.Map;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class TileGrid {

	public static Coordinate lonlat2xy(double lon,double lat,int level) {
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
		

		
		
		  int xTiles = levelMaxYSize.get(level);
		  int yTiles = levelMaxYSize.get(level);

		  double _rectangleNortheastInMetersX = 20037508.0;
		  double _rectangleNortheastInMetersY = 20037508.0;
		  
		  
		  double _rectangleSouthwestInMetersX = -20037508.0;
		  double _rectangleSouthwestInMetersY = -20037508.0;
		  
		  
		  
		  double overallWidth =
				  _rectangleNortheastInMetersX -_rectangleSouthwestInMetersX;
		  double xTileWidth = overallWidth / xTiles;
		  double overallHeight =
				  _rectangleNortheastInMetersY - _rectangleSouthwestInMetersY;
		  double yTileHeight = overallHeight / yTiles;

		 
			org.opengis.referencing.crs.CoordinateReferenceSystem sourceCRS;
			org.opengis.referencing.crs.CoordinateReferenceSystem targetCRS;
			Point quick = null;
			try {
				sourceCRS = CRS.parseWKT("GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]");

				targetCRS = CRS.decode("EPSG:3857");
			

				  Coordinate sourceCoord = new Coordinate(lon, lat);
			        GeometryFactory geoFactory = new GeometryFactory();
			        Point sourcePoint = geoFactory.createPoint(sourceCoord);
				
				MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
				
				quick = (Point) JTS.transform(sourcePoint, transform);
//				System.err.println(quick.getX()+" "+quick.getY());
			} catch (NoSuchAuthorityCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FactoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Sample 10 points around the envelope
//		        Envelope better = JTS.transform(envelope, null, transform, 10);
			catch (TransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		  
		  double distanceFromWest =
				  quick.getX() - _rectangleSouthwestInMetersX;
		  double distanceFromNorth =
				  _rectangleNortheastInMetersY - quick.getY();

		  double xTileCoordinate = distanceFromWest / xTileWidth;
		  if (xTileCoordinate >= xTiles) {
		    xTileCoordinate = xTiles - 1;
		  }
		  double yTileCoordinate =  (distanceFromNorth / yTileHeight);
		  if (yTileCoordinate >= yTiles) {
		    yTileCoordinate = yTiles - 1;
		  }

		
		  Coordinate sourceCoord = new Coordinate(Math.round(xTileCoordinate),Math.round(yTileCoordinate));
		
		  return sourceCoord;
	}

	// **3857坐标系对应瓦片范围**
	public static Point xyz2prj3857(int z, int x, int y) {
		double n = Math.pow(2, z);
		double lon_min = (x / n) * 40075016.0 - 20037508.0;
		double lat_min = 20037508.0 - (((y + 1) / n) * 40075016.0);
		double lon_max = ((x + 1) / n) * 40075016.0 - 20037508.0;
		double lat_max = 20037508.0 - ((y / n) * 40075016.0);

		org.opengis.referencing.crs.CoordinateReferenceSystem sourceCRS;
		org.opengis.referencing.crs.CoordinateReferenceSystem targetCRS;
		try {
			sourceCRS = CRS.decode("EPSG:3857");
			targetCRS = CRS.parseWKT("GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]");;
//			System.err.println(lon_min+" "+lat_min+" "+lon_max+" "+lat_max);
//			Envelope envelope = new Envelope(lon_min, lat_min, lon_max, lat_max);
//
//			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			
			
			  Coordinate sourceCoord = new Coordinate(lon_min + (lon_max - lon_min)/2, lat_min+(lat_max - lat_min)/2);
		        GeometryFactory geoFactory = new GeometryFactory();
		        Point sourcePoint = geoFactory.createPoint(sourceCoord);
			
			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			
			Point min = (Point) JTS.transform(sourcePoint, transform);
			
//			
//			  Coordinate sourceCoord2 = new Coordinate(lon_max, lat_max);
//		      
//		        Point sourcePoint2 = geoFactory.createPoint(sourceCoord2);
//		    	Point max = (Point) JTS.transform(sourcePoint2, transform);
		    	

//			Envelope quick = JTS.transform(envelope, transform);
			return min;
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Sample 10 points around the envelope
//	        Envelope better = JTS.transform(envelope, null, transform, 10);
		catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	// **4326坐标系对应瓦片范围**
	public static Envelope xyz2prj4326(int z, int x, int y) {
		double n = Math.pow(2, z);
		double lon_min = (x / n) * 360.0 - 180.0;
		double lat_min = 90.0 - (((y + 1) / n) * 360.0);
		double lon_max = ((x + 1) / n) * 360.0 - 180.0;
		double lat_max = 90.0 - ((y / n) * 360.0);
		Envelope envelope = new Envelope(lon_min, lat_min, lon_max, lat_max);
		return envelope;
	}
	public static void main(String[] args) {
		Coordinate coord = TileGrid.lonlat2xy(108.45022658628154,23.3486849234799, 16);
		System.err.println(coord.getX()+" "+coord.getY());
		
		Point e = TileGrid.xyz2prj3857(16,52510,28394);
		System.err.println(e.getX()+" "+e.getY());
		
		
		
		Coordinate max = TileGrid.lonlat2xy(108.4587885799378029,23.3476452596096316, 18);
		
		System.err.println(max.getX()+" "+max.getY());
		
		
		Coordinate min = TileGrid.lonlat2xy(108.4532403233806406,23.3425279514829036, 18);
		
		System.err.println(min.getX()+" "+min.getY());
	}
}
