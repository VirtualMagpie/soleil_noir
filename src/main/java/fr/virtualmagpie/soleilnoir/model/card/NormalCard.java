package fr.virtualmagpie.soleilnoir.model.card;

import lombok.Value;

@Value
public class NormalCard extends Card {
  CardValue value;
  CardSymbol symbol;
}
