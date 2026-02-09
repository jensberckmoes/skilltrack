Feature: User Management via controller
  As an administrator, I am interested in managing users through the controller to ensure that I can perform CRUD operations effectively.

  Scenario: Get all users
    When I browse to get all users
    Then the response contains a list of all the users