Feature: Task Management
  Enterprise users should be able to create and manage tasks

  Scenario: Create a new task
    Given a user is authenticated with role "ROLE_USER"
    When the user creates a task with title "Implement API" and description "Create RESTful endpoints"
    Then the task should be successfully created
    And the task should have "TODO" status

  Scenario: Assign a task to a user
    Given a task with title "Implement API" exists
    And a user with username "developer" exists
    When a manager assigns the task to "developer"
    Then the task should be assigned to "developer"

  Scenario: Change task status
    Given a user is authenticated as the assignee of task "Implement API"
    When the user changes the task status to "IN_PROGRESS"
    Then the task status should be updated to "IN_PROGRESS"