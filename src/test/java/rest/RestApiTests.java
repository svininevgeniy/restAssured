package rest;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import pojos.CreateStudentRequest;
import pojos.CreateStudentResponse;
import pojos.CreateStudentResponseWithoutId;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class RestApiTests {

    public static CreateStudentRequest createTestStudentWithId(int id){
        CreateStudentRequest createStudentRequest = new CreateStudentRequest(id, "John",
                new ArrayList<>() {{add(5); add(4); add(3);}});
        given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("http://localhost:8080/student");

        return createStudentRequest;
    }

    public static CreateStudentRequest createTestStudentWithAllAttributes(int id, String name, ArrayList<Integer> marks){
        CreateStudentRequest createStudentRequest = new CreateStudentRequest(id, name,
                marks);
        given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("http://localhost:8080/student");

        return createStudentRequest;
    }

    public static void deleteTestStudent(int id){
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("http://localhost:8080/student/" + id);
    }

    @Test
    @DisplayName("Тест №1")
    public void getTestStudentById(){
        int expectedId = 23;
        String expectedName = "John";
        createTestStudentWithId(23);

        CreateStudentResponse response = given()
                .when()
                .get("http://localhost:8080/student/23")
                .then()
                .statusCode(200)
                .extract().body().as(CreateStudentResponse.class);

        Assert.assertTrue(expectedName.equals(response.getName()));
        Assert.assertTrue(expectedId == response.getId());

        deleteTestStudent(23);
    }

    @Test
    @DisplayName("Тест №2")
    public void getUnrealStudent(){
        given()
                .when()
                .get("http://localhost:8080/student/999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Тест №3")
    public void postNewStudent(){
        CreateStudentRequest createStudentRequest = createTestStudentWithId(23);

        given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("http://localhost:8080/student")
                .then()
                .statusCode(201);

        deleteTestStudent(23);
    }

    @Test
    @DisplayName("Тест №4")
    public void postUpdateStudent(){
        CreateStudentRequest createStudentRequest = new CreateStudentRequest(24, "Alex",
                new ArrayList<>() {{add(3); add(3); add(3);}});

        given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("http://localhost:8080/student")
                .then()
                .statusCode(201);

        deleteTestStudent(24);
    }

    @Test
    @DisplayName("Тест №5")
    public void postAddStudentWithoutId(){
        CreateStudentRequest createStudentRequest = new CreateStudentRequest(null, "Alex",
                new ArrayList<Integer>() {{add(5); add(4); add(3);}});

        CreateStudentResponseWithoutId createStudentResponseWithoutId = given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("http://localhost:8080/student")
                .then()
                .statusCode(201)
                .extract().body().as(CreateStudentResponseWithoutId.class);

        Assert.assertNotNull(createStudentResponseWithoutId.getId());

        deleteTestStudent(createStudentResponseWithoutId.getId());
    }

    @Test
    @DisplayName("Тест №6")
    public void postAddStudentWithoutName() {
        CreateStudentRequest createStudentRequest = new CreateStudentRequest(22,
                new ArrayList<Integer>() {{add(5); add(4); add(3);}});

        given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("http://localhost:8080/student")
                .then()
                .statusCode(400);

        deleteTestStudent(22);
    }

    @Test
    @DisplayName("Тест №7")
    public void deleteStudentWithId(){
        CreateStudentRequest createStudentRequest = createTestStudentWithId(25);

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("http://localhost:8080/student/" + createStudentRequest.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Тест №8")
    public void deleteUnrealStudent(){
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("http://localhost:8080/student/999")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Тест №9")
    public void getTopStudent(){
        given()
                .when()
                .get("http://localhost:8080/topStudent")
                .then()
                .statusCode(200).and().body(Matchers.anything());
    }

    @Test
    @DisplayName("Тест №10")
    public void getTopWithoutMarks(){
        CreateStudentRequest createStudentRequest = new CreateStudentRequest(23, "Timmy");

        given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("http://localhost:8080/student");

        given()
                .when()
                .get("http://localhost:8080/topStudent")
                .then()
                .statusCode(200).and().body(Matchers.anything());

        deleteTestStudent(23);
    }

    @Test
    @DisplayName("Тест №11")
    public void getTopOneBestStudent(){
        int expectedResult = 1;
        CreateStudentRequest createStudentRequest1 = createTestStudentWithAllAttributes(23, "John",
                new ArrayList<>() {{add(5); add(4); add(5);}});
        CreateStudentRequest createStudentRequest2 = createTestStudentWithAllAttributes(24, "Buzz",
                new ArrayList<>() {{add(5); add(4); add(3);}});
        CreateStudentRequest createStudentRequest3 = createTestStudentWithAllAttributes(25, "John",
                new ArrayList<>() {{add(5); add(4);}});

        List<CreateStudentRequest> listOfStudents =
        given()
                .when()
                .get("http://localhost:8080/topStudent")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", CreateStudentRequest.class);

        Assert.assertEquals(expectedResult, listOfStudents.size());

        deleteTestStudent(23);
        deleteTestStudent(24);
        deleteTestStudent(25);

    }

    @Test
    @DisplayName("Тест №12")
    public void getTopSeveralStudents(){
        int expectedResult = 2;
        CreateStudentRequest createStudentRequest1 = createTestStudentWithAllAttributes(23, "John",
                new ArrayList<>() {{add(5); add(4); add(5);}});
        CreateStudentRequest createStudentRequest2 = createTestStudentWithAllAttributes(24, "Buzz",
                new ArrayList<>() {{add(5); add(4); add(5);}});

        List<CreateStudentRequest> listOfStudents =
                given()
                        .when()
                        .get("http://localhost:8080/topStudent")
                        .then()
                        .statusCode(200)
                        .extract().body().jsonPath().getList(".", CreateStudentRequest.class);

        Assert.assertEquals(expectedResult, listOfStudents.size());

        deleteTestStudent(23);
        deleteTestStudent(24);
    }


}
