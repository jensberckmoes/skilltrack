package com.sopra_steria.jens_berckmoes.BDD.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloStepDefinitions {

    private String message;

    @Given("a user says hello")
    public void a_user_says_hello() {
        System.out.println("Step: Given a user says hello");
    }

    @When("the system responds")
    public void the_system_responds() {
        message = "Hello World!";
        System.out.println("Step: When the system responds");
    }

    @Then("the message should be {string}")
    public void the_message_should_be(String expectedMessage) {
        System.out.println("Step: Then the message should be " + expectedMessage);
        assertEquals(expectedMessage, message);
    }
}
