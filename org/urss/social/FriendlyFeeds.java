/*
 * Danny Ayers Nov 3, 2004 http://dannyayers.com
 *  
 */
package org.urss.social;

import java.io.File;
import java.net.URI;

import org.kowari.itql.ItqlInterpreterBean;
import org.kowari.query.Answer;
import org.urss.feeds.FeedConstants;


public class FriendlyFeeds {

    static final String SOURCE_FOAF = "http://journal.dajobe.org/journal/2003/07/semblogs/bloggers.rdf";

    static final String RMI_HOST = "rmi://localhost/server1"; 

    static final String FRIENDS_MODEL = RMI_HOST + "#Friends";

    static final String CHANNELS_MODEL = RMI_HOST + "#ChannelList";

    public static void main(String[] args) {
        try {
            ItqlInterpreterBean itql = new ItqlInterpreterBean();

         //   itql.executeUpdate("drop <" + FRIENDS_MODEL + ">;");
            
            itql.executeUpdate("create <" + FRIENDS_MODEL + ">;");

            String loadQuery = "load<" + SOURCE_FOAF + "> " + "into <"
                    + FRIENDS_MODEL + ">;";

            itql.executeUpdate(loadQuery);

            String aliases = "alias <" + FeedConstants.RDF_NS + "> as rdf; "
                    + "alias <" + FeedConstants.RSS_NS + "> as rss; "
                    + "alias <" + FeedConstants.DC_NS + "> as dc; "
                    + "alias <" + FeedConstants.FOAF_NS + "> as foaf;";
 

            itql.executeUpdate(aliases);

            String channelsQuery = "select $subject " + "from <"
                    + FRIENDS_MODEL + ">"
                    + "where $subject <rdf:type> <rss:channel>;";

            Answer channelAnswer = itql.executeQuery(channelsQuery);

            String feedLoaderQuery;
            channelAnswer.beforeFirst();

            for (int i = 0; i < channelAnswer.getRowCount(); i++) {
                channelAnswer.next();
                System.out.println(i);

                feedLoaderQuery = "load <"
                        + channelAnswer.getObject(0).toString() + "> "
                        + " into <" + FRIENDS_MODEL + ">;";

                System.out.println(feedLoaderQuery);
                try {
                    itql.executeUpdate(feedLoaderQuery);
                } catch (Exception syntaxException) {
                    System.out.println("Error loading "
                            + channelAnswer.getObject(0).toString());
                }
            }

            channelAnswer.close();

       //     itql.executeUpdate("drop <" + CHANNELS_MODEL + ">;");
            itql.executeUpdate("create <" + CHANNELS_MODEL + ">;");

            String shiftChannelsQuery = "insert select $subject <rdf:type> <rss:channel> "
                    + "from <"
                    + FRIENDS_MODEL
                    + "> "
                    + "where $subject <rdf:type> <rss:channel> "
                    + "into <"
                    + CHANNELS_MODEL + ">;";

            itql.executeUpdate(shiftChannelsQuery);

            URI channelsURI = new URI(CHANNELS_MODEL);
            itql.backup(channelsURI, new File("friendly-channels.rdf"));
            URI friendsURI = new URI(FRIENDS_MODEL);
            itql.backup(friendsURI, new File("friends.rdf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Done.");
    }
}