package src.entities;

public class User {
    protected String name;
    protected int avatarId;
    
    public User(String name, int avatarId) {
        this.avatarId = avatarId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarId(int avatarId) {
        if (avatarId >= 0 && avatarId <= 7) {
            this.avatarId = avatarId;
        }
    }
}
