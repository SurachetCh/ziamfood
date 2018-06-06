package revenue_express.ziamfood.model;

import io.realm.RealmObject;

/**
 * Created by chaleamsuk on 1/19/2017.
 */

public class TimeShop extends RealmObject {

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}