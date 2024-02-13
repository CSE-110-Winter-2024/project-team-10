package edu.ucsd.cse110.successorator.lib.domain;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
public class Tasks {

    public static List<Task> rotate(List<Task> cards, int k) {
        var newCards = new ArrayList<Task>();
        for (int i = 0; i < cards.size(); i++) {
            var thisCard = cards.get(i);
            var thatCard = cards.get(Math.floorMod(i + k, cards.size()));
            newCards.add(thisCard.withSortOrder(thatCard.sortOrder()));
        }
        return newCards;
    }
}
