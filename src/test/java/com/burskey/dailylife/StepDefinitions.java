package com.burskey.dailylife;

import com.burskey.dailylife.api.APIGatewayHelper;
import com.burskey.dailylife.api.AWSClientConfig;
import com.burskey.dailylife.api.PartyClient;
import com.burskey.dailylife.party.domain.Communication;
import com.burskey.dailylife.party.domain.Person;
import com.burskey.dailylife.task.domain.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.Assert.*;

public class StepDefinitions {

    String region = null;
    String accessKey = null;
    String secretAccessKey = null;
    String bucketName = null;
    String s3Bucket = null;
    String stackName = null;
    String stage = null;
    Person person = null;

    private ResponseEntity response;
    private PartyClient client;
    private String partySaveURI;
    private String partyFindByIDURI;
    private Communication communication;
    private String communicationSaveURI;
    private String communicationFindByPartyURI;
    private String communicationFindByPartyAndIdURI;
    private String taskInProgressChangeStatusURI;
    private String taskInProgressByTaskURI;
    private String taskSaveURI;
    private String taskFindByPartyURI;
    private String taskFindByPartyAndIdURI;
    private String taskStartURI;
    private Map<String, Task> tasks;
    private Map<Task, List<TaskInProgress>> tasksInProgress;
    private StatusPoint[] statusHistory;


    @Given("a new scenario")
    public void a_new_scenario() {
        this.response = null;
        this.person = null;
        this.communication = null;

        this.tasks = new HashMap<>();
        this.tasksInProgress = new HashMap<>();
        this.statusHistory = null;
    }

    @Given("an AWS Region: {string}")
    public void an_aws_region(String string) {
        this.region = string;
    }

    @Given("an AWS S3 Bucket: {string}")
    public void an_aws_s3_bucket(String string) {
        this.s3Bucket = string;
    }

    @Given("an AWS Stack Name: {string}")
    public void an_aws_stack_name(String string) {
        this.stackName = string;
    }

    @Given("an AWS Stage: {string}")
    public void an_aws_stage(String string) {
        this.stage = string;
    }


    @Then("the service responds with status code: {int}")
    public void the_service_responds_with_status_code(Integer int1) {
        assertNotNull(this.response);
        int status = this.response.getStatusCode().value();
        assertEquals(int1, Integer.valueOf(status));
    }


    @Given("an AWS Client")
    public void an_aws_client() {
        PartyClient client = PartyClient.Builder()
                .withSave(this.partySaveURI)
                .withFindByID(this.partyFindByIDURI)
                .withCommunicationSave(this.communicationSaveURI)
                .withCommunicationFindByPartyAndId(this.communicationFindByPartyAndIdURI)
                .withCommunicationFindByParty(this.communicationFindByPartyURI)
                .withTaskSave(this.taskSaveURI)
                .withTaskFindByPartyAndId(this.taskFindByPartyAndIdURI)
                .withTaskFindByParty(this.taskFindByPartyURI)
                .withTaskStart(this.taskStartURI)
                .withTaskInProgressChangeStatus(this.taskInProgressChangeStatusURI)
                .withTasksInProgressByTask(this.taskInProgressByTaskURI)
                .withTaskFindByParty(this.taskFindByPartyURI);
        this.client = client;
    }

    @When("I bootstrap the URIs")
    public void i_bootstrap_the_uris() {
        assertNotNull(this.stage);

        AWSClientConfig config = new AWSClientConfig(this.accessKey, this.secretAccessKey, this.bucketName, this.region);
        APIGatewayHelper helper = APIGatewayHelper.With(config);
        String baseURI = helper.constructBaseURIForEnvironment("Daily Life AWS Api Gateway", this.stage);

        LambdaResourceLoader loader = LambdaResourceLoader.BuildUsingBaseURI(baseURI);
        this.partySaveURI = loader.get(LambdaResourceLoader.Thing.PartySave);
        this.partyFindByIDURI = loader.get(LambdaResourceLoader.Thing.PartyFindByID);
        this.communicationSaveURI = loader.get(LambdaResourceLoader.Thing.CommunicationSave);
        this.communicationFindByPartyURI = loader.get(LambdaResourceLoader.Thing.CommunicationFindByParty);
        this.communicationFindByPartyAndIdURI = loader.get(LambdaResourceLoader.Thing.CommunicationFindByPartyAndCommunication);
        this.taskSaveURI = loader.get(LambdaResourceLoader.Thing.TaskSave);
        this.taskFindByPartyURI = loader.get(LambdaResourceLoader.Thing.TaskFindByParty);
        this.taskFindByPartyAndIdURI = loader.get(LambdaResourceLoader.Thing.TaskFindByPartyAndCommunication);
        this.taskStartURI = loader.get(LambdaResourceLoader.Thing.TaskStart);
        this.taskInProgressChangeStatusURI = loader.get(LambdaResourceLoader.Thing.TaskInProgressChangeStatus);
        this.taskInProgressByTaskURI = loader.get(LambdaResourceLoader.Thing.TasksInProgressByTask);

    }

    @Given("an AWS Bucket Name: {string}")
    public void an_aws_bucket_name(String string) {
        this.bucketName = string;
    }

    @Given("an IAM Access Key: {string}")
    public void an_iam_access_key(String string) {
        this.accessKey = string;
    }

    @Given("an IAM Secret Access Key: {string}")
    public void an_iam_secret_access_key(String string) {
        this.secretAccessKey = string;
    }


    public String getFromEnvironmentOrProperty(String string) {
        String value = System.getenv(string);
        if (value == null) {
            value = System.getProperty(string);
        }
        return value;
    }


    @Given("an environment provided AWS Bucket Name")
    public void an_environment_provided_aws_bucket_name() {
        this.bucketName = this.getFromEnvironmentOrProperty("AWS_BUCKET_NAME");
    }

    @Given("an environment provided IAM Access Key")
    public void an_environment_provided_iam_access_key() {
        this.accessKey = this.getFromEnvironmentOrProperty("AWS_ACCESS_KEY");
    }

    @Given("an environment provided IAM Secret Access Key")
    public void an_environment_provided_iam_secret_access_key() {
        this.secretAccessKey = this.getFromEnvironmentOrProperty("AWS_SECRET_ACCESS_KEY");
    }

    @When("I wait for {int} seconds")
    public void i_wait_for_seconds(Integer int1) throws Exception {
        Thread.sleep(int1 * 1000);
    }

    @Then("the response has a message: {string}")
    public void the_response_has_a_message(String string) {
        assertNotNull(this.response);
        boolean found = this.response.getBody().toString().contains(string);
        Assertions.assertTrue(found);
    }


    @Given("that I want to save a person")
    public void that_i_want_to_save_a_person(io.cucumber.datatable.DataTable dataTable) {
        if (dataTable != null && !dataTable.isEmpty()) {

            List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
            Person aPerson = new Person();
            for (Map<String, String> columns : rows) {
                aPerson.setGivenName(columns.get("Given Name"));

                aPerson.setMiddleName(columns.get("Middle Name"));

                aPerson.setSurname(columns.get("Surname"));

//                aPerson.setDateOfBirth(columns.get("Date of Birth"));


            }
            this.person = aPerson;


        }
    }

    @When("I ask the service to save the person")
    public void i_ask_the_service_to_save_the_person() throws JsonProcessingException {
        Assertions.assertNotNull(this.person);
        Assertions.assertNotNull(this.client);
        this.response = this.client.save(this.person);
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            Person aPerson = mapper.readValue((String) this.response.getBody(), Person.class);
            this.person = aPerson;
        }

    }

    @Then("the person has an ID")
    public void the_person_has_an_id() {
        assertNotNull(this.person);
        assertNotNull(this.person.getId());
    }


    @Given("a saved person")
    public void a_saved_person() throws JsonProcessingException {
        Person person = new Person();
        person.setId(null);
        person.setSurname(java.util.UUID.randomUUID().toString());
        person.setMiddleName(java.util.UUID.randomUUID().toString());
        person.setGivenName(java.util.UUID.randomUUID().toString());
        this.person = person;
        this.i_ask_the_service_to_save_the_person();
    }

    @When("I ask the service to find the person by ID")
    public void i_ask_the_service_to_find_the_person_by_id() throws JsonProcessingException {
        Assertions.assertNotNull(this.person);
        Assertions.assertNotNull(this.person.getId());

        this.response = this.client.findPartyByID(this.person.getId());
        ObjectMapper mapper = new ObjectMapper();
        Person aPerson = mapper.readValue((String) this.response.getBody(), Person.class);
        this.person = aPerson;

    }

    @Then("the person is found")
    public void the_person_is_found() {
        Assertions.assertNotNull(this.person);
        Assertions.assertNotNull(this.person.getId());
    }


    @Then("I have a party save uri")
    public void i_have_a_party_save_uri() {
        Assertions.assertNotNull(this.partySaveURI);
    }

    @Then("I have a party find by id uri")
    public void i_have_a_party_find_by_id_uri() {
        Assertions.assertNotNull(this.partyFindByIDURI);
    }


    @Given("that I want to save a communication")
    public void that_i_want_to_save_a_communication(io.cucumber.datatable.DataTable dataTable) throws JsonProcessingException {
        if (dataTable != null && !dataTable.isEmpty()) {

            List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
            Communication aCommunication = new Communication();
            for (Map<String, String> columns : rows) {
                aCommunication.setCommunication(columns.get("Communication"));
            }
            this.communication = aCommunication;


        }
    }

    @When("I associate the party with the communication")
    public void i_associate_the_party_with_the_communication() {
        Assertions.assertNotNull(this.communication);
        Assertions.assertNotNull(this.person);
        Assertions.assertNotNull(this.person.getId());
        this.communication.setPartyID(this.person.getId());
    }

    @Then("the communication has a party")
    public void the_communication_has_a_party() {
        Assertions.assertNotNull(this.communication.getPartyID());
    }

    @Then("the communication does not have an ID")
    public void the_communication_does_not_have_an_id() {
        Assertions.assertNull(this.communication.getId());
    }

    @When("I ask the service to save the communication")
    public void i_ask_the_service_to_save_the_communication() throws JsonProcessingException {
        Assertions.assertNotNull(this.communication);
        Assertions.assertNotNull(this.client);
        this.response = this.client.save(this.communication);
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            Communication aCommunication = mapper.readValue((String) this.response.getBody(), Communication.class);
            this.communication = aCommunication;
        }
    }

    @Then("the communication has an ID")
    public void the_communication_has_an_id() {
        Assertions.assertNotNull(this.communication.getId());
    }

    @Then("I have a commmunication save uri")
    public void i_have_a_commmunication_save_uri() {
        Assertions.assertNotNull(this.communicationSaveURI);
    }

    @Then("I have a communication find by id uri")
    public void i_have_a_communication_find_by_id_uri() {
        Assertions.assertNotNull(this.communicationFindByPartyAndIdURI);
    }

    @Then("I have a communication find by party id uri")
    public void i_have_a_communication_find_by_party_id_uri() {
        Assertions.assertNotNull(this.communicationFindByPartyURI);
    }

    @Given("a saved communication")
    public void a_saved_communication() throws JsonProcessingException {
        Communication aCommunication = new Communication();
        aCommunication.setId(null);
        aCommunication.setCommunication(java.util.UUID.randomUUID().toString());
        aCommunication.setPartyID(this.person.getId());
        this.communication = aCommunication;
        this.i_ask_the_service_to_save_the_communication();
    }

    @When("I ask the service to find the communication by id")
    public void i_ask_the_service_to_find_the_communication_by_id() throws JsonProcessingException {
        Assertions.assertNotNull(this.communication.getPartyID());
        Assertions.assertNotNull(this.communication.getId());

        this.response = this.client.findCommunicationByPartyAndCommunicationID(this.communication.getPartyID(), this.communication.getId());
        ObjectMapper mapper = new ObjectMapper();
        Communication aCommunication = mapper.readValue((String) this.response.getBody(), Communication.class);
        this.communication = aCommunication;
    }

    @When("I ask the service to find communications associated with the party")
    public void i_ask_the_service_to_find_communications_associated_with_the_party() throws JsonProcessingException {
        Assertions.assertNotNull(this.person.getId());

        this.response = this.client.findCommunicationByPartyID(this.person.getId());

    }

    @Then("{int} communications were found")
    public void communications_were_found(Integer int1) throws JsonProcessingException {
        Assertions.assertNotNull(this.response);
        ObjectMapper mapper = new ObjectMapper();
        String json = (String) this.response.getBody();
        List aList = mapper.readValue(json, List.class);
        Assertions.assertNotNull(aList);
        Assertions.assertEquals(int1, aList.size());
    }


    @Given("a task with a simple status open closed machine")
    public void a_task_with_a_simple_status_open_closed_machine(io.cucumber.datatable.DataTable dataTable) throws JsonProcessingException {
        if (dataTable != null && !dataTable.isEmpty()) {

            List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
            SimpleTask aTask = new SimpleTask();
            for (Map<String, String> columns : rows) {
                aTask.setTitle(columns.get("Title"));
                aTask.setDescription(columns.get("Description"));
                aTask.setCreationDate(new Date());
            }
            Map<String, String[]> progressionConfiguration = new HashMap();
            SimpleStatus start = new SimpleStatus("open");
            SimpleStatus end = new SimpleStatus("closed");
            progressionConfiguration.put(start.getId(), new String[]{end.getId()});
            progressionConfiguration.put(end.getId(), null);


            Map<String, SimpleStatus> state = new HashMap<>();
            state.put(start.getId(), start);
            state.put(end.getId(), end);

            aTask.setStatusStateMachine(new SimpleStatusStateMachine(progressionConfiguration, start.getId(), end.getId(), state));

            this.tasks.clear();
            this.tasks.put(aTask.getId(), aTask);

//
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(new SimpleStatus("abc", "open"));
//            json = mapper.writeValueAsString(new SimpleStatusStateMachine());
//            json = mapper.writeValueAsString(this.task.getStatusStateMachine().getState());
//            json = mapper.writeValueAsString(this.task.getStatusStateMachine().getEndState());
//            json = mapper.writeValueAsString(this.task.getStatusStateMachine().getStartState());
//            json = mapper.writeValueAsString(this.task.getStatusStateMachine().getType());
//            json = mapper.writeValueAsString(this.task.getStatusStateMachine().getProgressionConfiguration());
//            json = mapper.writeValueAsString(this.task.getStatusStateMachine());
//
//            json = mapper.writeValueAsString(this.task);
//            Assertions.assertNotEquals(json, "");


        }
    }

    public List<String> newWithValue(String aValue) {
        List aList = new ArrayList();
        aList.add(aValue);
        return aList;
    }

    private Task getSoleTask() {
        Task aTask = (Task) this.tasks.values().toArray()[0];
        return aTask;
    }

    private Task getTaskLabeledAs(String aValue) {
        Task aTask = (Task) this.tasks.get(aValue);
        return aTask;
    }



    private TaskInProgress getSoleTaskInProgress() {
        TaskInProgress aTip = (TaskInProgress) this.tasksInProgress.get(this.getSoleTask()).get(0);
        return aTip;
    }

    @When("I ask the service to save the task")
    public void i_ask_the_service_to_save_the_task() throws JsonProcessingException {
        Assertions.assertNotNull(this.getSoleTask());
        Assertions.assertNotNull(this.client);
        this.response = this.client.save(this.getSoleTask());
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            SimpleTask aTask = mapper.readValue((String) this.response.getBody(), SimpleTask.class);
            this.tasks.clear();
            this.tasks.put(aTask.getId(), aTask);
        }
    }

    @Then("the task has an ID")
    public void the_task_has_an_id() {
        Assertions.assertNotNull(this.getSoleTask());
        Assertions.assertNotNull(this.getSoleTask().getId());
    }

    @Then("the task is associated with a party")
    public void the_task_is_associated_with_a_party() {
        Assertions.assertNotNull(this.getSoleTask());
        Assertions.assertNotNull(this.getSoleTask().getPartyID());
    }

    @When("I associate the party with the task")
    public void i_associate_the_party_with_the_task() {
        Assertions.assertNotNull(this.getSoleTask());
        Assertions.assertNotNull(this.person);
        Assertions.assertNotNull(this.person.getId());
        SimpleTask aTask = (SimpleTask) this.getSoleTask();
        aTask.setPartyID(this.person.getId());
    }


    @Given("a party task with a simple status open closed machine")
    public void a_party_task_with_a_simple_status_open_closed_machine() {


        SimpleTask aTask = new SimpleTask();
        aTask.setTitle("Title");
        aTask.setDescription("Description");
        aTask.setCreationDate(new Date());

        Map<String, String[]> progressionConfiguration = new HashMap();
        SimpleStatus start = new SimpleStatus("open");
        SimpleStatus end = new SimpleStatus("closed");
        progressionConfiguration.put(start.getId(), new String[]{end.getId()});
        progressionConfiguration.put(end.getId(), null);


        Map<String, SimpleStatus> state = new HashMap<>();
        state.put(start.getId(), start);
        state.put(end.getId(), end);

        aTask.setStatusStateMachine(new SimpleStatusStateMachine(progressionConfiguration, start.getId(), end.getId(), state));

        this.tasks.clear();
        this.tasks.put(aTask.getId(), aTask);


    }

    @When("I start work on the task")
    public void i_start_work_on_the_task() throws JsonProcessingException {
        Task aTask = this.getSoleTask();
        Assertions.assertNotNull(aTask);
        this.response = this.client.startTask(this.getSoleTask().getId());
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            TaskInProgress aTip = mapper.readValue((String) this.response.getBody(), TaskInProgress.class);
            this.tasksInProgress.clear();
            List<TaskInProgress> list = new ArrayList<TaskInProgress>();
            list.add(aTip);
            this.tasksInProgress.put(aTask, list);
        }

    }

    @Then("the task has a task in progress")
    public void the_task_has_a_task_in_progress() {
        Assertions.assertNotNull(this.getSoleTask());
        Assertions.assertNotNull(this.getSoleTaskInProgress());
    }

    @Then("the status of the task in progress is {string}")
    public void the_status_of_the_task_in_progress_is(String string) {
        Assertions.assertNotNull(this.getSoleTask());
        Assertions.assertNotNull(this.getSoleTaskInProgress());
        Assertions.assertEquals(string, this.getSoleTaskInProgress().getStatus().getStatus().getDescription());
    }

    @When("I change the status to the next available status")
    public void i_change_the_status_to_the_next_available_status() throws JsonProcessingException {
        Task aTask = this.getSoleTask();
        Assertions.assertNotNull(aTask);
        Assertions.assertNotNull(this.getSoleTaskInProgress());
        Status[] nextAvailable = this.getSoleTask().getStatusStateMachine().available(this.getSoleTaskInProgress().getStatus().getStatus());
        assertNotNull(nextAvailable);
        Status nextStatus = nextAvailable[0];
        assertNotNull(nextStatus);
        this.response = this.client.changeTaskInProgressStatusTo(this.getSoleTaskInProgress().getID(), nextStatus.getId());
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            TaskInProgress aTip = mapper.readValue((String) this.response.getBody(), TaskInProgress.class);
            this.tasksInProgress.clear();
            List<TaskInProgress> list = new ArrayList<TaskInProgress>();
            list.add(aTip);
            this.tasksInProgress.put(aTask, list);
        }

    }


    @When("I ask the service to get the task by id")
    public void i_ask_the_service_to_get_the_task_by_id() throws JsonProcessingException {
        Assertions.assertNotNull(this.getSoleTask());

        Assertions.assertNotNull(this.client);
        this.response = this.client.findTaskTaskID(this.getSoleTask().getId());
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            SimpleTask aTask = mapper.readValue((String) this.response.getBody(), SimpleTask.class);
            this.tasks.clear();
            this.tasks.put(aTask.getId(), aTask);
        }
    }

    @Then("the task has been found")
    public void the_task_has_been_found() {
        Assertions.assertNotNull(this.getSoleTask());
    }

    @When("I ask the service to get the task by party")
    public void i_ask_the_service_to_get_the_task_by_party() throws JsonProcessingException {
        Assertions.assertNotNull(this.getSoleTask());

        Assertions.assertNotNull(this.client);
        this.response = this.client.findTasksByPartyID(this.getSoleTask().getPartyID());
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            SimpleTask[] tasks = mapper.readValue((String) this.response.getBody(), SimpleTask[].class);
            this.tasks.clear();
            for (int i = 0; i < tasks.length; i++) {
                SimpleTask task = tasks[i];
                this.tasks.put(task.getId(), task);
            }

        }
    }

    @Then("the tasks have been found")
    public void the_tasks_have_been_found() {
        Assertions.assertNotNull(this.tasks);
    }


    @When("I search for tasks in progress associated with the task and party")
    public void i_search_for_tasks_in_progress_associated_with_the_task_and_party() throws JsonProcessingException {
        Task aTask = this.getSoleTask();
        Assertions.assertNotNull(aTask);

        Assertions.assertNotNull(this.client);
        this.response = this.client.findTasksInProgressByTask(this.getSoleTask().getId());
        if (this.response.getStatusCode().is2xxSuccessful() && this.response.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();
            TaskInProgress[] tips = mapper.readValue((String) this.response.getBody(), TaskInProgress[].class);
            this.tasksInProgress.clear();
            List<TaskInProgress> list = new ArrayList<TaskInProgress>();
            this.tasksInProgress.put(aTask, list);
            for (int i = 0; tips != null && i < tips.length; i++) {
                TaskInProgress tip = tips[i];
                list.add(tip);
            }
        }
    }

    @Then("{int} tasks in progress have been found")
    public void tasks_in_progress_have_been_found(Integer int1) {
        if (int1 > 0) {
            Assertions.assertNotNull(this.tasksInProgress);
            Assertions.assertEquals(int1, this.tasksInProgress.size());
        } else {
            Assertions.assertTrue(this.tasksInProgress == null || this.tasksInProgress.size() == 0);
        }
    }


    @Given("a simple start stop task identified as {string}")
    public void a_simple_start_stop_task_identified_as(String string) {

        SimpleTask aTask = new SimpleTask();
        aTask.setTitle("Title");
        aTask.setDescription("Description");
        aTask.setCreationDate(new Date());

        Map<String, String[]> progressionConfiguration = new HashMap();
        SimpleStatus start = new SimpleStatus("start");
        SimpleStatus end = new SimpleStatus("stop");
        progressionConfiguration.put(start.getId(), new String[]{end.getId()});
        progressionConfiguration.put(end.getId(), null);


        Map<String, SimpleStatus> state = new HashMap<>();
        state.put(start.getId(), start);
        state.put(end.getId(), end);

        aTask.setStatusStateMachine(new SimpleStatusStateMachine(progressionConfiguration, start.getId(), end.getId(), state));

        this.tasks.clear();
        this.tasks.put(string, aTask);
    }

    @When("I ask the service to save the task {string}")
    public void i_ask_the_service_to_save_the_task(String string) throws JsonProcessingException {

        Task aTask = this.tasks.get(string);

        Assertions.assertNotNull(aTask);
        Assertions.assertNotNull(this.client);
        this.response = this.client.save(aTask);
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            aTask = mapper.readValue((String) this.response.getBody(), SimpleTask.class);
            this.tasks.put(string, aTask);
        }
    }

    @Then("task {string} has an id")
    public void task_has_an_id(String string) {
        Task aTask = this.tasks.get(string);

        Assertions.assertNotNull(aTask);
        Assertions.assertNotNull(aTask.getId());
    }

    @When("I start work on task {string}")
    public void i_start_work_on_task(String string) throws JsonProcessingException {
        Task aTask = this.tasks.get(string);
        Assertions.assertNotNull(aTask);

        this.response = this.client.startTask(aTask.getId());
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            TaskInProgress aTip = mapper.readValue((String) this.response.getBody(), TaskInProgress.class);
            this.tasksInProgress.clear();
            List<TaskInProgress> list = new ArrayList<TaskInProgress>();
            this.tasksInProgress.put(aTask, list);
            list.add(aTip);
        }
    }

    @Then("task {string} has a task in progress")
    public void task_has_a_task_in_progress(String string) throws JsonProcessingException {
        Task aTask = this.getTaskLabeledAs(string);
        Assertions.assertNotNull(aTask);

        Assertions.assertNotNull(this.client);
        this.response = this.client.findTasksInProgressByTask(aTask.getId());
        if (this.response.getStatusCode().is2xxSuccessful() && this.response.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();
            TaskInProgress[] tips = mapper.readValue((String) this.response.getBody(), TaskInProgress[].class);
            this.tasksInProgress.clear();
            List<TaskInProgress> list = new ArrayList<TaskInProgress>();
            this.tasksInProgress.put(aTask, list);
            for (int i = 0; tips != null && i < tips.length; i++) {
                TaskInProgress tip = tips[i];
                list.add(tip);
            }
        }

        assertNotNull(this.tasksInProgress);
        assertFalse(this.tasksInProgress.isEmpty());



    }

    @When("I change the status of the active task in progress for task {string} to the next available status")
    public void i_change_the_status_of_the_active_task_in_progress_for_task_to_the_next_available_status(String string) throws JsonProcessingException {
        Task aTask = this.tasks.get(string);
        Assertions.assertNotNull(this.getSoleTaskInProgress());
        Status[] nextAvailable = aTask.getStatusStateMachine().available(this.getSoleTaskInProgress().getStatus().getStatus());
        assertNotNull(nextAvailable);
        Status nextStatus = nextAvailable[0];
        assertNotNull(nextStatus);
        this.response = this.client.changeTaskInProgressStatusTo(this.getSoleTaskInProgress().getID(), nextStatus.getId());
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            TaskInProgress aTip = mapper.readValue((String) this.response.getBody(), TaskInProgress.class);
            this.tasksInProgress.clear();
            List<TaskInProgress> list = new ArrayList<TaskInProgress>();
            list.add(aTip);
        }

    }

    @Then("{int} tasks in progress have been found for task: {string}")
    public void tasks_in_progress_have_been_found_for_task(Integer int1, String string) throws JsonProcessingException {
        Task aTask = this.getTaskLabeledAs(string);
        Assertions.assertNotNull(aTask);

        Assertions.assertNotNull(this.client);
        this.response = this.client.findTasksInProgressByTask(aTask.getId());
        if (this.response.getStatusCode().is2xxSuccessful() && this.response.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();
            TaskInProgress[] tips = mapper.readValue((String) this.response.getBody(), TaskInProgress[].class);
            this.tasksInProgress.clear();
            List<TaskInProgress> list = new ArrayList<TaskInProgress>();
            this.tasksInProgress.put(aTask, list);
            for (int i = 0; tips != null && i < tips.length; i++) {
                TaskInProgress tip = tips[i];
                list.add(tip);
            }
        }
    }

    @When("I get the status history for the task in progress for task: {string}")
    public void i_get_the_status_history_for_the_task_in_progress_for_task(String string) throws JsonProcessingException {
        Task aTask = this.getTaskLabeledAs(string);
        Assertions.assertNotNull(aTask);

        TaskInProgress tip = this.getSoleTaskInProgress();
        assertNotNull(tip);

        this.response = this.client.findStatusHistoryForTaskInProgrsss(this.getSoleTaskInProgress().getID());
        if (this.response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            StatusPoint[] points = mapper.readValue((String) this.response.getBody(), StatusPoint[].class);
            this.statusHistory = points;

        }
    }
    @Then("{int} status points have been found in the status history")
    public void status_points_have_been_found_in_the_status_history(Integer int1) {
        assertNotNull(this.statusHistory);
        assertEquals(int1.intValue(), this.statusHistory.length);
    }

    @Then("the status history contains a status of {string}")
    public void the_status_history_contains_a_status_of(String aString) {
        assertNotNull(this.statusHistory);
        boolean found = false;
        for (int i = 0; !found && i < this.statusHistory.length; i++) {
            if (statusHistory[i] != null && statusHistory[i].getStatus().getDescription().equals(aString)) {
                found = true;
            }
        }
        assertTrue(found);
    }



}
