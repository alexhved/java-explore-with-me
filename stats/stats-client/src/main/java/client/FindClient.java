package client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class FindClient extends BaseClient {

    private static final String API_PREFIX = "/stats";

    @Autowired
    public FindClient(@Value("&{server-url}") String url, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(url + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> find(String start, String end, Boolean unique, String uris) {
        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "unique", unique,
                "uris", uris
        );
        return get("?start={start}&end={end}&unique={unique}&uris={uris}", params);
    }
}
