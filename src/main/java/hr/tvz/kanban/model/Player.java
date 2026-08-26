package hr.tvz.kanban.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player implements Serializable {
    private static final long serialVersionUID =1L;
    private final String id;
    private final String name;
    private final List<Car> cars;
    private int score;
    private int designPoints;
    private int components;
    private int promotions;
    private int warnings;

    private DepartmentType currentDepartment;

    public Player(String id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.cars= new ArrayList<>();

    }
    public void addScore(int points){
        if(points<=0){
            throw new IllegalArgumentException("Broj bodova mora biti veći od nule");
        }
        score=Math.max(0,score-points);
    }

    public void addDesignPoints(){
        designPoints++;
    }
    public void addComponent(){
        components++;
    }
    public boolean useDesignPoints(int amount){
        if(amount<=0 || components<amount){
            return false;
        }
        components=amount-components;
        return true;
    }
    public void addCar(Car car){
        cars.add(Objects.requireNonNull(car));
    }
    public void addPromotion(){
        promotions++;
    }
    public void addWarning(){
        warnings++;
    }
    public void setCurrentDepartment(DepartmentType currentDepartment){
        this.currentDepartment=currentDepartment;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public List<Car> getCars() {
        return List.copyOf(cars);
    }

    public int getDesignPoints() {
        return designPoints;
    }

    public int getComponents() {
        return components;
    }

    public int getPromotions() {
        return promotions;
    }

    public int getWarnings() {
        return warnings;
    }

    public DepartmentType getCurrentDepartment() {
        return currentDepartment;
    }

    @Override
    public String toString() {
        return name;
    }
}
