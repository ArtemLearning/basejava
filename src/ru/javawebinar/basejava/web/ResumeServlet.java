package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.util.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset =UTF-8");
//        String name = request.getParameter("name");
//        response.getWriter().write(name == null ? "Hello Resumes" : "Hello " + name);
        final SqlStorage storage = (SqlStorage) Config.get().getStorage();
        final List<Resume> list = storage.getAllSorted();
        final PrintWriter out = response.getWriter();
        out.println("<head><title>Курс BaseJava</title></head>");
        out.println("<body><h1>Таблица резюме</h1>");
        out.println("<table border='1'>");
        out.println("<tr>" + "<th>UUID</th>" + "<th>Full Name</th>");
        for (Resume element : list) {
            out.println("<tr><td>" + element.getUuid() + "</td><td>" + element.getFullName() + "</td>");
        }
        out.println("</table>");
        out.println("</body>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
