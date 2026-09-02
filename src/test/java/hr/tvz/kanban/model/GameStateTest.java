package hr.tvz.kanban.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {
    @Test
    void gameShouldFinishAfterFifthWeek(){
        GameState gameState = new GameState();

        for (int index=0;index<4;index++){
            gameState.nextWeek();
        }
        assertEquals(GameState.MAX_WEEKS, gameState.getCurrentWeek());
        assertFalse(gameState.isGameFinished());
        gameState.nextWeek();
        assertTrue(gameState.isGameFinished());
        assertEquals(GameState.MAX_WEEKS,gameState.getCurrentWeek());
    }
}
