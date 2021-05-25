package outputwriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public final class OutputWriter {
    private FileWriter outputWriter;

    public OutputWriter(final String outputPath) throws IOException {
        this.outputWriter = new FileWriter(outputPath);
    }

    /**
     * Scrie fisierul de iesire
     * @param object
     */
    public void closeJSON(final HashMap<String, JSONArray> object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputWriter, object);
            outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
