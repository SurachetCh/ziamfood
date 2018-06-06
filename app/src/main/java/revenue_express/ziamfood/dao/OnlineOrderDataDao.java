package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by surachet on 1/5/2017.
 */

public class OnlineOrderDataDao {
    int bsic_id;
    String bsic_name;
    List<OnlineOrderDataListDao> menu_list;

    public OnlineOrderDataDao(String bsic_name) {
        this.bsic_name = bsic_name;
    }

    public int getBsic_id() {
        return bsic_id;
    }

    public void setBsic_id(int bsic_id) {
        this.bsic_id = bsic_id;
    }

    public String getBsic_name() {
        return bsic_name;
    }

    public void setBsic_name(String bsic_name) {
        this.bsic_name = bsic_name;
    }

    public List<OnlineOrderDataListDao> getMenu_list() {
        return menu_list;
    }

    public void setMenu_list(List<OnlineOrderDataListDao> menu_list) {
        this.menu_list = menu_list;
    }
}
