package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by chaleamsuk on 12/26/2016.
 */

public class DealDao {
    Boolean success;
    List<NearByDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<NearByDataDao> getData() {
        return data;
    }

    public void setData(List<NearByDataDao> data) {
        this.data = data;
    }
}
