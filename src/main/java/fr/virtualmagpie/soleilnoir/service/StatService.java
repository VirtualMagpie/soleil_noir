package fr.virtualmagpie.soleilnoir.service;

import fr.virtualmagpie.soleilnoir.model.Deck;
import fr.virtualmagpie.soleilnoir.model.card.Card;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import fr.virtualmagpie.soleilnoir.model.combinaison.CombinationStrategy;
import fr.virtualmagpie.soleilnoir.model.stat.CardDrawStatistics;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StatService {
  public final Deck deck;
  public final Random random;

  public StatService(Random random) {
    this.deck = new Deck();
    this.deck.init();
    this.random = random;
  }

  /**
   * Compute statistics of card draw against given difficulties.
   *
   * @param nbTry - Nb of draw repetition pour each experience nbCard x difficulty. More repetitions
   *     means a more precise result.
   * @param nbCardDrawnMin - For definition of number of cards to draw. This is the min value of the
   *     range of possible values. Must be strictly positive.
   * @param nbCardDrawnMax - For definition of number of cards to draw. This is the max value of the
   *     range of possible values. Must be more than or equal to min value in range.
   * @param difficulties - Array of combinations, defining difficulties against which success rate
   *     will be computed.
   * @return object containing success rate stat for each possibility
   */
  public CardDrawStatistics statsCardDraw(
      int nbTry, int nbCardDrawnMin, int nbCardDrawnMax, Combination[] difficulties) {
    if (nbCardDrawnMax < nbCardDrawnMin) {
      throw new IllegalArgumentException("Wrong range definition of card drawn");
    }

    CardDrawStatistics stats =
        new CardDrawStatistics(
            Arrays.asList(difficulties),
            IntStream.rangeClosed(nbCardDrawnMin, nbCardDrawnMax)
                .boxed()
                .collect(Collectors.toList()));

    for (Combination difficulty : stats.getDifficulties()) {
      for (int nbCards : stats.getCardNumbers()) {
        float successRate = computeStatOfCardDraw(nbTry, nbCards, difficulty);
        stats.addStat(difficulty, nbCards, successRate);
      }
    }
    return stats;
  }

  private float computeStatOfCardDraw(int nbTry, int nbCardDrawn, Combination difficulty) {
    if (nbCardDrawn < 1) {
      throw new IllegalArgumentException("At least one card to draw");
    }

    int nbSuccess = 0;
    for (int i = 0; i < nbTry; i++) {
      deck.shuffle(random);
      Card[] drawn = deck.seeNext(nbCardDrawn);
      Combination combination = CombinationStrategy.findBestCombination(drawn);
      if (combination.compareTo(difficulty) >= 0) {
        nbSuccess++;
      }
    }

    return ((float) nbSuccess) / nbTry;
  }
}
