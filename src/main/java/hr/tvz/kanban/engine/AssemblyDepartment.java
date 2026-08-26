package hr.tvz.kanban.engine;

import hr.tvz.kanban.model.*;

public class AssemblyDepartment extends Department{

    private static final int REQUIRED_DESIGN_POINTS=1;
    private static final int REQUIRED_COMPONENTS=1;



    protected AssemblyDepartment() {
        super("Montaža", DepartmentType.ASSEMBLY);
    }

    @Override
    public ActionResult performAction(Player player, GameState gameState) {
        player.setCurrentDepartment(DepartmentType.ASSEMBLY);
        if (player.getDesignPoints() < REQUIRED_DESIGN_POINTS || player.getComponents() <REQUIRED_COMPONENTS){
            String message = player.getName() + " nema dovoljno resursa za montažu";
            return new ActionResult(false, message );
        }
        player.useDesignPoints(REQUIRED_DESIGN_POINTS);
        player.useComponents(REQUIRED_COMPONENTS);

        String carId = player.getId() + "-car-"+(player.getCars().size()+1);
        Car car = new Car(carId, CarModel.CITY);
        car.markAsAssembled();
        player.addCar(car);
        player.addScore(3);

        gameState.addCarInDevelopment(car);
        String message = player.getName()+" sastavio je automobil CITY i dobio 3 boda";
        return new ActionResult(true,message);

    }
}
