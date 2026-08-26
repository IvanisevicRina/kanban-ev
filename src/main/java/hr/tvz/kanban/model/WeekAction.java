package hr.tvz.kanban.model;

import java.io.Serializable;

public record WeekAction(String playerId, DepartmentType departmentType, int weekNumber, String description) implements Serializable {
    private static final long serialVersionUID = 1L;
}
