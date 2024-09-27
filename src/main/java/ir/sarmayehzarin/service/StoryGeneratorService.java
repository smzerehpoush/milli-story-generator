package ir.sarmayehzarin.service;

import com.ibm.icu.util.ULocale;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoryGeneratorService {
  private static final ULocale PERSIAN_LOCALE = new ULocale("fa@calendar=persian");
  private final RestTemplate restTemplate = new RestTemplate();

  private static void drawText(Graphics2D g, Font font, String text, Color color, int x, int y) {
    g.setFont(font);
    AttributedString attributedText = new AttributedString(text);
    attributedText.addAttribute(TextAttribute.FONT, g.getFont());
    attributedText.addAttribute(TextAttribute.RUN_DIRECTION, TextAttribute.RUN_DIRECTION_RTL);
    AttributedCharacterIterator characterIterator = attributedText.getIterator();
    FontRenderContext frc = g.getFontRenderContext();
    TextLayout textLayout = new TextLayout(characterIterator, frc);
    g.setColor(color);
    textLayout.draw(g, x, y);
  }

  public static String convertToPersianNumber(Long numberValue) {
    String number = String.valueOf(numberValue);
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < number.length(); i++) {
      char token = number.charAt(i);
      char digit =
          switch (token) {
            case '0' -> '۰';
            case '1' -> '۱';
            case '2' -> '۲';
            case '3' -> '۳';
            case '4' -> '۴';
            case '5' -> '۵';
            case '6' -> '۶';
            case '7' -> '۷';
            case '8' -> '۸';
            case '9' -> '۹';
            default -> token;
          };
      result.append(digit);
    }
    return result.toString();
  }

  private static String convertToPersianDate(LocalDate localDate, String dateFormat) {
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    com.ibm.icu.text.SimpleDateFormat formatter =
        new com.ibm.icu.text.SimpleDateFormat(dateFormat, PERSIAN_LOCALE);
    return formatter.format(date);
  }

  public void generateStory(int hour) throws Exception {
    MilliPriceResponseDto responseDto =
        restTemplate.getForObject(
            "https://milli.gold/api/v1/public/milli-price/detail", MilliPriceResponseDto.class);
    if (responseDto == null) throw new IllegalStateException("error in getting milli price");
    log.info("milli response {}", responseDto);

    InputStream fontInputStream =
        Objects.requireNonNull(
            StoryGeneratorService.class.getResourceAsStream("/YekanBakhFaNum-SemiBold.ttf"));

    BufferedImage backgroundImage =
        ImageIO.read(
            Objects.requireNonNull(getClass().getResource(String.format("/%s.png", hour))));
    if (backgroundImage == null) {
      log.error("Failed to load the background image. Ensure it's a valid image file.");
      return;
    }

    BufferedImage combinedImage =
        new BufferedImage(
            backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

    Graphics2D g = combinedImage.createGraphics();

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHint(
        RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.drawImage(backgroundImage, 0, 0, null);
    Font dateFont = Font.createFont(Font.TRUETYPE_FONT, fontInputStream).deriveFont(36f);
    Font priceFont = Font.createFont(Font.TRUETYPE_FONT, fontInputStream).deriveFont(102f);
    Font rialFont = Font.createFont(Font.TRUETYPE_FONT, fontInputStream).deriveFont(80f);
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    ge.registerFont(dateFont);
    ge.registerFont(priceFont);
    ge.registerFont(rialFont);

    String date = convertToPersianDate(LocalDate.now(ZoneId.of("Asia/Tehran")), "yyyy/MM/dd");
    drawText(g, dateFont, date, Color.WHITE, 652, 645);

    String price = convertToPersianNumber(responseDto.getPrice18() * 1000);
    drawText(g, priceFont, price, new Color(5, 16, 97), 350, 850);

    String rial = "ریال";
    drawText(g, rialFont, rial, new Color(61, 64, 74), 200, 850);
    g.dispose();

    ImageIO.write(combinedImage, "png", new File("/var/www/html/result.png"));
    String response =
        restTemplate.postForObject(
            "https://api.telegram.org/bot7759907035:AAGht_v717Q6II3NsEgmQ5sLB2zBp_8IkOk/sendMessage",
            new TelegramRequestDto(),
            String.class);
    log.info("telegram response {}", response);
  }
}
