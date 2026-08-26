package hr.tvz.kanban.model;

import java.io.Serializable;

public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final CarModel model;
    private int level;
    private boolean assembled;
    private boolean tested;

    public Car(String id, CarModel model) {
        this.id = id;
        this.model = model;
        this.level = 1;
    }
    public void markAsAssembled() {
        assembled=true;
    }
    public void markAsTested(){
        if(!assembled){
            throw new IllegalStateException("Automobil se ne može testirat prije montaže");
        }
        if(!tested){
            tested=true;
            increaseLevel();
        }
    }
    public void increaseLevel(){
        level++;
    }

    public String getId() {
        return id;
    }

    public CarModel getModel() {
        return model;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isAssembled() {
        return assembled;
    }

    public void setAssembled(boolean assembled) {
        this.assembled = assembled;
    }

    public boolean isTested() {
        return tested;
    }

    public void setTested(boolean tested) {
        this.tested = tested;
    }
}
