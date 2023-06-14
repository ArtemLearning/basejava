package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.Config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {
    Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "view", "edit" -> r = storage.get(uuid);
            case "add" -> r = new Resume(String.valueOf(UUID.randomUUID()), "Введите имя");
            default -> throw new IllegalArgumentException("Action = " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view").equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r;
        try {
            r = storage.get(uuid);
            r.setFullName(fullName);
        } catch (NotExistStorageException e) {
            r = new Resume(uuid, fullName);
            storage.save(r);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getAllContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String str = request.getParameter(type.name());
            Section value = fromString(type, str);
            if (value != null && str.trim().length() != 0) {
                r.addSection(type, value);
            } else {
                r.getAllSections().remove(type);
            }
        }
        storage.update(r);
        response.sendRedirect("resume");
    }

    private Section fromString(SectionType type, String str) {
        switch (type) {
            case PERSONAL, OBJECTIVE -> {
                return new TextSection(str);
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                return new ListSection(str);
            }
            case EDUCATION, EXPERIENCE -> {
                return null;
            }
        }
        return null;
    }
}
