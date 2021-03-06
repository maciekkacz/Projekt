package library.model.comparator;

import library.model.Publication;

import java.util.Comparator;

public class DateComparator implements Comparator<Publication> {

    @Override
    public int compare(Publication p1, Publication p2) {
        if (p1 == null && p2 == null) {
            return 0;
        } else if (p1 == null) {
            return 1;
        } else if (p2 == null) {
            return -1;
        }
        Integer t1 = p1.getYear();
        Integer t2 = p2.getYear();
        return -t1.compareTo( t2 );
    }
}
