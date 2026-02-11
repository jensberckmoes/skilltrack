Feature: User Management via controller
  As an administrator, I am interested in managing users through the controller to ensure that I can perform CRUD operations effectively.

  Scenario: Get all users: succeeds when there are users to be found
    When I browse to get all users and users exist in the database
    Then the response contains a list of all the users

  @NoUsers
  Scenario: Get all users: fails when there are no users to be found
    When I browse to get all users but none are in the database
    Then the response contains a message declaring that no users were found

  Scenario: Finding a user by id: succeeds when given a valid username
    When I browse to get a user with username "alice@example.com"
    Then the response contains the user details

  Scenario: Finding a user by id: fails when given an empty username
    When I browse to get a user with no username
    Then the response contains a message "Username can not be null"

  Scenario: Finding a user by id:fails when given an empty username
    When I browse to get a user with username " "
    Then the response contains a message "Username can not be blank"

  Scenario: Finding a user by id: fails when given an null username
    When I browse to get a user with username ""
    Then the response contains a message "Username can not be blank"

  Scenario: Finding a user by id: fails when given a non existing username
    When I browse to get a user with username "jane.doe2@example.com"
    Then the response contains a message "User not found: jane.doe2@example.com"

  Scenario: Create a user: succeeds when given valid user details
    When I browse to create a user with username "josken.vermeulen@gmail.com"
    Then the response contains the created user details