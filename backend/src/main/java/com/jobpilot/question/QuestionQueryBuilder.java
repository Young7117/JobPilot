package com.jobpilot.question;

import com.jobpilot.battlecard.BattleCard;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class QuestionQueryBuilder {

    public QuestionQuery build(BattleCard battleCard) {
        Set<String> tags = new LinkedHashSet<>();
        addAll(tags, battleCard.getSkillBreakdown());
        addAll(tags, battleCard.getInterviewFocus());
        addAll(tags, battleCard.getWeakPoints());
        String queryText = String.join("\n", tags);
        return new QuestionQuery(queryText, tags);
    }

    private void addAll(Set<String> target, Iterable<String> values) {
        if (values == null) {
            return;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                target.add(value);
            }
        }
    }

    public record QuestionQuery(String queryText, Set<String> tags) {
    }
}
