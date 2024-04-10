package ru.netology.sql.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.sql.data.DataHelper;
import ru.netology.sql.page.LoginPage;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static ru.netology.sql.data.DataHelper.*;
import static ru.netology.sql.data.SQLHelper.*;

public class BankLoginTest {
    LoginPage loginPage;


    @AfterEach
    void cleanCode() {
        cleanAuth_code();
    }

    @AfterAll
    static void cleanAll() {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Must log in as a registered user with the correct code")
    void shouldPositiveLogin() {
        var verificationPage = loginPage.validLogin(getAuthData());
        verificationPage.verificationPageIsVisible();
        verificationPage.validVerify(getVerificationCode());
    }

    @Test
    @DisplayName("A login error should appear.")
    void shouldNotLoginWithInvalidLogin() {
        loginPage.validLogin(DataHelper.getTestAuthData());
        loginPage.findErrorMessage("Неверно указан логин или пароль");
    }

    @Test
    @DisplayName("An error should appear if the password is incorrect.")
    void shouldNotLoginWithInvalidPassword() {
        loginPage.validLogin(new AuthData(generateLogin(), DataHelper.generatePassword()));
        loginPage.findErrorMessage("Неверно указан логин или пароль");
    }

    @Test
    @DisplayName("An error should appear if the login and password are incorrect")
    void shouldNotLoginWithInvalidLoginAndPassword() {
        loginPage.validLogin(generateUser());
        loginPage.findErrorMessage("Неверно указан логин или пароль");
    }

    @Test
    @DisplayName("An error should appear if the verification code is incorrect.")
    void shouldNotLoginWithInvalidCode() {
        var verificationPage = loginPage.validLogin(getAuthData());
        verificationPage.verify(generateCode());
        verificationPage.findErrorMessage("Неверно указан код!");
    }

    @Test
    @DisplayName("Should blocked user when invalid password more 3 times enter")
    void shouldBlockedWhenInvalidPasswordEnterMoreTimes() {
        for (int i = 0; i < 3; i++) {
            refresh();
            loginPage.validLogin(new AuthData(DataHelper.generateLogin(), generatePassword()));
            loginPage.findErrorMessage("Неверно указан логин или пароль");
        }
        String expected = "blocked";
        String actual = getStatus();
        Assertions.assertEquals(expected, actual);
    }

}