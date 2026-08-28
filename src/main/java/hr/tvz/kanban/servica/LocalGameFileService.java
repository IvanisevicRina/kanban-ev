package hr.tvz.kanban.servica;

import hr.tvz.kanban.engine.KanbanEngine;
import hr.tvz.kanban.serialization.GameSaveService;
import hr.tvz.kanban.serialization.SavedGame;

import java.nio.file.Files;
import java.nio.file.Path;

public class LocalGameFileService {

    private static final Path SAVE_FILE = Path.of("saves", "kanban-current.dat");

    private final GameSaveService saveService = new GameSaveService();

    public Path saveGame(KanbanEngine engine){
        SavedGame savedGame = engine.createSavedGame();

        return saveService.save(savedGame,"kanban-current");
    }

    public SavedGame loadGame(){
        if(!Files.exists(SAVE_FILE)){
            throw new IllegalStateException("Još ne postoji spremljena igra");
        }
        return saveService.load(SAVE_FILE);
    }




}
