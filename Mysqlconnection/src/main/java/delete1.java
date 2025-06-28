

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

@WebServlet("/delete")
public class delete1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/forgot123", "root", "admin@123");
			String Email = request.getParameter("email");
			System.out.println("Email: "+Email);
			PreparedStatement ps = con.prepareStatement("delete from forgot where email= ?");

			ps.setString( 1, Email);
			int i = ps.executeUpdate();
			System.out.println("i: "+i);
			if (i > 0) {
				response.sendRedirect("delete1.html");
			} else {
				response.sendRedirect("error1.html");
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}


	}


