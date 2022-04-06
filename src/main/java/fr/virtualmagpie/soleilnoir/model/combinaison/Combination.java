package fr.virtualmagpie.soleilnoir.model.combinaison;

import fr.virtualmagpie.soleilnoir.model.card.CardValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Combination implements Comparable<Combination> {
  int nbCard;
  CardValue cardValue;

  @Override
  public int compareTo(Combination o) {
    // First compare by nb of cards in combination
    if (this.nbCard != o.nbCard) {
      return this.nbCard - o.nbCard;
    }
    // Then compare by card value
    return this.cardValue.compareTo(o.cardValue);
  }

  public String toString() {
    return nbCard + cardValue.toString();
  }

  public static Combination fromString(String value) {
    String[] splited = value.split("-");
    if (splited.length != 2) {
      throw new IllegalArgumentException(
          "String representing a combination should have same format as 2-A");
    }
    int nbCard = Integer.parseInt(splited[0]);
    CardValue cardValue = CardValue.fromString(splited[1]);
    return new Combination(nbCard, cardValue);
  }
}
