package revenue_express.ziamfood.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class StateDataDao {
    int loch_id;
    String loch_name,loch_abrv,loch_store_count;

    public int getLoch_id() {
        return loch_id;
    }

    public void setLoch_id(int loch_id) {
        this.loch_id = loch_id;
    }

    public String getLoch_name() {
        return loch_name;
    }

    public void setLoch_name(String loch_name) {
        this.loch_name = loch_name;
    }

    public String getLoch_abrv() {
        return loch_abrv;
    }

    public void setLoch_abrv(String loch_abrv) {
        this.loch_abrv = loch_abrv;
    }

    public String getLoch_store_count() {
        return loch_store_count;
    }

    public void setLoch_store_count(String loch_store_count) {
        this.loch_store_count = loch_store_count;
    }
}
