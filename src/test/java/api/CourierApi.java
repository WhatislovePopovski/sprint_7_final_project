package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;

import static io.restassured.RestAssured.given;

public class CourierApi {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL)
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Логин курьера")
    public Response loginCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL)
                .body(courier)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удаление курьера")
    public Response deleteCourier(int courierId) {
        return given()
                .baseUri(BASE_URL)
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }
}