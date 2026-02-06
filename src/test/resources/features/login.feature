Feature: Login of a User
  As a candidate for the assessment program: Skilltrack, I want to be able to log in, so I can actually take the assessments

  Background:
    Given I am on the login page

  Scenario: fails when given an invalid username
    When I attempt to log in with username "nonexistent_user" and token "valid_token"
    Then I am blocked

  Scenario: fails when given an invalid token
    When I attempt to log in with username "jane.doe@example.com" and token "wrong_token"
    Then I am blocked

  Scenario: fails when given an expired token
    When I attempt to log in with username "old.user@example.com" and token "expired_token"
    Then I am blocked

  Scenario: fails when given a token that does not belong to given username
    When I attempt to log in with username "belongs_to_other_user@example.com" and token "other_valid_token"
    Then I am blocked

  Scenario: Succeeds when given a valid not-expired token and a valid username
    When I attempt to log in with username "jane.doe@example.com" and token "valid_token"
    Then I can successfully log in