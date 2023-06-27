package top.rrricardo.postletter.models;

public interface OrientationBase {
    default String getNickname(){
        return null;
    }

    default String getUsername(){
        return null;
    }

    default int getId(){
        return 0;
    }

    default int getFriendId(){
        return 0;
    }

    default int getSessionId() {
        return 0;
    }
}
