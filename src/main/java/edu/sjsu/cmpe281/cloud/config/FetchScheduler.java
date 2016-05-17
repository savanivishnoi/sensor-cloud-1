package edu.sjsu.cmpe281.cloud.config;

import edu.sjsu.cmpe281.cloud.service.ICencoosConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Naks on 13-May-16.
 * Implementation class to fetch the data periodically
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@Component
public class FetchScheduler {

    private static final Logger logger = LoggerFactory.getLogger(FetchScheduler.class);

    @Autowired
    ICencoosConnect connectURL;

    @Scheduled(fixedRate = 1000*60*15)
    public void updateDatabasePeriodically() {
        try {
            // // TODO: 16-May-16 remove hardcode
            boolean updatedDb = connectURL.fetchData("2016-04-27T00:00:00Z");
            logger.info("Scheduling task is running..");
        } catch (Exception e) {
            logger.error("Exception in scheduler task: "+e.getMessage());
        }
    }

}
