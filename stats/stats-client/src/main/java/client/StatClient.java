package client;

import dto.RequestStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class StatClient extends BaseClient {

    private static final String API_PREFIX_HIT = "/hit";

    @Autowired
    public StatClient(String url, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public void save(RequestStat stat) {
        post(API_PREFIX_HIT, stat);
    }

}
