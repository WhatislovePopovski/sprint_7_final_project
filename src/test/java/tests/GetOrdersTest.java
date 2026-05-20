package tests;

import api.OrderApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(AllureJunit5.class)
public class GetOrdersTest {
    private OrderApi orderApi;

    @BeforeEach
    void setUp() {
        orderApi = new OrderApi();
    }

    @Test
    @Description("Проверка: в тело ответа возвращается список заказов")
    void getOrdersListTest() {
        orderApi.getOrders()
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}