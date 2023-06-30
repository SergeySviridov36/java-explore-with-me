package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.InputStatDto;
import ru.practicum.dto.OutputStatDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {

    private static final String SERVICE_NAME = "ewm-main-service";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatClient(@Value("${stats-server.url}") String urlServer, RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder.uriTemplateHandler(new DefaultUriBuilderFactory(urlServer))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void save(HttpServletRequest request) {

        final InputStatDto inputStatDto = new InputStatDto();
        inputStatDto.setApp(SERVICE_NAME);
        inputStatDto.setUri(request.getRequestURI());
        inputStatDto.setIp(request.getRemoteAddr());
        inputStatDto.setTimestamp(LocalDateTime.now());
        post("/hit", inputStatDto);
    }

    public List<OutputStatDto> get(LocalDateTime startIn, LocalDateTime endIn, List<String> uri, Boolean unique) {
        String uris = String.join(",", uri);
        String start = startIn.format(TIME_FORMATTER);
        ;
        String end = endIn.format(TIME_FORMATTER);
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}