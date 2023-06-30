<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="java.util.Map" %>
<%@ page import="ru.javawebinar.basejava.model.Section" %>
<%@ page import="ru.javawebinar.basejava.model.TextSection" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit">Edit</a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.allContacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <hr>
    <table>
        <c:forEach var="sectionEntry" items="${resume.allSections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.Section>"/>
            <c:set var="type" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="ru.javawebinar.basejava.model.Section"/>
            <td><h3><a name="type.name">${type.title}</a></h3></td>
            <c:if test="${type == 'OBJECTIVE'}">
                <tr>
                    <td>
                        <%=((TextSection) section).getContent()%>
                    </td>
                </tr>
            </c:if>
            <c:if test="${type != 'OBJECTIVE'}">
                <c:choose>
                    <c:when test="${type == 'PERSONAL'}">
                        <tr>
                            <td>
                                <%=((TextSection) section).getContent()%>
                            </td>
                        </tr>
                    </c:when>
                    <c:when test="${type == 'QUALIFICATIONS' || type == 'ACHIEVEMENT'}">
                        <tr>
                            <td>
                                <c:forEach var="item" items="<%=((ListSection) section).getItems()%>">
                                    <ul>
                                        <li>${item}</li>
                                    </ul>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:when>
                    <c:when test="${type == 'EDUCATION' || type == 'EXPERIENCE'}">
                        <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>">
                            <tr>
                                <td>
                                    <h3><a href="${org.homePage.url}">${org.homePage.name}</a></h3>
                                </td>
                            </tr>
                            <c:forEach var="position" items="${org.positions}">
                                <jsp:useBean id="position" type="ru.javawebinar.basejava.model.Organization.Position"/>
                                <tr>
                                    <td><%=DateUtil.formatDates(position)%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>${position.title}</strong></td>
                                    <td>${position.description}</td>
                                </tr>
                                <br>
                            </c:forEach>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </c:if>
        </c:forEach>
    </table>
    <button onclick="window.history.back()">Ок</button>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
