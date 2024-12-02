package com.burskey.dailylife.api;

import com.burskey.dailylife.party.domain.Communication;
import com.burskey.dailylife.party.domain.Party;
import com.burskey.dailylife.task.domain.SimpleTask;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class PartyClient {


    private RestClient findByIDClient;
    private RestClient saveClient;
    private RestClient communicationSaveClient;
    private RestClient communicationFindByPartyAndIdClient;
    private RestClient communicationFindByPartyClient;
    private RestClient taskSaveClient;
    private RestClient taskFindByPartyAndIdClient;
    private RestClient taskFindByPartyIdClient;

    public static PartyClient Builder() {
        return new PartyClient();
    }


    public PartyClient withSave(String aURI) {
        RestClient restClient = RestClient.create(aURI);
        this.saveClient = restClient;
        return this;
    }


    public PartyClient withCommunicationSave(String aURI) {
        RestClient restClient = RestClient.create(aURI);
        this.communicationSaveClient = restClient;
        return this;
    }

    public PartyClient withTaskSave(String aURI) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        RestClient restClient = RestClient.builder().messageConverters(httpMessageConverters -> new MappingJackson2HttpMessageConverter(mapper)).baseUrl(aURI).build();

//        RestClient restClient = RestClient.create(aURI);
        this.taskSaveClient = restClient;
        return this;
    }



    public PartyClient withFindByID(String aURI) {

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(aURI);

        RestClient restClient = RestClient.builder().uriBuilderFactory(factory).build();
        this.findByIDClient = restClient;


        return this;
    }
    public PartyClient withTaskFindByPartyAndId(String aURI) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(aURI);

        RestClient restClient = RestClient.builder().uriBuilderFactory(factory).build();
        this.taskFindByPartyAndIdClient = restClient;
        return this;
    }

    public PartyClient withTaskFindByParty(String taskFindByPartyURI) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(taskFindByPartyURI);

        RestClient restClient = RestClient.builder().uriBuilderFactory(factory).build();
        this.taskFindByPartyIdClient = restClient;
        return this;
    }


    public PartyClient withCommunicationFindByPartyAndId(String aURI) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(aURI);

        RestClient restClient = RestClient.builder().uriBuilderFactory(factory).build();
        this.communicationFindByPartyAndIdClient = restClient;


        return this;
    }

    public PartyClient withCommunicationFindByParty(String aURI) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(aURI);

        RestClient restClient = RestClient.builder().uriBuilderFactory(factory).build();
        this.communicationFindByPartyClient = restClient;


        return this;
    }
    public ResponseEntity save(Party party) {

        ResponseEntity<String> response = null;
        try {
            response = this.saveClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(party)
                    .retrieve()
                    .toEntity(String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Success
                String body = response.getBody();

            } else {
                // Handle error
            }

        } catch (HttpClientErrorException ex) {
            // Handle client errors (4xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (HttpServerErrorException ex) {
            // Handle server errors (5xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
        return response;

    }

    public ResponseEntity findPartyByID(String id) {

        ResponseEntity<String> response = null;
        try {
            response = this.findByIDClient
                    .get()
                    .uri("{id}", id)
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpClientErrorException ex) {
            // Handle client errors (4xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (HttpServerErrorException ex) {
            // Handle server errors (5xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
        return response;
    }

    public ResponseEntity save(Communication communication) {

        ResponseEntity<String> response = null;
        try {
            response = this.communicationSaveClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(communication)
                    .retrieve()
                    .toEntity(String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Success
                String body = response.getBody();

            } else {
                // Handle error
            }

        } catch (HttpClientErrorException ex) {
            // Handle client errors (4xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (HttpServerErrorException ex) {
            // Handle server errors (5xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
        return response;
    }



    public ResponseEntity findCommunicationByPartyAndCommunicationID(String partyID, String communicationID) {

        ResponseEntity<String> response = null;
        try {
            response = this.communicationFindByPartyAndIdClient
                    .get()
                    .uri("{partyID}/{communicationID}", partyID, communicationID)
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpClientErrorException ex) {
            // Handle client errors (4xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (HttpServerErrorException ex) {
            // Handle server errors (5xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
        return response;
    }

    public ResponseEntity findCommunicationByPartyID(String partyID) {

        ResponseEntity<String> response = null;
        try {
            response = this.communicationFindByPartyClient
                    .get()
                    .uri("{partyID}", partyID)
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpClientErrorException ex) {
            // Handle client errors (4xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (HttpServerErrorException ex) {
            // Handle server errors (5xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
        return response;
    }

    public ResponseEntity save(SimpleTask task) {
        ResponseEntity<String> response = null;
        try {
            response = this.taskSaveClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(task)
                    .retrieve()
                    .toEntity(String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Success
                String body = response.getBody();

            } else {
                // Handle error
            }

        } catch (HttpClientErrorException ex) {
            // Handle client errors (4xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (HttpServerErrorException ex) {
            // Handle server errors (5xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
        return response;
    }



    public ResponseEntity findTasksByPartyID(String partyID) {

        ResponseEntity<String> response = null;
        try {
            response = this.taskFindByPartyIdClient
                    .get()
                    .uri("{partyID}", partyID)
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpClientErrorException ex) {
            // Handle client errors (4xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (HttpServerErrorException ex) {
            // Handle server errors (5xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
        return response;
    }
    public ResponseEntity findTaskByPartyAndTaskID(String partyID, String taskID) {

        ResponseEntity<String> response = null;
        try {
            response = this.taskFindByPartyAndIdClient
                    .get()
                    .uri("{partyID}/{taskID}", partyID, taskID)
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpClientErrorException ex) {
            // Handle client errors (4xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (HttpServerErrorException ex) {
            // Handle server errors (5xx)
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
        return response;
    }



}
