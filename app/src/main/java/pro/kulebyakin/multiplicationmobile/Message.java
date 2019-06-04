package pro.kulebyakin.multiplicationmobile;

import java.util.Date;

public class Message {

    // TODO Добавить время и дату
    private String id;
    private String text;
    private String name;
    private String photoUrl;
    private long timeMessage;

    public Message() {
    }

    public Message(String text, String name, String photoUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;

        timeMessage = new Date().getTime();
    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
