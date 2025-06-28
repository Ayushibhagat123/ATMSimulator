

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


@WebServlet("/reg")
public class reg extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/forgot123","root","admin@123");
			String uname =request.getParameter("uname");
			String address =request.getParameter("add");
			String email =request.getParameter("email");
			String password =request.getParameter("pass");
			PreparedStatement ps=con.prepareStatement("insert into forgot value (?,?,?,?)");
			ps.setString(1, uname);
			ps.setString(2, address);
			ps.setString(3, email);
			ps.setString(4, password);
			int i= ps.executeUpdate();
			
			if(i>0) {
				response.sendRedirect("welcome1.html");
			}
			else {
				response.sendRedirect("error.html");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

	
}
