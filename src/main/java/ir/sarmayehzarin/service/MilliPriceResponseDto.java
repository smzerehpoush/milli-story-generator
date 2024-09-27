package ir.sarmayehzarin.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MilliPriceResponseDto {
    @JsonProperty(required = true)
    private long price18;

    @JsonProperty(required = true)
    private LocalDateTime date;
}
