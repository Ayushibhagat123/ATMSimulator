

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


@WebServlet("/forgot")
public class forgot extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out= response.getWriter();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/forgot123","root","admin@123");
			String email = request.getParameter("email");
			String pass = request.getParameter("pass");
			
			PreparedStatement ps = con.prepareStatement("update forgot set password=? where email=?");
			ps.setString(2, email);
			ps.setString(1, pass);
			
			int i=ps.executeUpdate();
			if(i>0) {
				response.sendRedirect("update.html");
			}
			else {
				response.sendRedirect("error1.html");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
