package fr.virtualmagpie.soleilnoir.model.stat;

import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This object hold relevant data to represent a stat results of a draw. */
public class CardDrawStatistics {
  private final Map<Combination, Integer> difficultiesIndexes;
  private final Map<Integer, Integer> cardNumbersIndexes;

  private final float[][] stats;

  public CardDrawStatistics(List<Combination> difficulties, List<Integer> cardNumbers) {
    this.difficultiesIndexes = buildIndexMap(difficulties);
    this.cardNumbersIndexes = buildIndexMap(cardNumbers);
    this.stats = new float[difficultiesIndexes.size()][cardNumbersIndexes.size()];
  }

  public List<Combination> getDifficulties() {
    return difficultiesIndexes.keySet().stream().sorted().toList();
  }

  public List<Integer> getCardNumbers() {
    return cardNumbersIndexes.keySet().stream().sorted().toList();
  }

  public void addStat(Combination difficulty, int nbCards, float stat) {
    int indexDifficulty = difficultiesIndexes.get(difficulty);
    int indexNbCards = cardNumbersIndexes.get(nbCards);
    stats[indexDifficulty][indexNbCards] = stat;
  }

  public float getStat(Combination difficulty, int nbCards) {
    int indexDifficulty = difficultiesIndexes.get(difficulty);
    int indexNbCards = cardNumbersIndexes.get(nbCards);
    return stats[indexDifficulty][indexNbCards];
  }

  private static <U> Map<U, Integer> buildIndexMap(List<U> values) {
    int index = 0;
    Map<U, Integer> mapToBuild = new HashMap<>();
    for (U value : values) {
      if (!mapToBuild.containsKey(value)) {
        mapToBuild.put(value, index++);
      }
    }
    return mapToBuild;
  }
}
