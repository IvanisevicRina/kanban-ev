package hr.tvz.kanban.engine;

import hr.tvz.kanban.model.ActionResult;
import hr.tvz.kanban.model.DepartmentType;
import hr.tvz.kanban.model.GameState;
import hr.tvz.kanban.model.Player;


public abstract class Department {
    private final String name;
    private final DepartmentType type;

    protected Department(String name, DepartmentType type) {
        this.name = name;
        this.type = type;
    }

    public abstract ActionResult performAction(Player player, GameState gameState);

    public String getName() {
        return name;
    }

    public DepartmentType getType() {
        return type;
    }
}
