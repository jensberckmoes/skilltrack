Feature: Login
  As a candidate for the assessment program: Skilltrack, I want to be able to log in, so I can actually take the assessments

  Background:
    Given I am on the login page

  Scenario: User cannot log in with invalid username
    When I attempt to log in with username "nonexistent_user" and token "valid_token"
    Then I am blocked

  Scenario: User cannot log in with invalid token
    When I attempt to log in with username "jane.doe@example.com" and token "wrong_token"
    Then I am blocked

  Scenario: User cannot log in with expired token
    When I attempt to log in with username "old.user@example.com" and token "expired_token"
    Then I am blocked

  Scenario: User can log in with valid token and valid username
    When I attempt to log in with username "jane.doe@example.com" and token "valid_token"
    Then I can successfully log in