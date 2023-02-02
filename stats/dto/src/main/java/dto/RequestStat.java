package dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RequestStat {
    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
