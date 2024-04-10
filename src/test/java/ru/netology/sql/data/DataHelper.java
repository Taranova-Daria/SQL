package ru.netology.sql.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;

public class DataHelper {

    private static Faker faker = new Faker(new Locale("en"));
    private static String testName = "vasya";
    private static String testPassword = "qwerty123";

    private DataHelper() {}

    @Value
    public static class AuthData {
        String login;
        String password;
    }

    public static AuthData getAuthData() {
        return new AuthData(getTestName(), getTestPassword());
    }

    public static String generateLogin() {
        return faker.name().username();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }

    public static AuthData generateUser() {
        return new AuthData(generateLogin(), generatePassword());
    }

    public static String generateCode() {
        return faker.number().digits(6);
    }

    public static String getTestName() {
        return testName;
    }

    public static String getTestPassword() {
        return testPassword;
    }

    public static AuthData getTestAuthData() {
        return new AuthData(getTestName(), getTestPassword());
    }
}