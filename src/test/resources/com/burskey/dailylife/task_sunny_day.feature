Feature: Sunny Day

  Background:
    Given an AWS Stage: "dev"
    Given an environment provided AWS Bucket Name
    Given an environment provided IAM Access Key
    Given an environment provided IAM Secret Access Key
    Given an AWS Region: "us-east-2"
    When I bootstrap the URIs
    Given an AWS Client


  Scenario: Simple Save Task
    Given a saved person
    Given a task with a simple status open closed machine
      | Title | Description |
      | a     | b           |

    When I associate the party with the task
    When I ask the service to save the task
    Then the service responds with status code: 200
    Then the task has an ID
    Then the task is associated with a party


  Scenario: Simple Start Task
    Given a saved person
    Given a party task with a simple status open closed machine
    When I associate the party with the task
    When I ask the service to save the task
    Then the service responds with status code: 200
    Then the task has an ID
    Then the task is associated with a party
    When I start work on the task
    Then the task has a task in progress
    Then the status of the task in progress is "open"
    When I change the status to the next available status
    Then the status of the task in progress is "closed"




  Scenario: Find Task By Task ID
    Given a saved person
    Given a party task with a simple status open closed machine
    When I associate the party with the task
    When I ask the service to save the task
    Then the service responds with status code: 200
    Then the task has an ID
    Then the task is associated with a party
    When I ask the service to get the task by id
    Then the service responds with status code: 200
    And the task has been found


  Scenario: Find Task By Party ID
    Given a saved person
    Given a party task with a simple status open closed machine
    When I associate the party with the task
    When I ask the service to save the task
    Then the service responds with status code: 200
    Then the task has an ID
    Then the task is associated with a party
    When I ask the service to get the task by party
    Then the service responds with status code: 200
    And the tasks have been found


  Scenario: Start task and resume
    Given a saved person
    Given a party task with a simple status open closed machine
    When I associate the party with the task
    When I ask the service to save the task
    Then the service responds with status code: 200
    Then the task has an ID
    Then the task is associated with a party
    When I start work on the task
    Then the task has a task in progress
    Then the status of the task in progress is "open"




  @me
  Scenario: Find tasks in progress by task id
    Given a saved person
    Given a party task with a simple status open closed machine
    When I associate the party with the task
    When I ask the service to save the task
    Then the service responds with status code: 200
    Then the task has an ID
    Then the task is associated with a party
    When I start work on the task
    Then the task has a task in progress
    Then the status of the task in progress is "open"
    When I search for tasks in progress associated with the task and party
    Then the service responds with status code: 200
    And 1 tasks in progress have been found