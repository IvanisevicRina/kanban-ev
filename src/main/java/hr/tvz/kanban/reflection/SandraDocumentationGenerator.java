package hr.tvz.kanban.reflection;

import hr.tvz.kanban.engine.SandraAI;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SandraDocumentationGenerator {

    private static final Path OUTPUT_FILE = Path.of("generated-docs", "sadra-rules.txt");

    public Path generate() {
        List<String> documentation = createDocumentation();

        try {
            Files.createDirectories(OUTPUT_FILE.getParent());
            Files.write(OUTPUT_FILE, documentation, StandardCharsets.UTF_8);

            return OUTPUT_FILE;


        } catch (IOException e) {
            throw new IllegalStateException("Nije moguce generirati dokumentaciju sandrinih pravila " + e);
        }
    }


        private List<String> createDocumentation() {

        List<String> lines = new ArrayList<>();
        lines.add("sandRINA PRAVILA EVALUACIJE\n");

        Method[] methods = SandraAI.class.getDeclaredMethods();

        Arrays.sort(methods, Comparator.comparing(Method::getName));

        for (Method method : methods){
            appendRule(lines, method);
        }
        return lines;





        }

    private void appendRule(List<String> lines, Method method) {

        EvaluationRule rule = method.getAnnotation(EvaluationRule.class);
        if(rule == null){
            return;
        }
        lines.add("Metoda: "+ method.getName());
        lines.add("Opis: "+ rule.description());
        lines.add("Uvijet: "+ rule.condition());
        lines.add("");



    }


}
