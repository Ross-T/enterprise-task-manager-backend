Feature: Project Management
  Enterprise users should be able to organize tasks into projects

  Scenario: Create a new project
    Given a user is authenticated with role "ROLE_MANAGER"
    When the user creates a project with name "API Development" and description "RESTful API implementation"
    Then the project should be successfully created
    And the user should be set as project owner

  Scenario: Add members to a project
    Given a project named "API Development" exists
    And users "developer1" and "developer2" exist
    When a manager adds "developer1" and "developer2" to the project
    Then the project should have "developer1" and "developer2" as members

  Scenario: Add task to project
    Given a project named "API Development" exists
    And a task with title "Implement API" exists
    When a user adds the task to the project
    Then the task should be associated with the project