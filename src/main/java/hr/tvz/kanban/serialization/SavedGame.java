package hr.tvz.kanban.serialization;

import hr.tvz.kanban.model.GameState;
import hr.tvz.kanban.model.SandraEvaluation;
import hr.tvz.kanban.model.WeekAction;

import java.io.Serializable;
import java.util.*;

public record SavedGame(GameState gameState, List<WeekAction> actionHistory, List<SandraEvaluation> evaluationHistory, Set<String> playersWhoActed) implements Serializable {

    private static final long serialVersionUID=1L;


    public SavedGame{
        Objects.requireNonNull(gameState);
        Objects.requireNonNull(actionHistory);
        Objects.requireNonNull(evaluationHistory);
        Objects.requireNonNull(playersWhoActed);

        actionHistory = new ArrayList<>(actionHistory);

        evaluationHistory = new ArrayList<>(evaluationHistory);

        playersWhoActed = new HashSet<>(playersWhoActed);



    }
}
