package hr.tvz.kanban.model;

import java.io.Serializable;

public record SandraEvaluation(int weekNumber, String promotedPlayerId, String warnedPlayerId, String message) implements Serializable {
    private static final long serialVersionUID = 1L;






}

