package hr.tvz.kanban.engine;

import hr.tvz.kanban.model.*;

public class TestingDepartment extends Department{
    protected TestingDepartment(String name, DepartmentType type) {
        super("Testiranje", DepartmentType.TESTING);
    }

    @Override
    public ActionResult performAction(Player player, GameState gameState) {
        player.setCurrentDepartment(DepartmentType.TESTING);
        for (Car car : player.getCars()){
            if(car.isAssembled() && !car.isTested()){
                car.markAsTested();
                player.addScore(2);

                String message = player.getName() + " je testirao auto " + car.getModel()+ " i dobio 2 boda za to";
                return new ActionResult(true, message);
            }
        }
        String message = player.getName() + " nema automobil za testirat";
        return  new ActionResult(false, message);

    }
}
