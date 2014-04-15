/*
 * Danny Ayers Oct 26, 2004 http://dannyayers.com
 *  
 */
package org.urss.planet;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.urss.feeds.Entry;

public class EntryDateSorter implements Comparator {

    private final SimpleDateFormat RFC822 = new SimpleDateFormat(
            "EEE, d MMM yyyy HH:mm:ss z");

    private Date fallbackDate = new Date(System.currentTimeMillis() - 604800000);

    private static EntryDateSorter dateComparator = new EntryDateSorter();

    public static void sort(List unsorted) {
        Collections.sort(unsorted, dateComparator);
    }

    public int compare(Object objectA, Object objectB) {
        String dateStringA = ((Entry) objectA).getDate();
        String dateStringB = ((Entry) objectB).getDate();
        Date dateA;
        Date dateB;
        try {
            dateA = RFC822.parse(dateStringA);
        } catch (Exception eA) {
            dateA = fallbackDate;
            eA.printStackTrace();
        }
        try {
            dateB = RFC822.parse(dateStringB);
        } catch (Exception eB) {
            dateB = fallbackDate;
            eB.printStackTrace();
        }
        return dateB.compareTo(dateA); // reverse-chrono
    }
}