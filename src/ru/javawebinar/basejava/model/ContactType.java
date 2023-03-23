package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONE("Телефон"),
    EMAIL("Электронная почта"),
    LINK_HEADER("Имя ссылки"),
    LINK("Ссылка");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
