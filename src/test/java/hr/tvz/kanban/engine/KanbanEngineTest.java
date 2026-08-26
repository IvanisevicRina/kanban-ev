package hr.tvz.kanban.engine;

import hr.tvz.kanban.model.ActionResult;
import hr.tvz.kanban.model.DepartmentType;
import hr.tvz.kanban.model.GameState;
import hr.tvz.kanban.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KanbanEngineTest {

    private Player firstPlayer;
    private Player secondPlayer;
    private GameState gameState;
    private KanbanEngine engine;

    @BeforeEach
    void setUp() {
        firstPlayer = new Player("player-1", "Rina");
        secondPlayer = new Player("player-2", "Frane");
        gameState = new GameState();
        gameState.addPlayer(firstPlayer);
        gameState.addPlayer(secondPlayer);

        engine = new KanbanEngine(gameState);
    }

    @Test
    void succesfulActionShouldBeRecorded(){
        ActionResult result = engine.performAction(firstPlayer.getId(), DepartmentType.DESIGN);
        assertTrue(result.successful());
        assertEquals(1, firstPlayer.getDesignPoints());
        assertEquals(1, engine.getActionHistory().size());
    }

    @Test
    void playersShoulNotActTwiceInSameWeek(){
        engine.performAction(firstPlayer.getId(), DepartmentType.DESIGN);
        ActionResult secondResult = engine.performAction(firstPlayer.getId(), DepartmentType.LOGISTICS);
        assertFalse(secondResult.successful());
        assertEquals(0,firstPlayer.getComponents());
        assertEquals(1, engine.getActionHistory().size());
    }

    @Test
    void weekShouldEndAfterAllPlayersAct(){
        engine.performAction(firstPlayer.getId(), DepartmentType.DESIGN);
        engine.performAction(secondPlayer.getId(),DepartmentType.LOGISTICS);
        assertEquals(2, gameState.getCurrentWeek());
        assertEquals(2, engine.getActionHistory().size());
        assertEquals(1, engine.getEvaluationHistory().size());
    }




}
