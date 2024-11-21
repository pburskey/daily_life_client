package com.burskey.dailylife.api;

import com.burskey.dailylife.party.domain.Communication;
import com.burskey.dailylife.party.domain.Party;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class PartyClient {


    private RestClient findByIDClient;
    private RestClient saveClient;
    private RestClient communicationSaveClient;
    private RestClient communicationFindByPartyAndIdClient;


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



    public PartyClient withFindByID(String aURI) {

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(aURI);

        RestClient restClient = RestClient.builder().uriBuilderFactory(factory).build();
        this.findByIDClient = restClient;


        return this;
    }

    public PartyClient withCommunicationFindByPartyAndId(String aURI) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(aURI);

        RestClient restClient = RestClient.builder().uriBuilderFactory(factory).build();
        this.communicationFindByPartyAndIdClient = restClient;


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


}
