package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderApi {
    private static final String BASE_URL = "https://qa-scooter.education-services.ru";
    private static final String ORDERS_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL)
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get(ORDERS_PATH);
    }
}