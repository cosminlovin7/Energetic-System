package inputreader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import members.Consumer;
import members.Distributor;
import members.MembersGenerator;

import update.CostChange;
import update.NewConsumer;
import update.Update;
import update.UpdateFactory;

import utils.Constants;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Citeste datele de intrare din fisierele .json
 */
public final class InputLoader {
    private final String inputPath;

    public InputLoader(final String inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * @return locatia fisierului de input
     */
    public String getInputPath() {
        return inputPath;
    }

    /**
     * Citeste datele de intrare si returneaza un obiect
     * input ce contine listele de consumatori, distribuitori
     * si update-urile lunare.
     * @return
     */
    public Input readInputData() {
        Integer numberOfTurns = 0;
        Map<Integer, Consumer> consumersList = new HashMap<>();
        Map<Integer, Distributor> distributorsList = new HashMap<>();
        List<Update> monthlyUpdatesList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(inputPath));
            numberOfTurns = Integer.parseInt(jsonObject
                    .get(Constants.NUMBER_OF_TURNS).toString());

            JSONObject initialData = (JSONObject) jsonObject.get(Constants.INITIAL_DATA);
            JSONArray consumers = (JSONArray) initialData.get(Constants.CONSUMERS);
            JSONArray distributors = (JSONArray) initialData.get(Constants.DISTRIBUTORS);
            MembersGenerator membersFactory = MembersGenerator.getInstance();
            UpdateFactory updateFactory = UpdateFactory.getInstance();

            if (consumers != null) {
                for (Object consumer : consumers) {
                    Integer id = Integer.parseInt(((JSONObject) consumer)
                            .get(Constants.ID).toString());
                    consumersList.put(id, (Consumer) membersFactory.getMember(id,
                                      Integer.parseInt(((JSONObject) consumer)
                                              .get(Constants.INITIAL_BUDGET).toString()),
                                      Integer.parseInt(((JSONObject) consumer)
                                              .get(Constants.MONTHLY_INCOME).toString())));
                }
            } else {
                System.out.println("Nu exista consumatori.");
                consumersList = null;
            }

            if (distributors != null) {
                for (Object distributor : distributors) {
                    Integer id = Integer.parseInt(((JSONObject) distributor)
                            .get(Constants.ID).toString());
                    distributorsList.put(id, (Distributor) membersFactory.getMember(id,
                                      Integer.parseInt(((JSONObject) distributor)
                                              .get(Constants.CONTRACT_LENGTH).toString()),
                                      Integer.parseInt(((JSONObject) distributor)
                                              .get(Constants.INITIAL_BUDGET).toString()),
                                      Integer.parseInt(((JSONObject) distributor)
                                              .get(Constants.INITIAL_INFR_COST).toString()),
                                      Integer.parseInt(((JSONObject) distributor)
                                              .get(Constants.INITIAL_PROD_COST).toString())));
                }
            } else {
                System.out.println("Nu exista distribuitori.");
                distributorsList = null;
            }

            JSONArray monthlyUpdates = (JSONArray) jsonObject.get(Constants.MONTHLY_UPDATES);

            if (monthlyUpdates != null) {
                for (Object monthlyUpdate : monthlyUpdates) {
                    Update newUpdate = new Update();
                    List<NewConsumer> newUpdateConsumers = newUpdate.getNewConsumers();
                    List<CostChange> newUpdateCostsChanges = newUpdate.getCostChanges();

                    JSONArray newConsumers =
                            (JSONArray) ((JSONObject) monthlyUpdate).get(Constants.NEW_CONSUMERS);
                    JSONArray costsChanges =
                            (JSONArray) ((JSONObject) monthlyUpdate).get(Constants.COSTS_CHANGES);

                    if (newConsumers.size() == 0) {
                        newUpdateConsumers.add((NewConsumer) updateFactory.getNewUpdateMember(
                                "newConsumer",
                                -1,
                                -1,
                                -1));
                    } else {
                        for (Object consumer : newConsumers) {
                            newUpdateConsumers.add((NewConsumer) updateFactory.getNewUpdateMember(
                                "newConsumer",
                                Integer.parseInt(((JSONObject) consumer)
                                        .get(Constants.ID).toString()),
                                Integer.parseInt(((JSONObject) consumer)
                                        .get(Constants.INITIAL_BUDGET).toString()),
                                Integer.parseInt(((JSONObject) consumer)
                                        .get(Constants.MONTHLY_INCOME).toString())));
                        }
                    }

                    if (costsChanges.size() == 0) {
                        newUpdateCostsChanges.add((CostChange) updateFactory.getNewUpdateMember(
                                "costChange",
                                -1,
                                -1,
                                -1));
                    } else {
                        for (Object cost : costsChanges) {
                            newUpdateCostsChanges
                                    .add((CostChange) updateFactory.getNewUpdateMember(
                                "costChange",
                                Integer.parseInt(((JSONObject) cost)
                                        .get(Constants.ID).toString()),
                                Integer.parseInt(((JSONObject) cost)
                                        .get(Constants.INFR_COST).toString()),
                                Integer.parseInt(((JSONObject) cost)
                                        .get(Constants.PROD_COST).toString())));
                        }
                    }
                    monthlyUpdatesList.add(newUpdate);
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return new Input(numberOfTurns, consumersList, distributorsList, monthlyUpdatesList);
    }
}
