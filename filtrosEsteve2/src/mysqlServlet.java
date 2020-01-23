

import java.io.*;
import java.sql.*;

import javax.servlet.ServletConfig;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;


/**
 * Servlet implementation class mysqlServlet
 */
@WebServlet("/mysqlServlet")
public class mysqlServlet extends HttpServlet {
	//El método doGet() se ejecuta una vez por cada petición HTTP GET.
	private String userName;
	private String password;
	private String url;
	
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mysqlServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// Establecemos el tipo MIME del mensaje de respuesta
				response.setContentType("text/html");
				// Creamos un objeto para poder escribir la respuesta
				PrintWriter out = response.getWriter();
				
				Connection conn = null;
				Statement stmt = null;
				try {
					//Paso 1: Cargar el driver JDBC
					Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

					//Paso 2: Conectarse a la Base de Datos utilizando la clase Connection
					//String userName="root";
					//String password="232323";
					//URL de la base de datos(equip, puerto, base de datos)
					//String url = "jdbc:mysql://localhost/TiendaLibros?serverTimezone=UTC";
					
					//C:\Program Files\MySQL\MySQL Server 8.0\bin\tiendalibros.sql";
					
					//String url = "jdbc:mysql://localhost:33065/tiendalibros?serverTimezone=UTC";
					//DriverManager.getConnection(url, userName, password);
					conn = DriverManager.getConnection(url, userName, password);
					
					//Paso 3: Crear sentencias SQL, utilizando objetos de tipo Statement
					stmt = conn.createStatement();
					
					//Paso 4: Ejecutar las sentencias SQL a través de los objetos Statement
					 String sqlStr = "select * from libros where autor = "
							+ "'" + request.getParameter("autor") + "'";
					//Generar una página HTML como resultado de la consulta
					out.println("<html><head><title>Resultado de la consulta</title></head><body>");
					out.println("<h3>Gracias por su consulta.</h3>");
					out.println("<p>Tu consulta es: " + sqlStr + "</p>");
					ResultSet rset = stmt.executeQuery(sqlStr);

					//Paso 5: Procesar el conjunto de registros resultante utilizando ResultSet
					int count = 0;
					while(rset.next()) {
						out.println("<p>" + rset.getString("autor")
						+ ", " + rset.getString("titulo")
						+ ", " + rset.getDouble("precio") + "</p>");
						
						count++;
					}
					//out.println(request.getParameter("autor"));
					out.println("<p>=== " + count + " registros encontrados =====</p>");
					out.println("</body></html>");
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					out.close();   // Cerramos el flujo de escritura
					 try {
						 //Cerramos el resto de los recursos
						 if (stmt != null) stmt.close();
						 if (conn != null) conn.close();
					 } catch (SQLException ex) {
						ex.printStackTrace();
					}
					
				}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response); // Redirecciona la petición POST al método doGet().
	}
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		
		//Se leen los parámetros de inicialización del Servlet y
		//se convierten en atributos del contexto para compartirlos con
		//cualquier servlet y JSP de la aplicación
		ServletContext context = config.getServletContext();
		context.setAttribute("URLBaseDeDatos", config.getInitParameter("URLBaseDeDatos"));
		context.setAttribute("usuario", config.getInitParameter("usuario"));
		context.setAttribute("password", getInitParameter("password"));

		//Se recuperan las variables de contexto de la aplicación
		userName = (String)context.getAttribute("usuario");
		password = (String)context.getAttribute("password");
		url = (String)context.getAttribute("URLBaseDeDatos");
	}
}
