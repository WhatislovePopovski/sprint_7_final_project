package tests;

import api.CourierApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit5.AllureJunit5;
import models.Courier;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.*;

@ExtendWith(AllureJunit5.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateCourierTest {
    private CourierApi courierApi;
    private static int createdCourierId;

    @BeforeEach
    void setUp() {
        courierApi = new CourierApi();
    }

    @Test
    @Order(1)
    @Description("Проверка: курьера можно создать")
    void createCourierSuccessTest() {
        Courier courier = Courier.builder()
                .login("test_courier_" + System.currentTimeMillis())
                .password("password123")
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
    @Order(2)
    @Description("Проверка: нельзя создать двух одинаковых курьеров")
    void createDuplicateCourierTest() {
        String uniqueLogin = "duplicate_" + System.currentTimeMillis();
        Courier courier = Courier.builder()
                .login(uniqueLogin)
                .password("password123")
                .firstName("TestName")
                .build();

        // Создаем первого курьера
        courierApi.createCourier(courier)
                .then()
                .statusCode(201);

        // Пытаемся создать такого же
        courierApi.createCourier(courier)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой." +
                        ""));

        // Получаем id для удаления
        int courierId = courierApi.loginCourier(courier)
                .then()
                .extract()
                .path("id");
        courierApi.deleteCourier(courierId);
    }

    @Test
    @Order(3)
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
    @Order(4)
    @Description("Проверка: запрос возвращает правильный код ответа при успешном создании")
    void createCourierReturnCorrectStatusCodeTest() {
        Courier courier = Courier.builder()
                .login("status_test_" + System.currentTimeMillis())
                .password("password123")
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
    @Order(5)
    @Description("Проверка: успешный запрос возвращает ok: true")
    void createCourierReturnsOkTrueTest() {
        Courier courier = Courier.builder()
                .login("ok_test_" + System.currentTimeMillis())
                .password("password123")
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

    @AfterAll
    static void cleanup() {
        if (createdCourierId > 0) {
            CourierApi api = new CourierApi();
            api.deleteCourier(createdCourierId);
        }
    }
}