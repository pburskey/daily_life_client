Feature: Sunny Day

  Background:
    Given an AWS Stage: "dev"
    Given an environment provided AWS Bucket Name
    Given an environment provided IAM Access Key
    Given an environment provided IAM Secret Access Key
    Given an AWS Region: "us-east-2"
    When I bootstrap the URIs
    Then I have a party save uri
    Then I have a party find by id uri
    Given an AWS Client


  Scenario: Simple Save Person
    Given that I want to save a person
      | Given Name | Middle Name | Surname | Date of Birth |
      | first      | middle      | last    |               |
    When I ask the service to save the person
    Then the service responds with status code: 200
    Then the person has an ID


  Scenario: Simple Find Saved Person
    Given a saved person
    Then the person has an ID
    When I ask the service to find the person by ID
    Then the service responds with status code: 200
    And the person is found


  Scenario: Simple Save Person - Validation first name
    Given that I want to save a person
      | Given Name | Middle Name | Surname | Date of Birth |
      |            | middle      | last    |               |
    When I ask the service to save the person
    Then the service responds with status code: 400
    Then the response has a message: "Please provide a given name"

  Scenario: Simple Save Person - Validation middle name
    Given that I want to save a person
      | Given Name | Middle Name | Surname | Date of Birth |
      | first      | middle      | last    |               |
    When I ask the service to save the person
    Then the service responds with status code: 200
    Then the person has an ID


  Scenario: Simple Save Person - Validation last name
    Given that I want to save a person
      | Given Name | Middle Name | Surname | Date of Birth |
      | first      | middle      |         |               |
    When I ask the service to save the person
    Then the service responds with status code: 400
    Then the response has a message: "Please provide a surname"