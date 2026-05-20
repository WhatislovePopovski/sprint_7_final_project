package tests;

import api.CourierApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit5.AllureJunit5;
import models.Courier;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.*;

@Tag("login")
@ExtendWith(AllureJunit5.class)
public class LoginCourierTest {
    private CourierApi courierApi;
    private static int testCourierId;
    private static String testLogin;
    private static String testPassword;

    @BeforeAll
    static void createTestCourier() {
        CourierApi api = new CourierApi();
        testLogin = "login_test_" + System.currentTimeMillis();
        testPassword = "password123";

        Courier courier = Courier.builder()
                .login(testLogin)
                .password(testPassword)
                .firstName("TestLogin")
                .build();

        api.createCourier(courier);
        testCourierId = api.loginCourier(courier)
                .then()
                .extract()
                .path("id");
    }

    @BeforeEach
    void setUp() {
        courierApi = new CourierApi();
    }

    @Test
    @Description("Проверка: курьер может авторизоваться")
    void loginCourierSuccessTest() {
        Courier courier = Courier.builder()
                .login(testLogin)
                .password(testPassword)
                .build();

        courierApi.loginCourier(courier)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @Description("Проверка: для авторизации нужны все обязательные поля")
    void loginWithoutPasswordTest() {
        Courier courier = Courier.builder()
                .login(testLogin)
                .build();

        courierApi.loginCourier(courier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Проверка: ошибка при неправильном логине")
    void loginWithWrongLoginTest() {
        Courier courier = Courier.builder()
                .login("wrong_login")
                .password(testPassword)
                .build();

        courierApi.loginCourier(courier)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Проверка: ошибка при неправильном пароле")
    void loginWithWrongPasswordTest() {
        Courier courier = Courier.builder()
                .login(testLogin)
                .password("wrong_password")
                .build();

        courierApi.loginCourier(courier)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Проверка: авторизация под несуществующим пользователем")
    void loginNonExistentUserTest() {
        Courier courier = Courier.builder()
                .login("nonexistent_" + System.currentTimeMillis())
                .password("any_password")
                .build();

        courierApi.loginCourier(courier)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Проверка: успешный запрос возвращает id")
    void loginReturnsIdTest() {
        Courier courier = Courier.builder()
                .login(testLogin)
                .password(testPassword)
                .build();

        courierApi.loginCourier(courier)
                .then()
                .body("id", is(testCourierId));
    }

    @AfterAll
    static void cleanup() {
        CourierApi api = new CourierApi();
        api.deleteCourier(testCourierId);
    }
}