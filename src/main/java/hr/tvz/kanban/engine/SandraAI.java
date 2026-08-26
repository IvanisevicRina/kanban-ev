package hr.tvz.kanban.engine;

import hr.tvz.kanban.model.Player;
import hr.tvz.kanban.model.SandraEvaluation;
import hr.tvz.kanban.reflection.EvaluationRule;

import java.util.List;

public class SandraAI {

    @EvaluationRule(description = "Usporedba napretka igraca", condition = "Izvodi se na kraju svakog radnog tjedna")

   public SandraEvaluation evaluatePlayers(List<Player> players,int weekNumber){
       if(players.isEmpty()){
           return new SandraEvaluation(weekNumber, null, null, "Sandra nije pronašla igrače za evaluaciju");
       }
       if(players.size()==1) {
           Player player = players.getFirst();
           return new SandraEvaluation(weekNumber, null, null, "Sandra je pregledala rad igrala " + player.getName() + "ali nema drugih igrača za usporedbu");
       }
       int highestProgress = players.stream().mapToInt(this::calculateProgress).max().orElse(0);
       int lowestProgress = players.stream().mapToInt(this::calculateProgress).min().orElse(0);

       List<Player> bestPlayers = players.stream().filter(player->calculateProgress(player)==highestProgress).toList();
       List<Player> weakestPlayers = players.stream().filter(player -> calculateProgress(player)==lowestProgress).toList();



       if (highestProgress == lowestProgress || bestPlayers.size()>1 || weakestPlayers.size()>1){
           return new SandraEvaluation(weekNumber, null, null, "Sandra nije dodjelila promociju ili upozorenje jer su igrači izjednačeni");

       }
       Player bestPlayer = bestPlayers.getFirst();
       Player weakestPlayer = weakestPlayers.getFirst();

       bestPlayer.addPromotion();
       bestPlayer.addScore(2);

       weakestPlayer.addWarning();
       weakestPlayer.removeScore(1);
       String message = "Sandra je promovirala igrača " + bestPlayer.getName() + "i dodjelila mu 2 boda." + "Igrač " + weakestPlayer.getName() + " je dobio upozorenje i izgubio 1 bod";

       return new SandraEvaluation(weekNumber, bestPlayer.getId(), weakestPlayer.getId(), message);



   }
   @EvaluationRule(description = "Izračun napretka igrača", condition = "Dizajn + komponente + automobil*3 + testirani automobili * 2")

   private int calculateProgress(Player player){
       long testedCars = player.getCars().stream().filter(car->car.isTested()).count();
       return player.getDesignPoints() + player.getComponents() + player.getCars().size()*3 +(int) testedCars*2;
   }
}
