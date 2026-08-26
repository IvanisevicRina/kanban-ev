package hr.tvz.kanban.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameState implements Serializable {
    private static final long serialVersionUID=1L;
    public static final int MAX_WEEKS=5;
    private final List<Player> players;
    private final List<Car> carsInDevelopment;
    private int currentWeek;
    private boolean gameFinished;
    private String lastSandraMessage;

    public GameState(){
        players=new ArrayList<>();
        carsInDevelopment=new ArrayList<>();
        currentWeek=1;
        lastSandraMessage="";
    }
    public void addPlayer(Player player){
        players.add(Objects.requireNonNull(player));
    }
    public void addCarInDevelopment(Car car){
        carsInDevelopment.add(Objects.requireNonNull(car));
    }
    public void nextWeek(){
        if(currentWeek < MAX_WEEKS){
            currentWeek++;
        }else{
            finishGame();
        }
    }

    public void finishGame(){
        gameFinished=true;
    }
    public void setLastSandraMessage(String message){
        lastSandraMessage=Objects.requireNonNull(message);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Car> getCarsInDevelopment() {
        return carsInDevelopment;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public String getLastSandraMessage() {
        return lastSandraMessage;
    }
}
