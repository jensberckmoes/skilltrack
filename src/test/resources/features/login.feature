Feature: Login
  As a candidate for the assessment program: Skilltrack, I want to be able to log in, so I can actually take the assessments

  Background:
    Given I am on the login page

  Scenario: User cannot log in with invalid username
    When I enter username "nonexistent_user" and token "valid_token"
    Then I am blocked and prompted to contact support

  Scenario: User cannot log in with invalid token
    When I enter username "jane.doe@example.com" and token "wrong_token"
    Then I am blocked and prompted to contact support