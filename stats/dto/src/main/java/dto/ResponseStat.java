package dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ResponseStat {
    private String app;

    private String uri;

    private int hits;
}
