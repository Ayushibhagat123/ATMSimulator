

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
import java.sql.ResultSet;


@WebServlet("/process1")
public class process1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/forgot123","root","admin@123");
			String uname =request.getParameter("uname");
			String password =request.getParameter("pass");
			PreparedStatement ps=con.prepareStatement("select*from forgot where username=? and password=?");
			ps.setString(1, uname);
			ps.setString(2, password);
			
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				response.sendRedirect("Welcome.html");
			}
			else {
				response.sendRedirect("error.html");
				}
		}catch (Exception e) {
			out.println(e);
			e.printStackTrace();
		}
	}

}
