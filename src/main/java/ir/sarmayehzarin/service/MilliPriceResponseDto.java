package ir.sarmayehzarin.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MilliPriceResponseDto {
    @JsonProperty(required = true)
    private long price18;

    @JsonProperty(required = true)
    private LocalDateTime date;
}
