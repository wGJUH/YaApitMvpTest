package test.ya.translater.wgjuh.yaapitmvptest.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    private Event(Parcel in) {
        this.content = in.createTypedArray(Event.CREATOR);
        this.eventType = EventType.valueOf(in.readString());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeArray(content);
        parcel.writeString(eventType.toString());
    }

    public enum EventType {
        BTN_CLEAR_CLICKED,
        WORD_TRANSLATED,
        WORD_UPDATED,
        TARGET_LANGUAGE,
        FROM_LANGUAGE,
        UPDATE_FAVORITE,
        DELETE_FAVORITE,
        START_TRANSLATE,
        CHANGE_LANGUAGES
    }

    public final Object[] content;
    public final EventType eventType;


    public Event(EventType eventType, Object... content) {
        this.eventType = eventType;
        this.content = content;
    }
}