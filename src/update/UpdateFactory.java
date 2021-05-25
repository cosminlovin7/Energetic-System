package update;

/**
 * Clasa este un factory ce implementeaza un singleton
 * si intoarce noi tipuri de update-uri :
 * - CostChange
 * - NewConsumer
 */
public final class UpdateFactory {
    private static final UpdateFactory INSTANCE = new UpdateFactory();
    private UpdateFactory() { }
    public static UpdateFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Returneaza un consumator nou sau un update pentru
     * costul unui distribuitor de energie
     * @param updateType
     * @param id
     * @param param1
     * @param param2
     * @return
     */
    public UpdateMember getNewUpdateMember(final String updateType,
                                           final Integer id,
                                           final long param1,
                                           final long param2) {
        if (updateType.equals("newConsumer")) {
            return new NewConsumer(id, param1, param2);
        } else if (updateType.equals("costChange")) {
            return new CostChange(id, param1, param2);
        }
        return null;
    }
}
