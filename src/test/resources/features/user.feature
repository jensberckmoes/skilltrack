Feature: User Management via controller
  As an administrator, I am interested in managing users through the controller to ensure that I can perform CRUD operations effectively.

  Scenario: Get all users
    When I browse to get all users
    Then the response contains a list of all the users

  @NoUsers
  Scenario: Get all users but none are found
    When I browse to get all users but none are found
    Then the response contains a message declaring that no users were found

  Scenario: Succeeds when given a valid username
    When I browse to get a user with username "jane.doe@example.com"
    Then the response contains the user details

  Scenario: fails when given an empty username
    When I browse to get a user with no username
    Then the response contains a message "Username can not be null"

  Scenario: fails when given an empty username
    When I browse to get a user with username " "
    Then the response contains a message "Username can not be blank"

  Scenario: fails when given an null username
    When I browse to get a user with username ""
    Then the response contains a message "Username can not be blank"