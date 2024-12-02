Feature: Sunny Day

  Background:
    Given an AWS Stage: "dev"
#    Given a property save uri: "https://zeb8w5qk26.execute-api.us-east-2.amazonaws.com/Stage/save"
#    Given a property get by id uri: "https://zeb8w5qk26.execute-api.us-east-2.amazonaws.com/Stage/"
#    Given a property get by category and name uri: "https://zeb8w5qk26.execute-api.us-east-2.amazonaws.com/Stage/find"
    Given an environment provided AWS Bucket Name
    Given an environment provided IAM Access Key
    Given an environment provided IAM Secret Access Key
    Given an AWS Region: "us-east-2"
    When I bootstrap the URIs
    Given an AWS Client

@me
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


  @me
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



