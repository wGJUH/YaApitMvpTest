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
        /**
         * Событие очищения последней абочей информации
         */
        BTN_CLEAR_CLICKED,
        /**
         * Событие окончания перевода слова
         */
        WORD_TRANSLATED,
        /**
         * Событие изменения конечного языка перевода
         */
        TARGET_LANGUAGE,
        /**
         * Событие изменения целевого языка перевода
         */
        FROM_LANGUAGE,
        /**
         * Событие Обновления
         */
        UPDATE_FAVORITE,
        /**
         * Событие удалить все из избранного с меткой "не избранное"
         */
        DELETE_FAVORITE,
        /**
         * Событие начать перевод
         */
        START_TRANSLATE,
        /**
         * Событие смены языков
         */
        CHANGE_LANGUAGES
    }
    public final Object[] content;
    public final EventType eventType;

    public Event(EventType eventType, Object... content) {
        this.eventType = eventType;
        this.content = content;
    }
}