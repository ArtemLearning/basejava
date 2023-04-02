package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

public class ResumeTestData {
    private static Resume resume;

//    public static void fillContactPhone(String phone) {
//        resume.getContact().put(ContactType.PHONE, phone);
//    }
//
//    public static void fillContactEmail(String email) {
//        resume.getAllContacts().put(ContactType.MAIL, email);
//    }
//
//    public static void fillContactLinkHeader(String linkHeader) {
//        resume.getAllContacts().put(ContactType.GITHUB, linkHeader);
//    }
//
//    public static void fillContactLink(String link) {
//        resume.getAllContacts().put(ContactType.STACKOVERFLOW, link);
//    }
//
//    public static void fillSectionPersonal(Section personal) {
//        resume.getAllSections().put(SectionType.PERSONAL, personal);
//    }
//
//    public static void fillSectionObjective(Section objective) {
//        resume.getAllSections().put(SectionType.OBJECTIVE, objective);
//    }
//
//    public static void fillSectionAchievement(Section achievement) {
//        resume.getAllSections().put(SectionType.ACHIEVEMENT, achievement);
//    }
//
//    public static void fillSectionQualifications(Section qualifications) {
//        resume.getAllSections().put(SectionType.QUALIFICATIONS, qualifications);
//    }
//
//    public static void fillSectionExperience(Section experience) {
//        resume.getAllSections().put(SectionType.EXPERIENCE, experience);
//    }
//
//    public static void fillSectionEducation(Section education) {
//        resume.getAllSections().put(SectionType.EDUCATION, education);
//    }
//
//    public static void showResume(Resume resume) {
//        System.out.println("ИД " + resume.getUuid());
//        System.out.println("Имя Фамилия " + resume.getFullName());
//        System.out.println();
//        System.out.println("Контакты");
//        for (ContactType entry : ContactType.values()) {
//            System.out.println(entry.getTitle() + ": " + resume.getContacts().get(entry));
//        }
//        System.out.println();
//        for (SectionType entry : SectionType.values()) {
//            System.out.println(entry.getTitle() + " " + resume.getSections().get(entry));
//        }
//    }
//
//    public static void main(String[] args) {
//        resume = new Resume("0001", "Григорий Кислин");
//
//        fillContactPhone("+7(921) 855-0482");
//        fillContactEmail("gkislin@yandex.ru");
//        fillContactLinkHeader("Профиль LinkedIn");
//        fillContactLink("https://www.linkedin.com/in/gkislin");
//
//        TextSection personalData = new TextSection("Аналитический склад ума, сильная логика, креативность, " +
//                "инициативность. Пурист кода и архитектуры.");
//        fillSectionPersonal(personalData);
//
//        TextSection objectiveData = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и" +
//                " Enterprise технологиям");
//        fillSectionObjective(objectiveData);
//
//        String achievementOne = "Организация команды и успешная реализация Java проектов для сторонних заказчиков: " +
//                "приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов " +
//                "на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для " +
//                "комплексных DIY смет";
//        String achievementTwo = "Реализация двухфакторной аутентификации для онлайн платформы управления проектами " +
//                "Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.";
//        String achievementThree = "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. " +
//                "Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: " +
//                "Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, " +
//                "интеграция CIFS/SMB java сервера.";
//        ListSection achievementData = new ListSection(Arrays.asList(achievementOne, achievementTwo,
//                achievementThree));
//        fillSectionAchievement(achievementData);
//
//        String qualificationOne = "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2";
//        String qualificationTwo = "Version control: Subversion, Git, Mercury, ClearCase, Perforce";
//        String qualificationThree = "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js";
//        ListSection qualificationData = new ListSection(Arrays.asList(qualificationOne, qualificationTwo,
//                qualificationThree));
//        fillSectionQualifications(qualificationData);
//
//        Period jobOne = new Period(LocalDate.of(2014, Month.OCTOBER, 1), LocalDate.of(2016, Month.JANUARY, 1),
//                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, " +
//                        "MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по " +
//                        "OAuth1, OAuth2, JWT SSO.");
//        Period jobTwo = new Period(LocalDate.of(2012, Month.APRIL, 1), LocalDate.of(2014, Month.OCTOBER, 1),
//                "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование," +
//                        " ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы " +
//                        "(pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. " +
//                        "Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения " +
//                        "(почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из " +
//                        "браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, " +
//                        "Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, " +
//                        "Unix shell remote scripting via ssh tunnels, PL/Python");
//        Period jobThree = new Period(LocalDate.of(2010, Month.DECEMBER, 1), LocalDate.of(2012, Month.APRIL, 1),
//                "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, " +
//                        "Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для " +
//                        "администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. " +
//                        "JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5.");
//        List<Period> jobList = List.of(jobOne);
//        Organization organizationOne = new Organization("Wrike", "Старший разработчик (backend)",
//                jobList, "https://www.wrike.com/");
//        jobList = List.of(jobTwo);
//        Organization organizationTwo = new Organization("RIT Center", "Java архитектор", jobList, "");
//        jobList = List.of(jobThree);
//        Organization organizationThree = new Organization("Luxoft (Deutsche Bank)", "Ведущий программист",
//                jobList, "http://www.luxoft.ru/");
//        List<Organization> experience = Arrays.asList(organizationOne, organizationTwo, organizationThree);
//        OrganizationSection experienceData = new OrganizationSection(experience);
//        fillSectionExperience(experienceData);
//
//        Period learnOne = new Period(LocalDate.of(2013, Month.MARCH, 1), LocalDate.of(2013, Month.MAY, 1),
//                "'Functional Programming Principles in Scala' by Martin Odersky");
//        Period learnTwo = new Period(LocalDate.of(2011, Month.MARCH, 1), LocalDate.of(2011, Month.APRIL, 1),
//                "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'");
//        Period learnThree = new Period(LocalDate.of(2005, Month.JANUARY, 1), LocalDate.of(2005, Month.APRIL, 1),
//                "3 месяца обучения мобильным IN сетям (Берлин)");
//        List<Period> learnList = List.of(learnOne);
//        Organization schoolOne = new Organization("Coursera", "", learnList, "https://www.coursera.org/course/progfun");
//        learnList = List.of(learnTwo);
//        Organization schoolTwo = new Organization("Luxoft", "", learnList,
//                "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366");
//        learnList = List.of(learnThree);
//        Organization schoolThree = new Organization("Siemens AG", "", learnList, "http://www.siemens.ru/");
//        List<Organization> education = Arrays.asList(schoolOne, schoolTwo,schoolThree);
//        OrganizationSection educationData = new OrganizationSection(education);
//        fillSectionEducation(educationData);
//        showResume(resume);
//    }
}
