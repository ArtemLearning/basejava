package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class ResumeTestData {
    private static Resume resume;

    private static void fillContactPhone(String phone) {
        resume.getAllContacts().put(ContactType.PHONE, phone);
    }

    private static void fillContactMobile(String phone) {
        resume.getAllContacts().put(ContactType.MOBILE, phone);
    }

    private static void fillContactHomePhone(String phone) {
        resume.getAllContacts().put(ContactType.HOME_PHONE, phone);
    }

    private static void fillContactSkype(String skype) {
        resume.getAllContacts().put(ContactType.SKYPE, skype);
    }

    private static void fillContactEmail(String email) {
        resume.getAllContacts().put(ContactType.MAIL, email);
    }

    private static void fillContactLinkedin(String linkedin) {
        resume.getAllContacts().put(ContactType.LINKEDIN, linkedin);
    }

    private static void fillContactGithub(String github) {
        resume.getAllContacts().put(ContactType.GITHUB, github);
    }

    private static void fillContactStackoverflow(String stackoverflow) {
        resume.getAllContacts().put(ContactType.STACKOVERFLOW, stackoverflow);
    }

    private static void fillContactHomePage(String homePage) {
        resume.getAllContacts().put(ContactType.HOME_PAGE, homePage);
    }

    private static void fillSectionPersonal(Section personal) {
        resume.getAllSections().put(SectionType.PERSONAL, personal);
    }

    private static void fillSectionObjective(Section objective) {
        resume.getAllSections().put(SectionType.OBJECTIVE, objective);
    }

    private static void fillSectionAchievement(Section achievement) {
        resume.getAllSections().put(SectionType.ACHIEVEMENT, achievement);
    }

    private static void fillSectionQualifications(Section qualifications) {
        resume.getAllSections().put(SectionType.QUALIFICATIONS, qualifications);
    }

    private static void fillSectionExperience(Section experience) {
        resume.getAllSections().put(SectionType.EXPERIENCE, experience);
    }

    private static void fillSectionEducation(Section education) {
        resume.getAllSections().put(SectionType.EDUCATION, education);
    }

    private static void showResume(Resume resume) {
        System.out.println("ИД " + resume.getUuid());
        System.out.println("Имя Фамилия " + resume.getFullName());
        System.out.println();
        System.out.println("Контакты");
        for (ContactType entry : ContactType.values()) {
            System.out.println(entry.getTitle() + ": " + resume.getContact(entry));
        }
        System.out.println();
        for (SectionType entry : SectionType.values()) {
            System.out.println(entry.getTitle() + " " + resume.getSection(entry));
        }
    }

    private static void fillResumeContacts() {
        fillContactPhone("+7(921) 855-0482");
        fillContactMobile("-");
        fillContactHomePhone("-");
        fillContactSkype("skype:grigory.kislin");
        fillContactEmail("gkislin@yandex.ru");
        fillContactLinkedin("https://www.linkedin.com/in/gkislin");
        fillContactGithub("https://github.com/gkislin");
        fillContactStackoverflow("https://stackoverflow.com/users/548473");
        fillContactHomePage("http://gkislin.ru/");
    }

    private static void fillResumeSections() {
        // Личные качества
        TextSection personalData = new TextSection("Аналитический склад ума, сильная логика, креативность, " +
                "инициативность. Пурист кода и архитектуры.");
        fillSectionPersonal(personalData);
        // Позиция
        TextSection objectiveData = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и" +
                " Enterprise технологиям");
        fillSectionObjective(objectiveData);
        // Достижения
        String achievementOne = "Организация команды и успешная реализация Java проектов для сторонних заказчиков: " +
                "приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов " +
                "на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для " +
                "комплексных DIY смет";
        String achievementTwo = "Реализация двухфакторной аутентификации для онлайн платформы управления проектами " +
                "Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.";
        String achievementThree = "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. " +
                "Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: " +
                "Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, " +
                "интеграция CIFS/SMB java сервера.";
        ListSection achievementData = new ListSection(Arrays.asList(achievementOne, achievementTwo,
                achievementThree));
        fillSectionAchievement(achievementData);
        // Квалификация
        String qualificationOne = "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2";
        String qualificationTwo = "Version control: Subversion, Git, Mercury, ClearCase, Perforce";
        String qualificationThree = "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js";
        ListSection qualificationData = new ListSection(Arrays.asList(qualificationOne, qualificationTwo,
                qualificationThree));
        fillSectionQualifications(qualificationData);
        // Опыт работы
        Period jobOne = new Period(LocalDate.of(2014, Month.OCTOBER, 1),
                LocalDate.of(2016, Month.JANUARY, 1),
                "Старший разработчик (backend)",
                "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, " +
                        "MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по " +
                        "OAuth1, OAuth2, JWT SSO.");
        Period jobTwo = new Period(LocalDate.of(2012, Month.APRIL, 1),
                LocalDate.of(2014, Month.OCTOBER, 1),
                "Java архитектор",
                "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование," +
                        " ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы " +
                        "(pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. " +
                        "Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения " +
                        "(почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из " +
                        "браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, " +
                        "Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, " +
                        "Unix shell remote scripting via ssh tunnels, PL/Python");
        Period jobThree = new Period(LocalDate.of(2010, Month.DECEMBER, 1),
                LocalDate.of(2012, Month.APRIL, 1),
                "Ведущий программист",
                "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, " +
                        "Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для " +
                        "администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. " +
                        "JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5.");
        List<Period> jobList = List.of(jobOne);
        Organization organizationOne = new Organization("Wrike", "https://www.wrike.com/", jobList);
        jobList = List.of(jobTwo);
        Organization organizationTwo = new Organization("RIT Center", "-", jobList);
        jobList = List.of(jobThree);
        Organization organizationThree = new Organization("Luxoft (Deutsche Bank)", "http://www.luxoft.ru/", jobList);
        List<Organization> experience = Arrays.asList(organizationOne, organizationTwo, organizationThree);
        OrganizationSection experienceData = new OrganizationSection(experience);
        fillSectionExperience(experienceData);
        //Образование
        Period learnOne = new Period(LocalDate.of(1987, Month.SEPTEMBER, 1),
                LocalDate.of(1993, Month.JULY, 1), "Инженер (программист Fortran, C)", "-");
        Period learnTwo = new Period(LocalDate.of(1993, Month.SEPTEMBER, 1), LocalDate.of(1996, Month.JULY, 1),
                "Аспирантура (программист С, С++)", "-");
        Period learnThree = new Period(LocalDate.of(1996, Month.SEPTEMBER, 1), LocalDate.of(1998, Month.MARCH, 1),
                "6 месяцев обучения цифровым телефонным сетям (Москва)", "-");
        List<Period> learnList = List.of(learnOne, learnTwo);
        Organization schoolOne = new Organization("Санкт-Петербургский национальный исследовательский университет " +
                "информационных технологий, механики и оптики", "http://www.ifmo.ru/",
                learnList);
        learnList = List.of(learnThree);
        Organization schoolTwo = new Organization("Alcatel", "https://www.alcatel.ru/", learnList);
        List<Organization> education = Arrays.asList(schoolOne, schoolTwo);
        OrganizationSection educationData = new OrganizationSection(education);
        fillSectionEducation(educationData);
    }

    public static Resume fillTestResume(String uuid, String fullName) {
        resume = new Resume(uuid, fullName);
        fillResumeContacts();
        fillResumeSections();
        return resume;
    }

    public static void main(String[] args) {
        Resume testResume = fillTestResume("0001", "Григорий Кислин");
        showResume(testResume);
    }
}
