import at.oculus.teamf.technical.loggin.ILogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author FabianLaptop
 */

@WebServlet(name = "RedirectServlet")
public class RedirectServlet extends HttpServlet implements ILogger{
    private UserController ulogin;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String dispatchto = request.getParameter("dispatchto");

        // null redirect (go back to login page)
        if (dispatchto == null) {
            response.sendRedirect("login.jsp");
        }

        // redirect to user login servlet
        if (dispatchto.equals("login")) {
            ulogin = new UserController();
            ulogin.service(request, response);
        }

        // redirect to create appointment servlet
        if (dispatchto.equals("checkappointments")) {
            CreateAppointmentController appoint = new CreateAppointmentController();
            appoint.service(request, response);
        }

        // redirect to confirm appointment servlet
        if (dispatchto.equals("confirmappointment")) {
            ConfirmAppointmentController conf = new ConfirmAppointmentController();
            conf.service(request, response);
        }

        // redirect to delete appointment servlet
        if (dispatchto.equals("deleteappointment")) {
            DeleteAppointmentController del = new DeleteAppointmentController();
            del.service(request, response);
        }

        // redirect to appointment servlet
        if (dispatchto.equals("logoutuser")) {
            LogoutUserController ulogout = new LogoutUserController();
            ulogout.service(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Redirect Controller";
    }

}
