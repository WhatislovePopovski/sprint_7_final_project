package tests;

import api.OrderApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit5.AllureJunit5;
import models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(AllureJunit5.class)
public class CreateOrderTest {
    private OrderApi orderApi;

    @BeforeEach
    void setUp() {
        orderApi = new OrderApi();
    }

    @ParameterizedTest
    @MethodSource("colorOptionsProvider")
    @DisplayName("Проверка создания заказа с разными цветами")
    @Description("Заказ можно создать с разными комбинациями цветов")
    void createOrderWithDifferentColorsTest(List<String> colors) {
        Order order = Order.builder()
                .firstName("Тест")
                .lastName("Тестов")
                .address("Москва, ул. Тестовая, 1")
                .metroStation("Лубянка")
                .phone("+79998887766")
                .rentTime(5)
                .deliveryDate("2024-12-31")
                .color(colors)
                .comment("Тестовый заказ")
                .build();

        orderApi.createOrder(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    static Stream<Arguments> colorOptionsProvider() {
        return Stream.of(
                Arguments.of(Collections.singletonList("BLACK")),
                Arguments.of(Collections.singletonList("GREY")),
                Arguments.of(Arrays.asList("BLACK", "GREY")),
                Arguments.of(Collections.emptyList())
        );
    }

    @Test
    @Description("Проверка: тело ответа содержит track")
    void createOrderReturnsTrackTest() {
        Order order = Order.builder()
                .firstName("Тест")
                .lastName("Тестов")
                .address("Москва, ул. Тестовая, 1")
                .metroStation("Лубянка")
                .phone("+79998887766")
                .rentTime(5)
                .deliveryDate("2024-12-31")
                .color(Collections.singletonList("BLACK"))
                .comment("Тестовый заказ")
                .build();

        orderApi.createOrder(order)
                .then()
                .body("track", notNullValue());
    }
}