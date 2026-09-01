package hr.tvz.kanban.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class GameSaveService {

    private static final Path SAVE_DIRECTORY = Path.of("saves");

    public Path save(SavedGame savedGame, String fileName) {
        Objects.requireNonNull(savedGame);
        Objects.requireNonNull(fileName);

        try {
            Files.createDirectories(SAVE_DIRECTORY);

            String safeFileName = Path.of(fileName).getFileName().toString();

            if(!safeFileName.endsWith(".dat")){
                safeFileName= safeFileName + ".dat";
            }
            Path saveFile = SAVE_DIRECTORY.resolve(safeFileName);

            try(ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(saveFile))){
                outputStream.writeObject(savedGame);
            }

            return saveFile;


        } catch (IOException e) {
            throw new IllegalStateException("Nije moguće spremit igru", e);
        }
    }

    public SavedGame load(Path saveFile){
        Objects.requireNonNull(saveFile);
        try(ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(saveFile))){
            Object loadedObjects = inputStream.readObject();

            if(loadedObjects instanceof SavedGame savedGame){
                return savedGame;
            }
            throw new IllegalStateException("Datoteka ne sadrži spremljenu igru");
        } catch (IOException | ClassNotFoundException exception){
            throw new IllegalStateException("Nije moguće učitat igru", exception);
        }
    }







}


