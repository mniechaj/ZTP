import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Nprime extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @EJB
	NprimeRemote nprimeRemote;
    
    public Nprime() {
        
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		
		try {
			String nParam = request.getParameter("n");
			int n = Integer.parseUnsignedInt(nParam);
			int result = nprimeRemote.prime(n);
			writer.append(Integer.toString(result));
		} catch (NumberFormatException e) {
			writer.append("Provided parameter was not a number or was not natural number.");
		} catch (Exception e) {
			writer.append("Indefinite exception: " + e.getMessage());
		}
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		
		try {
			String nParam = request.getParameter("n");
			int n = Integer.parseUnsignedInt(nParam);
			
			int iter = n;
			int result = 0;
			while (result == 0 || result <= n) {
				iter += 1;
				result = nprimeRemote.prime(iter);
			}
			writer.append(Integer.toString(result));
		} catch (NumberFormatException e) {
			writer.append("Provided parameter was not a number or was not natural number!");
		} catch (Exception e) {
			writer.append("Indefinite exception: " + e.getMessage());
		}
	}

}
