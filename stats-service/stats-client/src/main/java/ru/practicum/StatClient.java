package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.InputStatDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StatClient extends BaseClient {

    @Autowired
    public StatClient(@Value("${stats-server.url}") String urlServer, RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder.uriTemplateHandler(new DefaultUriBuilderFactory(urlServer))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void save(HttpServletRequest request, String app) {

        final InputStatDto inputStatDto = new InputStatDto();
        inputStatDto.setApp(app);
        inputStatDto.setUri(request.getRequestURI());
        inputStatDto.setIp(request.getRemoteAddr());
        inputStatDto.setTimestamp(LocalDateTime.now());
        post("/hit", inputStatDto);
    }

    public ResponseEntity<Object> get(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("/?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}