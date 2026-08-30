package hr.tvz.kanban.servica;

import hr.tvz.kanban.engine.KanbanEngine;
import hr.tvz.kanban.model.WeekAction;
import hr.tvz.kanban.replay.DomReplayReader;
import hr.tvz.kanban.replay.DomReplayWriter;
import hr.tvz.kanban.serialization.GameSaveService;
import hr.tvz.kanban.serialization.SavedGame;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LocalGameFileService {

    private static final Path SAVE_FILE = Path.of("saves", "kanban-current.dat");

    private static final Path REPLAY_FILE = Path.of("replays", "kanban-current.xml");

    private final GameSaveService saveService = new GameSaveService();

    private final DomReplayWriter replyWriter = new DomReplayWriter();

    private final DomReplayReader replayReader = new DomReplayReader();




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

    public Path exportReplay(KanbanEngine engine){
        List<WeekAction> actions = engine.getActionHistory();
        if(actions.isEmpty()){
            throw new IllegalStateException("Još nema poteza za izvoz");
        }
        return replyWriter.write(actions,"kanban-current");

    }

    public List<WeekAction> loadReplay(){
        if(!Files.exists(REPLAY_FILE)){
            throw new IllegalStateException("Najprije izvezi replay u xml");
        }
        List<WeekAction> actions = replayReader.read(REPLAY_FILE);
        if(actions.isEmpty()){
            throw new IllegalStateException("xml nema spremljenih poteza");
        }
        return actions;



    }





}
