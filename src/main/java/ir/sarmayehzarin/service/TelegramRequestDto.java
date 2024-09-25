package ir.sarmayehzarin.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TelegramRequestDto {
  @JsonProperty("chat_id")
  private Long chatId = -1002362960489L;

  private String text = "https://mahdiyar.me/result.png";

  @JsonProperty("parse_mode")
  private String parseMode = "HTML";
}
