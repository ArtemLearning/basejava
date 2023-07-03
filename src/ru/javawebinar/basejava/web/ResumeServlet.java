package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.Config;
import ru.javawebinar.basejava.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            case "view" -> r = storage.get(uuid);
            case "edit" -> {
                r = storage.get(uuid);
                for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
                    OrganizationSection section = (OrganizationSection) r.getSection(type);
                    List<Organization> emptyOrganization = new ArrayList<>();
                    emptyOrganization.add(Organization.EMPTY);
                    if (section != null) {
                        for (Organization org : section.getOrganizations()) {
                            List<Organization.Position> emptyPositions = new ArrayList<>();
                            emptyPositions.add(Organization.Position.EMPTY);
                            emptyPositions.addAll(org.getPositions());
                            emptyOrganization.add(new Organization(org.getHomePage(), emptyPositions));
                        }
                    }
                    r.addSection(type, new OrganizationSection(emptyOrganization));
                }
            }
            case "add" -> {
                r = new Resume(String.valueOf(UUID.randomUUID()), "");
                r.addSection(SectionType.EXPERIENCE, new OrganizationSection(Organization.EMPTY));
                r.addSection(SectionType.EDUCATION, new OrganizationSection(Organization.EMPTY));
            }
            default -> throw new IllegalArgumentException("Action = " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(("view").equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp").forward(request, response);
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
            String[] values = request.getParameterValues(type.name());
            switch (type) {
                case PERSONAL, OBJECTIVE -> {
                    r.addSection(type, new TextSection(str));
                }
                case ACHIEVEMENT, QUALIFICATIONS -> {
                    r.addSection(type, new ListSection(str));
                }
                case EDUCATION, EXPERIENCE -> {
                    List<Organization> organizations = new ArrayList<>();
                    String[] urls = request.getParameterValues(type.name() + "url");
                    for (int i = 0; i < values.length; i++) {
                        String name = values[i];
                        if (name != null) {
                            List<Organization.Position> positions = new ArrayList<>();
                            String pfx = type.name() + i;
                            String[] startDates = request.getParameterValues(pfx + "startDate");
                            String[] endDates = request.getParameterValues(pfx + "endDate");
                            String[] titles = request.getParameterValues(pfx + "title");
                            String[] descriptions = request.getParameterValues(pfx + "description");
                            for (int j = 0; j < titles.length; j++) {
                                if (titles[j] != null) {
                                    positions.add(new Organization.Position(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                }
                            }
                            organizations.add(new Organization(new Link(name, urls[i]), positions));
                        }
                    }
                    r.addSection(type, new OrganizationSection(organizations));
                }
            }
        }
        storage.update(r);
        response.sendRedirect("resume");
    }
}
