package hr.tvz.kanban.engine;

import hr.tvz.kanban.model.ActionResult;
import hr.tvz.kanban.model.DepartmentType;
import hr.tvz.kanban.model.GameState;
import hr.tvz.kanban.model.Player;


public class DesignDepartment extends Department{
    protected DesignDepartment() {
        super("Dizajn", DepartmentType.DESIGN);
    }

    @Override
    public ActionResult performAction(Player player, GameState gameState) {
       player.setCurrentDepartment(DepartmentType.DESIGN);
       player.addDesignPoints();

       String message = player.getName() + " je dobio dizajnersko znanje";
       return new ActionResult(true, message);
    }
}
