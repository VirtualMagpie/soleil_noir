package fr.virtualmagpie.soleilnoir.config;

import fr.virtualmagpie.soleilnoir.Main;
import fr.virtualmagpie.soleilnoir.model.card.CardSymbol;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import fr.virtualmagpie.soleilnoir.model.combinaison.strategy.*;
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
  private final String combinationStrategyName;
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
    combinationStrategyName = properties.getProperty("combination-strategy");
    combinationStrategy = combinationStrategyFromProperty(combinationStrategyName);
  }

  private static CombinationStrategy combinationStrategyFromProperty(String property) {
    switch (property) {
      case "default":
        return new DefaultCombinationStrategy();
      case "advantage":
        return new AdvantageCombinationStrategy(CardSymbol.CLUB);
      case "advantage-limited-joker-before":
        return new AdvantageLimitedCombinationStrategy(CardSymbol.CLUB, true);
      case "advantage-limited-joker-after":
        return new AdvantageLimitedCombinationStrategy(CardSymbol.CLUB, false);
      case "disadvantage":
        return new DisadvantageCombinationStrategy(CardSymbol.CLUB);
      default:
        throw new RuntimeException(
            String.format(
                "Unknown property value for combination strategy: %s. Accepted values are: 'default', 'advantage', 'advantage-limited-joker-before', 'advantage-limited-joker-after', 'disadvantage'.",
                property));
    }
  }
}
