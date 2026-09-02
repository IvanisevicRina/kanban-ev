package hr.tvz.kanban.engine;

import hr.tvz.kanban.model.Player;
import hr.tvz.kanban.model.SandraEvaluation;
import hr.tvz.kanban.model.WeekAction;
import hr.tvz.kanban.reflection.EvaluationRule;

import java.util.List;

public class SandraAI {

    private static final String NO_ID = "";

    @EvaluationRule(description = "Usporedba napretka igraca", condition = "Izvodi se na kraju svakog radnog tjedna")

   public SandraEvaluation evaluatePlayers(List<Player> players, List<WeekAction> weeklyActions, int weekNumber){
       if(players.isEmpty()){
           return new SandraEvaluation(weekNumber, NO_ID, NO_ID, "Sandra nije pronašla igrače za evaluaciju");
       }
       if(players.size()==1) {
           Player player = players.getFirst();
           return new SandraEvaluation(weekNumber, NO_ID, NO_ID, "Sandra je pregledala rad igrala " + player.getName() + "ali nema drugih igrača za usporedbu");
       }
        int highestProgress = players
                .stream()
                .mapToInt(player -> calculateWeeklyProgress(player, weeklyActions))
                .max()
                .orElse(0);

        int lowestProgress = players
                .stream()
                .mapToInt(player -> calculateWeeklyProgress(player, weeklyActions))
                .min()
                .orElse(0);

        List<Player> bestPlayers = players
                .stream()
                .filter(player -> calculateWeeklyProgress(player, weeklyActions) == highestProgress)
                .toList();

        List<Player> weakestPlayers = players
                .stream()
                .filter(player -> calculateWeeklyProgress(player, weeklyActions) == lowestProgress)
                .toList();

        if(highestProgress == lowestProgress){
            return new SandraEvaluation(weekNumber, NO_ID, NO_ID, "Sandra nije dodijelila promociju a ni upozorenje, izjednačeni su");
        }
        String promotedPlayerId=NO_ID;
        String warnedPlayerId = NO_ID;
        String message ="";
        if(bestPlayers.size()==1){
            Player bestPlayer = bestPlayers.getFirst();
            bestPlayer.addPromotion();
            bestPlayer.addScore(2);
            promotedPlayerId = bestPlayer.getId();
            message  = "Sandra je promovirala igrača i dala mu dva boda : " + bestPlayer.getName() + ".\n";
        }else{
            message = "Promocije nema jer su najbolji igrači izjednačeni.\n";
        }
        if(weakestPlayers.size()==1){
            Player weakestPlayer = weakestPlayers.getFirst();
            weakestPlayer.addWarning();
            weakestPlayer.removeScore(1);
            warnedPlayerId = weakestPlayer.getId();
            message = message + weakestPlayer.getName() + "je upozoren i izgubio je 1 bod";
        }else{
            message = message + "Upozorenje nije dodjeljeno jer su najslabiji igrači izjednačeni";
        }



       return new SandraEvaluation(weekNumber, promotedPlayerId, warnedPlayerId, message);



   }

    @EvaluationRule(description = "Izračun tjednog napretka igrača", condition = "Dizajn + komponente + vrijednost auta + testirani automobili * 2")

    private int calculateWeeklyProgress(Player player, List<WeekAction> weeklyActions) {
            return weeklyActions
                    .stream()
                    .filter(action ->
                            action.playerId().equals(
                                    player.getId()
                            )
                    )
                    .mapToInt(WeekAction::performancePoints)
                    .sum();
    }

}
