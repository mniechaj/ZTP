import java.util.LinkedList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * @author Maciek Niechaj
 *
 */
@Stateless
@LocalBean
public class Solid implements ISolidRemote {

    public Solid() {

    }
    
    /**
     * Method counts surface area of hull represented by
     * list of points adding surface area of triangles
     * appointed by hull
     */
    @Override
    public double countXZprojection(List<Double> pts) {
    	List<Point> points = getPoints(pts);
    	List<Point> hull = findConvexHull(points);
    	double projectionArea = 0.0;
    	
    	Point p1 = hull.get(0);
    	Point p2 = hull.get(1);
    	
    	List<Point> remaining = hull.subList(2, hull.size());
    	for (Point p : remaining) {
    		projectionArea += countTriangleArea(p1, p2, p);
    		p1 = p2;
    		p2 = p;
    	}
    	
    	return projectionArea;
    }
    
    private List<Point> getPoints(List<Double> points) {
    	List<Point> ret = new LinkedList<>();
    	for (int i = 0; i < points.size() / 3; i++) {
    		ret.add(new Point(points.get(3 * i), points.get(3 * i + 1), points.get(3 * i + 2)));
    	}
		return ret;
    }
    
    /**
     * Auxiliary method to find convex hull of points that are projection
     * on XZ plane
     */
    private List<Point> findConvexHull(List<Point> points) {
    	List<Point> hull = new LinkedList<>();
    	Point leftMost = getLeftmost(points);
    	Point p1 = leftMost;
    	Point p2;
    	
    	do {
    		hull.add(p1);
    		p2 = points.get((points.indexOf(p1) + 1) % points.size());
    		
    		for (Point point : points) {
    			if (getOrientation(p1, point, p2) == -1) {
    				p2 = point;
    			}
    		}
    		p1 = p2;
    	} while (!p1.equals(leftMost));
    	
    	return hull;
    }
    
    private Point getLeftmost(List<Point> points) {
    	Point leftMost = points.get(0);
    	
    	for (Point point : points) {
    		if (point.x < leftMost.x) {
    			leftMost = point;
    		}
    	}
    	return leftMost;
	}
    
    /**
     * Auxiliary method to find rotation
     * returns 1, 0, -1 for clockwise, none, counter clockwise respectively
     */
    private int getOrientation(Point p1, Point p2, Point p3) {
    	double i = (p2.z - p1.z) * (p3.x - p2.x) - (p2.x - p1.x) * (p3.z - p2.z);
    	if (i == 0) {
    		return 0;
    	} else if (i > 0) {
    		return 1;
    	} else {
    		return -1;
    	}
    }
    
    private double countTriangleArea(Point p1, Point p2, Point p3) {
    	double area = 0.5 * Math.abs(((p2.x - p1.x) * (p3.z - p1.z)) - ((p2.z - p1.z) * (p3.x - p1.x)));
    	return area;
    }
    
    private class Point {
		double x;
		double y;
		double z;
		
		public Point(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		@Override
		public String toString() {
			return "x: " + x + " y: " + y + " z :" + z;
		}
	}
    
}
