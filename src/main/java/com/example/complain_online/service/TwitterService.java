package com.example.complain_online.service;

import com.example.complain_online.model.MentionDetail;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 *
 * @author Juang Nasution
 */
@Service
public class TwitterService {

    private final Twitter twitter = TwitterFactory.getSingleton();

    public List<Status> asdf() throws TwitterException {
        List<MentionDetail> mentionDetails = new ArrayList<>();
//        for (Status tweet : twitter.getMentionsTimeline()) {
//            MentionDetail mentionDetail = new MentionDetail();
//            mentionDetail.setText(tweet.getText());
//            mentionDetail.setUser(tweet.);
//            mentionDetail.setImage(tweet.getProfileImageUrl());
//            mentionDetail.setCreatedAt(tweet.getCreatedAt());
//            mentionDetails.add(mentionDetail);
//        }

        return twitter.getMentionsTimeline();
    }

    public String sendDirectMessage(String name) throws TwitterException {
        TwitterFactory factory = new TwitterFactory();
        Twitter twitterr = factory.getInstance();
        DirectMessage directMessage = twitterr.sendDirectMessage(name, "hai ini testing API yaaaaa"); //        DirectMessage message = twitter.sendDirectMessage(name, "ini testing API");
//        DirectMessage message = twitter.sendDirectMessage(1160112166067830785L, "hai ini testing API");
        return directMessage.getText();
    }

    public List<DirectMessage> directMessage() throws TwitterException {
        return twitter.getDirectMessages();
    }

}
