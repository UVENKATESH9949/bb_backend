package com.BrainBlitz;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserApiTest {

    @LocalServerPort
    private int port;

    String userToken;
    String adminToken;

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        System.out.println("Tests running on port: " + port);
    }

    @Test @Order(1)
    void register_ValidUser_Returns200() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name":"John Doe","email":"john@gmail.com","password":"Test@123"}
                """)
        .when()
            .post("/api/users/register")
        .then()
            .statusCode(200)
            .body("success", equalTo(true));
    }

    @Test @Order(2)
    void register_DuplicateEmail_Returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"name":"John Doe","email":"john@gmail.com","password":"Test@123"}
                """)
        .when()
            .post("/api/users/register")
        .then()
            .statusCode(400)
            .body("message", equalTo("Email already registered"));
    }

    @Test @Order(3)
    void register_MissingName_Returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":"john@gmail.com","password":"Test@123"}
                """)
        .when()
            .post("/api/users/register")
        .then()
            .statusCode(400);
    }

    @Test @Order(4)
    void verifyOtp_WrongOtp_Returns400() {
        given()
            .param("email", "john@gmail.com")
            .param("otp", 000000)
        .when()
            .post("/api/users/verify-otp")
        .then()
            .statusCode(400)
            .body("message", equalTo("Invalid OTP"));
    }

    @Test @Order(5)
    void verifyOtp_UnknownEmail_Returns404() {
        given()
            .param("email", "ghost@x.com")
            .param("otp", 123456)
        .when()
            .post("/api/users/verify-otp")
        .then()
            .statusCode(404)
            .body("message", equalTo("User not found"));
    }

    @Test @Order(6)
    void resendOtp_UnverifiedUser_Returns200() {
        given()
            .param("email", "john@gmail.com")
        .when()
            .post("/api/users/resend-otp")
        .then()
            .statusCode(200)
            .body("success", equalTo(true));
    }

    @Test @Order(7)
    void resendOtp_UnknownEmail_Returns400() {
        given()
            .param("email", "nobody@x.com")
        .when()
            .post("/api/users/resend-otp")
        .then()
            .statusCode(400)
            .body("message", equalTo("User not found"));
    }

    @Test @Order(8)
    void login_ValidUser_Returns200AndToken() {
        userToken =
            given()
                .param("email", "john@gmail.com")
                .param("password", "Test@123")
            .when()
                .post("/api/users/login")
            .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract().path("data");

        System.out.println("User Token: " + userToken);
    }

    @Test @Order(9)
    void login_WrongPassword_Returns401() {
        given()
            .param("email", "john@gmail.com")
            .param("password", "wrongpass")
        .when()
            .post("/api/users/login")
        .then()
            .statusCode(401)
            .body("message", equalTo("Invalid password"));
    }

    @Test @Order(10)
    void login_NonExistentUser_Returns401() {
        given()
            .param("email", "ghost@x.com")
            .param("password", "Test@123")
        .when()
            .post("/api/users/login")
        .then()
            .statusCode(401)
            .body("message", equalTo("User not found"));
    }

    @Test @Order(11)
    void adminLogin_ValidAdmin_Returns200AndToken() {
        adminToken =
            given()
                .contentType(ContentType.JSON)
                .body("""
                    {"email":"admin@brainblitz.com","password":"Admin@123"}
                    """)
            .when()
                .post("/api/admin/login")
            .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract().path("data");

        System.out.println("Admin Token: " + adminToken);
    }

    @Test @Order(12)
    void adminLogin_NormalUserEmail_Returns401() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":"john@gmail.com","password":"Test@123"}
                """)
        .when()
            .post("/api/admin/login")
        .then()
            .statusCode(401)
            .body("message", equalTo("Access denied: Admin only"));
    }

    @Test @Order(13)
    void adminLogin_WrongPassword_Returns401() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {"email":"admin@brainblitz.com","password":"wrongpass"}
                """)
        .when()
            .post("/api/admin/login")
        .then()
            .statusCode(401)
            .body("message", equalTo("Invalid credentials"));
    }

    @Test @Order(14)
    void forgotPassword_VerifiedEmail_Returns200() {
        given()
            .param("email", "john@gmail.com")
        .when()
            .post("/api/users/forgot-password")
        .then()
            .statusCode(200)
            .body("success", equalTo(true));
    }

    @Test @Order(15)
    void forgotPassword_UnknownEmail_Returns400() {
        given()
            .param("email", "nobody@x.com")
        .when()
            .post("/api/users/forgot-password")
        .then()
            .statusCode(400)
            .body("message", equalTo("User not found"));
    }

    @Test @Order(16)
    void resetPassword_WrongOtp_Returns400() {
        given()
            .param("email", "john@gmail.com")
            .param("otp", 000000)
            .param("newPassword", "NewPass@456")
        .when()
            .post("/api/users/reset-password")
        .then()
            .statusCode(400)
            .body("message", equalTo("Invalid OTP"));
    }

    @Test @Order(17)
    void adminDashboard_WithAdminToken_Returns200() {
        given()
            .header("Authorization", "Bearer " + adminToken)
        .when()
            .get("/api/admin/dashboard")
        .then()
            .statusCode(200);
    }

    @Test @Order(18)
    void adminDashboard_WithUserToken_Returns403() {
        given()
            .header("Authorization", "Bearer " + userToken)
        .when()
            .get("/api/admin/dashboard")
        .then()
            .statusCode(403);
    }

    @Test @Order(19)
    void adminDashboard_NoToken_Returns401() {
        given()
        .when()
            .get("/api/admin/dashboard")
        .then()
            .statusCode(401);
    }

    @Test @Order(20)
    void adminDashboard_TamperedToken_Returns401() {
        given()
            .header("Authorization", "Bearer abc123tampered")
        .when()
            .get("/api/admin/dashboard")
        .then()
            .statusCode(401);
    }
}