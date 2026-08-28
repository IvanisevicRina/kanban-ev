package hr.tvz.kanban.engine;

import hr.tvz.kanban.model.*;
import hr.tvz.kanban.serialization.SavedGame;

import java.util.*;

public class KanbanEngine {

    private final GameState gameState;
    private final Map<DepartmentType, Department> departments;
    private final Set<String> playerWhoActed;
    private final List<WeekAction> actionHistory;
    private final SandraAI sandraAI;
    private final List<SandraEvaluation> evaluationHistory;

    public KanbanEngine(GameState gameState) {
        this.gameState = gameState;
        this.departments = new EnumMap<>(DepartmentType.class);
        this.playerWhoActed = new HashSet<>();
        this.actionHistory = new ArrayList<>();
        this.sandraAI = new SandraAI();
        this.evaluationHistory = new ArrayList<>();
        initializeDepartments();
    }

    private void initializeDepartments() {
        departments.put(DepartmentType.DESIGN, new DesignDepartment());
        departments.put(DepartmentType.LOGISTICS, new LogisticsDepartment());
        departments.put(DepartmentType.ASSEMBLY, new AssemblyDepartment());
        departments.put(DepartmentType.TESTING, new TestingDepartment());
    }

    public ActionResult performAction(String playerID, DepartmentType departmentType){
        if(gameState.isGameFinished()){
            return new ActionResult(false, "Igra je već završena");
        }
        Player player = findPlayer(playerID);

        if (player == null){
            return new ActionResult(false, "Igrač nije pronađen");
        }

        if (playerWhoActed.contains(playerID)){
            return new ActionResult(false, "Igrač je već završio potez u ovom tjednu");
        }

        Department department = departments.get(departmentType);
        ActionResult result = department.performAction(player, gameState);

        if (result.successful()){
            saveAction(player, departmentType, result.message());
            playerWhoActed.add(playerID);
            if(haveAllPlayersActed()){
                completeWeek();
            }
        }
        return result;

    }

    private void completeWeek() {
        int completedWeek = gameState.getCurrentWeek();

        List<WeekAction> weeklyActions = actionHistory.stream()
                        .filter(action->action.weekNumber() == completedWeek).toList();

        SandraEvaluation evaluation = sandraAI.evaluatePlayers(gameState.getPlayers(), weeklyActions,completedWeek);

        evaluationHistory.add(evaluation);
        gameState.setLastSandraMessage(evaluation.message());
        gameState.nextWeek();
        playerWhoActed.clear();
    }

    private boolean haveAllPlayersActed() {
        return !gameState.getPlayers().isEmpty() && playerWhoActed.size()==gameState.getPlayers().size();
    }

    private void saveAction(Player player, DepartmentType departmentType, String description) {

        int performancePoints = calculateActionPoint(player, departmentType);
        WeekAction action = new WeekAction(player.getId(), departmentType, gameState.getCurrentWeek(), description, performancePoints);
        actionHistory.add(action);
    }

    public int calculateActionPoint(Player player, DepartmentType departmentType){
        return switch (departmentType){
            case DESIGN,LOGISTICS -> 1;
            case ASSEMBLY -> player.getSelectedCarModel().getAssemblyPoints();
            case TESTING -> 2;
        };
    }

    private Player findPlayer(String playerID) {
        return gameState.getPlayers().stream().filter(player -> player.getId().equals(playerID)).findFirst().orElse(null);
    }

    public List<SandraEvaluation> getEvaluationHistory() {
        return List.copyOf(evaluationHistory);
    }

    public List<WeekAction> getActionHistory() {
        return List.copyOf(actionHistory);
    }

    public GameState getGameState() {
        return gameState;
    }

    public SavedGame createSavedGame() {
        return new SavedGame(gameState,actionHistory,evaluationHistory,playerWhoActed);
    }


    public static KanbanEngine restore(SavedGame savedGame){
        Objects.requireNonNull(savedGame);
        KanbanEngine restoredEngine = new KanbanEngine(savedGame.gameState());

        restoredEngine.actionHistory.addAll(savedGame.actionHistory());

        restoredEngine.evaluationHistory.addAll(savedGame.evaluationHistory());

        restoredEngine.playerWhoActed.addAll(savedGame.playersWhoActed());

        return restoredEngine;
    }



}
