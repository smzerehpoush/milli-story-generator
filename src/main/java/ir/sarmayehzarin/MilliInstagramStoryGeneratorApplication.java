package ir.sarmayehzarin;

import ir.sarmayehzarin.service.StoryGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "ir.sarmayehzarin**")
@EnableScheduling
public class MilliInstagramStoryGeneratorApplication implements CommandLineRunner {

  @Autowired StoryGeneratorService storyGeneratorService;

  public static void main(String[] args) {
    SpringApplication.run(MilliInstagramStoryGeneratorApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    storyGeneratorService.generateStory(11);
  }
}
