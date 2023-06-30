package ru.practicum;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.OutputStatDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class BaseClient {

    protected final RestTemplate restTemplate;

    public BaseClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    protected <T> List<OutputStatDto> post(String path, T body) {
        return post(path, null, body);
    }

    protected <T> List<OutputStatDto> post(String path, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body);
    }

    protected <T> List<OutputStatDto> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    private <T> List<OutputStatDto> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters,
                                                       @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<List<OutputStatDto>> ewmServerResponse;
        try {
            if (parameters != null) {
                ewmServerResponse = restTemplate.exchange(path, method, requestEntity, new ParameterizedTypeReference<>() {
                }, parameters);
            } else {
                ewmServerResponse = restTemplate.exchange(path, method, requestEntity, new ParameterizedTypeReference<>() {
                });
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return prepareStatsResponse(ewmServerResponse);
    }

    private static List<OutputStatDto> prepareStatsResponse(ResponseEntity<List<OutputStatDto>> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody()).getBody();
        }

        return Collections.emptyList();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}