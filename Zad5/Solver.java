import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import pl.jrj.dsm.IDSManagerRemote;

public class Solver extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(mappedName = "java:global/ejb-project/DSManager")
	IDSManagerRemote dsManager;
	@EJB
	ISolidRemote solid;

    public Solver() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tableName = request.getParameter("t");
		if (tableName != null) {
			try {
				InitialContext context = new InitialContext();
				String jndiDSname = dsManager.getDS();
				DataSource dataSource = (DataSource)context.lookup(jndiDSname);
				Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
				List<Double> points = new LinkedList<>();
				while (resultSet.next()) {
					double x = resultSet.getDouble("x");
					double y = resultSet.getDouble("y");
					double z = resultSet.getDouble("z");
					points.add(x);
					points.add(y);
					points.add(z);
				}
				
				double xzProjectionArea = solid.countXZprojection(points);
				response.getWriter().append(String.format("%.5f", xzProjectionArea));
				connection.close();
			} catch (NamingException e) {
				response.getWriter().append(e.getMessage());
			} catch (SQLException e) {
				response.getWriter().append(e.getMessage());
			}
			
		} else {
			response.getWriter().append("Required parameter t was not provided in url.");
		}
		
	}

}
