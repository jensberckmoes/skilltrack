Feature: Login of a User
  As a candidate for the assessment program: Skilltrack, I want to be able to log in, so I can actually take the assessments

  Background:
    Given I am on the login page

  Scenario: fails when given an invalid username
    When I attempt to log in with username "hans@example.com" and token "A1b2C3d4E5f6G7h8I9j"
    Then I am blocked

  Scenario: fails when given an invalid token
    When I attempt to log in with username "alice@example.com" and token "X1y2Z3a4B5c6D7e8F9g"
    Then I am blocked

  Scenario: fails when given an expired token
    When I attempt to log in with username "bob@example.com" and token "Z9y8X7w6V5u4T3s2R1q"
    Then I am blocked

  Scenario: fails when given a token that does not belong to given username
    When I attempt to log in with username "geert@example.com" and token "A1b2C3d4E5f6G7h8I9j"
    Then I am blocked

  Scenario: Succeeds when given a valid not-expired token and a valid username
    When I attempt to log in with username "alice@example.com" and token "A1b2C3d4E5f6G7h8I9j"
    Then I can successfully log in