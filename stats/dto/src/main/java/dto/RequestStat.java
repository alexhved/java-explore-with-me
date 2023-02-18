package dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RequestStat {

    @NotBlank
    @Size(max = 20)
    private String app;

    @NotBlank
    @Size(max = 50)
    private String uri;

    @NotBlank
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$")
    private String ip;

    @PastOrPresent
    private String timestamp;
}
