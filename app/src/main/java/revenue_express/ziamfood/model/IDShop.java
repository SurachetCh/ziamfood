package revenue_express.ziamfood.model;

import io.realm.RealmObject;

/**
 * Created by surachet on 1/12/2017.
 */

public class IDShop extends RealmObject {
    private int idshop;
    private String nameshop;


    public int getIdshop() {
        return idshop;
    }

    public void setIdshop(int id) {
        this.idshop = id;
    }
}
