package fr.virtualmagpie.soleilnoir.config;

import fr.virtualmagpie.soleilnoir.Main;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import fr.virtualmagpie.soleilnoir.model.combinaison.strategy.AdvantageCombinationStrategy;
import fr.virtualmagpie.soleilnoir.model.combinaison.strategy.CombinationStrategy;
import fr.virtualmagpie.soleilnoir.model.combinaison.strategy.DefaultCombinationStrategy;
import lombok.Getter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Getter
public class StatConfig {

  private static final String CONFIG_FILENAME = "stat-config.properties";

  private final int nbTry;
  private final int nbCardMin;
  private final int nbCardMax;
  private final List<Combination> difficulties;
  private final CombinationStrategy combinationStrategy;

  public StatConfig() {
    Properties properties = new Properties();
    try {
      properties.load(Main.class.getClassLoader().getResourceAsStream(CONFIG_FILENAME));
    } catch (IOException e) {
      e.printStackTrace();
    }

    nbTry = Integer.parseInt(properties.getProperty("nb-try"));
    nbCardMin = Integer.parseInt(properties.getProperty("nb-card-min"));
    nbCardMax = Integer.parseInt(properties.getProperty("nb-card-max"));
    difficulties =
        Arrays.stream(properties.getProperty("difficulties").split(","))
            .map(Combination::fromString)
            .collect(Collectors.toList());
    combinationStrategy =
        combinationStrategyFromProperty(properties.getProperty("combination-strategy"));
  }

  private static CombinationStrategy combinationStrategyFromProperty(String property) {
    switch (property) {
      case "default":
        return new DefaultCombinationStrategy();
      case "advantage":
        return new AdvantageCombinationStrategy();
      default:
        throw new RuntimeException(
            String.format(
                "Unknown property value for combination strategy: %s. Accepted values are: 'default', 'advantage'.",
                property));
    }
  }
}
