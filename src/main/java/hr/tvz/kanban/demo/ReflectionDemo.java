package hr.tvz.kanban.demo;

import hr.tvz.kanban.reflection.SandraDocumentationGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReflectionDemo {

    public static void main(String[] arg) throws IOException{
        SandraDocumentationGenerator generator = new SandraDocumentationGenerator();

        Path documentatioFile = generator.generate();

        System.out.println("Dokumentacija je generirana u: " + documentatioFile.toAbsolutePath());

        System.out.println();

        Files.readAllLines(documentatioFile).forEach(System.out::println);


    }
}
