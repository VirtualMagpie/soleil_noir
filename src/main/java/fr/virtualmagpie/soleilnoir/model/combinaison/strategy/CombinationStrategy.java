package fr.virtualmagpie.soleilnoir.model.combinaison.strategy;

import fr.virtualmagpie.soleilnoir.model.card.Card;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;

public interface CombinationStrategy {
  Combination findBestCombination(Card[] cards);
}
