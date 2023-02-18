package client;

import dto.RequestStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class StatClient extends BaseClient {

    private static final String API_PREFIX_HIT = "/hit";
    private static final String API_PREFIX_STATS = "/stats";

    @Autowired
    public StatClient(String url, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void save(RequestStat stat) {
        post(API_PREFIX_HIT, stat);
    }

    public ResponseEntity<Object> find(String start, String end, Boolean unique, String uris) {
        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "unique", unique,
                "uris", uris
        );
        return get(API_PREFIX_STATS + "?start={start}&end={end}&unique={unique}&uris={uris}", params);
    }

}
