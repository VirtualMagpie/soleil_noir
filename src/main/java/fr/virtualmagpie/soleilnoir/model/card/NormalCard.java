package fr.virtualmagpie.soleilnoir.model.card;

import fr.virtualmagpie.soleilnoir.model.card.Card;
import fr.virtualmagpie.soleilnoir.model.card.CardSymbol;
import fr.virtualmagpie.soleilnoir.model.card.CardValue;
import lombok.Value;

@Value
public class NormalCard extends Card {
    CardValue value;
    CardSymbol symbol;
}
