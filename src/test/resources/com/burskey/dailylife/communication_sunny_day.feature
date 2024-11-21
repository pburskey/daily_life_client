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
    Then I have a party save uri
    Then I have a party find by id uri
    Then I have a commmunication save uri
    Then I have a communication find by id uri
    Then I have a communication find by party id uri
    Given an AWS Client


  Scenario: Simple Save Communication
    Given a saved person
    Then the person has an ID
    Given that I want to save a communication
      | Communication |
      | abc123        |
    When I associate the party with the communication
    Then the communication has a party
    Then the communication does not have an ID
    When I ask the service to save the communication
    Then the service responds with status code: 200
    Then the communication has an ID



  Scenario: Simple Find Communication
    Given a saved person
    Then the person has an ID
    Given a saved communication
    Then the communication has a party
    Then the communication has an ID
    When I ask the service to find the communication by id
    Then the service responds with status code: 200



  Scenario: Simple Save Communication - Validation communication
    Given a saved person
    Then the person has an ID
    Given that I want to save a communication
      | Communication |
      |               |
    When I associate the party with the communication
    Then the communication has a party
    Then the communication does not have an ID
    When I ask the service to save the communication
    Then the service responds with status code: 400
    Then the response has a message: "Please provide a communication"
