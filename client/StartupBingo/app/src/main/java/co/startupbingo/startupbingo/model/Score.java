package co.startupbingo.startupbingo.model;

/**
 * Created by jubb on 15/04/16.
 */
public class Score {
    private User associatedUser;
    private Integer userScore;

    public Score(User user, Integer score) {
        this.associatedUser = user;
        this.userScore = score;
    }

    public Integer getScore(){
        return this.userScore;
    }

    public User getUser() {
        return this.associatedUser;
    }

    public String getName() {
        return this.associatedUser.userName;
    }
}
