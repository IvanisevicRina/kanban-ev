package hr.tvz.kanban.engine;

import hr.tvz.kanban.model.ActionResult;
import hr.tvz.kanban.model.DepartmentType;
import hr.tvz.kanban.model.GameState;
import hr.tvz.kanban.model.Player;

public class LogisticsDepartment extends Department{
    protected LogisticsDepartment() {
        super("Logistika", DepartmentType.LOGISTICS);
    }

    @Override
    public ActionResult performAction(Player player, GameState gameState) {
        player.setCurrentDepartment(DepartmentType.LOGISTICS);
        player.addComponent();
        String message = player.getName() + " je uzeo jednu komponentu";
        return new ActionResult(true, message);
    }
}
