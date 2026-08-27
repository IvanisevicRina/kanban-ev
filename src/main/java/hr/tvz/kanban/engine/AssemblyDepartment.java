package hr.tvz.kanban.engine;

import hr.tvz.kanban.model.*;

public class AssemblyDepartment extends Department{





    protected AssemblyDepartment() {
        super("Montaža", DepartmentType.ASSEMBLY);
    }





    @Override
    public ActionResult performAction(Player player, GameState gameState) {
        CarModel carModel=player.getSelectedCarModel();

        int requiredDesignPoints=carModel.getRequiredDesignPoints();
        int requiredComponents=carModel.getRequiredComponents();
        int assemblyPoints= carModel.getAssemblyPoints();



        if (player.getDesignPoints() < requiredDesignPoints || player.getComponents() <requiredComponents){
            String message = player.getName() + " nema dovoljno resursa za montažu";
            return new ActionResult(false, message );
        }
        player.setCurrentDepartment(DepartmentType.ASSEMBLY);
        player.useDesignPoints(requiredDesignPoints);
        player.useComponents(requiredComponents);

        String carId = player.getId() + "-car-"+(player.getCars().size()+1);
        Car car = new Car(carId, carModel);
        car.markAsAssembled();
        player.addCar(car);
        player.addScore(assemblyPoints);

        gameState.addCarInDevelopment(car);
        String message = player.getName()+" sastavio je automobil"+carModel.getDisplayName()+" i dobio 3 boda";
        return new ActionResult(true,message);
    }


}
