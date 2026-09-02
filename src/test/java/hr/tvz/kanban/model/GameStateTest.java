package hr.tvz.kanban.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    @Test
    void gameShouldFinishAfterSixtgWeek(){
        GameState gameState = new GameState();

        for (int index=0;index<5;index++){
            gameState.nextWeek();
        }
        assertEquals(GameState.MAX_WEEKS, gameState.getCurrentWeek());
        assertFalse(gameState.isGameFinished());
        gameState.nextWeek();
        assertTrue(gameState.isGameFinished());
        assertEquals(GameState.MAX_WEEKS,gameState.getCurrentWeek());
    }
}
