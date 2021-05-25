package outputwriter;

import members.Distributor;

import java.util.Comparator;

public final class SortByContractAndId implements Comparator<Distributor.Contracts> {
    /**
     * Suprascrierea metodei compare, in acest caz, ordoneaza o lista de
     * contracte, intai dupa numarul de luni ramase ale contractului incheiat,
     * crescator, de la cel mai mic contract la cel mai mare, apoi in functie
     * de id-ul consumatorului
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(final Distributor.Contracts o1, final Distributor.Contracts o2) {
        if (o1.getRemainedContractMonths() > o2.getRemainedContractMonths()) {
            return 1;
        } else if (o1.getRemainedContractMonths() < o2.getRemainedContractMonths()) {
            return -1;
        }

        if (o1.getConsumerId() > o2.getConsumerId()) {
            return 1;
        } else if (o1.getConsumerId() < o2.getConsumerId()) {
            return -1;
        }

        return 0;
    }
}
