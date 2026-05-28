package tests;

import api.CourierApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit5.AllureJunit5;
import models.Courier;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.*;

@ExtendWith(AllureJunit5.class)
public class CreateCourierTest {
    private CourierApi courierApi;
    private int createdCourierId;
    private String createdLogin;
    private String createdPassword;

    @BeforeEach
    void setUp() {
        courierApi = new CourierApi();
    }

    @AfterEach
    void cleanup() {
        if (createdCourierId > 0) {
            courierApi.deleteCourier(createdCourierId);
        }
    }

    @Test
    @Description("Проверка: курьера можно создать")
    void createCourierSuccessTest() {
        createdLogin = "test_courier_" + System.currentTimeMillis();
        createdPassword = "password123";

        Courier courier = Courier.builder()
                .login(createdLogin)
                .password(createdPassword)
                .firstName("TestName")
                .build();

        courierApi.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", is(true));

        // Получаем id для последующего удаления
        createdCourierId = courierApi.loginCourier(courier)
                .then()
                .extract()
                .path("id");
    }

    @Test
    @Description("Проверка: нельзя создать двух одинаковых курьеров")
    void createDuplicateCourierTest() {
        String uniqueLogin = "duplicate_" + System.currentTimeMillis();
        String uniquePassword = "password123";

        Courier courier = Courier.builder()
                .login(uniqueLogin)
                .password(uniquePassword)
                .firstName("TestName")
                .build();

        courierApi.createCourier(courier)
                .then()
                .statusCode(201);

        courierApi.createCourier(courier)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        int courierId = courierApi.loginCourier(courier)
                .then()
                .extract()
                .path("id");
        courierApi.deleteCourier(courierId);
    }

    @Test
    @Description("Проверка: для создания курьера нужны все обязательные поля")
    void createCourierWithoutLoginTest() {
        Courier courier = Courier.builder()
                .password("password123")
                .firstName("TestName")
                .build();

        courierApi.createCourier(courier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Проверка: запрос возвращает правильный код ответа при успешном создании")
    void createCourierReturnCorrectStatusCodeTest() {
        String login = "status_test_" + System.currentTimeMillis();
        String password = "password123";

        Courier courier = Courier.builder()
                .login(login)
                .password(password)
                .firstName("TestName")
                .build();

        courierApi.createCourier(courier)
                .then()
                .statusCode(201);

        int courierId = courierApi.loginCourier(courier)
                .then()
                .extract()
                .path("id");
        courierApi.deleteCourier(courierId);
    }

    @Test
    @Description("Проверка: успешный запрос возвращает ok: true")
    void createCourierReturnsOkTrueTest() {
        String login = "ok_test_" + System.currentTimeMillis();
        String password = "password123";

        Courier courier = Courier.builder()
                .login(login)
                .password(password)
                .firstName("TestName")
                .build();

        courierApi.createCourier(courier)
                .then()
                .body("ok", is(true));

        int courierId = courierApi.loginCourier(courier)
                .then()
                .extract()
                .path("id");
        courierApi.deleteCourier(courierId);
    }
}