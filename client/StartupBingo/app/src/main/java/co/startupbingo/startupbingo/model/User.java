package co.startupbingo.startupbingo.model;

/**
 * Created by jubb on 15/04/16.
 */
public class User {

    public String userName;
    public String userRoom;
    public boolean isConnected;

    public User(String userName, String userRoom){
        this.userName = userName;
        this.userRoom = userRoom;
        this.isConnected = true;
    }

    @Override
    public boolean equals(Object otherUser){
        return otherUser instanceof User && ((User) otherUser).userName.equals(userName);
    }

}
