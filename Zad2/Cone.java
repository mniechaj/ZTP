import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cone extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private List<Sphere> defects = new LinkedList<>();

	public Cone() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Double x = Double.parseDouble(request.getParameter("x"));
			Double y = Double.parseDouble(request.getParameter("y"));
			Double z = Double.parseDouble(request.getParameter("z"));
			Double r = Double.parseDouble(request.getParameter("r"));
			Sphere defect = new Sphere(x, y, z, r);
			defects.add(defect);
		} catch (NumberFormatException e) {

		} catch (NullPointerException e) {

		} catch (Exception e) {

		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		try {
			Double ra = Double.parseDouble(request.getParameter("ra"));
			Double rb = Double.parseDouble(request.getParameter("rb"));
			Double h = Double.parseDouble(request.getParameter("h"));
			Double g = Double.parseDouble(request.getParameter("g"));
			Double c = Double.parseDouble(request.getParameter("c"));

			Double iterations = 3000000.0;
			Double mass = coneMC(ra, rb, h, g, c, iterations);
			writer.printf("%.3f", mass);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (NullPointerException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} finally {
			defects.clear();
		}
	}

	private class Sphere {
		public Double x;
		public Double y;
		public Double z;
		public Double r;

		public Sphere(Double x, Double y, Double z, Double r) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.r = r;
		}
	}

	private Double coneMC(Double ra, Double rb, Double h, Double g, Double c, Double iterations) {
		Double inDefect = 0.0;
		Double inCone = 0.0;
		Random random = new Random();
		Double x = 0.0;
		Double y = 0.0;
		Double z = 0.0;

		for (int i = 0; i < iterations; i++) {
			Double r = Math.sqrt(random.nextDouble()) * ra;
			Double alpha = random.nextDouble() * 2.0 * Math.PI;
			x = r * Math.cos(alpha);
			y = r * Math.sin(alpha);
			z = random.nextDouble() * h;		
		
			if (pointInCone(x, y, z, ra, rb, h)) {
				inCone += 1.0;
				for (Sphere defect : defects) {
					if ((Math.pow(x - defect.x, 2) + Math.pow(y - defect.y, 2) + Math.pow(z - defect.z, 2)) <= Math
							.pow(defect.r, 2)) {
						inDefect += 1.0;
						break;
					}
				}
			}
		}

		Double coneVolume = Math.PI * h * (Math.pow(ra, 2) + Math.pow(rb, 2) + ra * rb) / 3.0;
		Double defectsVolume = (inDefect / inCone) * coneVolume;
		Double remainingVolume = coneVolume - defectsVolume;
		Double defectsMass = defectsVolume * g;
		Double remainingMass = remainingVolume * c;
		return defectsMass + remainingMass;
	}

	private boolean pointInCone(Double x, Double y, Double z, Double ra, Double rb, Double h) {
		Double rz = ra - (z * (ra - rb) / h);
		if (Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(rz, 2))
			return true;
		else
			return false;
	}
	
}
