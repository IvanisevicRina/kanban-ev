package hr.tvz.kanban.model;

public enum CarModel {
    CITY("City", 1, 1, 3),
    SUV("SUV", 2, 2, 5),
    SPORT("Sport", 3, 2, 7);

    private final String displayName;
    private final int requiredDesignPoints;
    private final int requiredComponents;
    private final int assemblyPoints;


    CarModel(String displayName, int requiredDesignPoints, int requiredComponents, int assemblyPoints) {


        this.displayName = displayName;
        this.requiredDesignPoints = requiredDesignPoints;
        this.requiredComponents = requiredComponents;
        this.assemblyPoints = assemblyPoints;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getRequiredDesignPoints() {
        return requiredDesignPoints;
    }

    public int getRequiredComponents() {
        return requiredComponents;
    }

    public int getAssemblyPoints() {
        return assemblyPoints;
    }

    @Override
    public String toString() {
        return "CarModel{" +
                "displayName='" + displayName + '\'' +
                '}';
    }
}
