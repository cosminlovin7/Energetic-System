import members.Consumer;
import members.Distributor;

import inputreader.Input;
import inputreader.InputLoader;

import org.json.simple.JSONArray;
import outputwriter.OutputGenerator;
import outputwriter.OutputWriter;
import simulate.Simulate;
import update.Update;
import utils.Constants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Main {
    private Main() { }
    /**
     * Primeste parametrii sub forma de args :
     * - inputPath = args[0]
     * - outputPath = args[1]
     * Realizeaza simularea pentru fiecare test
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        String path = args[Constants.INPUT_FILE];
        InputLoader inputLoader = new InputLoader(path);
        Input input = inputLoader.readInputData();

        Integer numberOfTurns = input.getNumberOfTurns();
        Map<Integer, Distributor> distributorsList = input.getDistributors();
        Map<Integer, Consumer> consumersList = input.getConsumers();
        List<Update> monthlyUpdateList = input.getMonthlyUpdates();

        Simulate simulate = Simulate.getInstance();
        simulate.game(numberOfTurns, consumersList, distributorsList, monthlyUpdateList);

        String outputPath = args[Constants.OUTPUT_FILE];
        OutputGenerator outputGenerator = OutputGenerator.getInstance();
        JSONArray jsonConsumers = outputGenerator.getJSONConsumers(consumersList);
        JSONArray jsonDistributors = outputGenerator.getJSONDistributors(distributorsList);
        OutputWriter outWriter = new OutputWriter(outputPath);

        HashMap<String, JSONArray> newMap = new LinkedHashMap<>();
        newMap.put("consumers", jsonConsumers);
        newMap.put("distributors", jsonDistributors);
        outWriter.closeJSON(newMap);
    }
}
