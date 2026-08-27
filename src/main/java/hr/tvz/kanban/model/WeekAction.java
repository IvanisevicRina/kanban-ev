package hr.tvz.kanban.model;

import java.io.Serializable;

public record WeekAction(String playerId, DepartmentType departmentType, int weekNumber, String description,  int performancePoints) implements Serializable {
    private static final long serialVersionUID = 1L;


    public WeekAction(String playerId, DepartmentType departmentType, int weekNumber, String description) {
        this(playerId, departmentType, weekNumber, description, 0);
    }
}

