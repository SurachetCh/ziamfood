package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by surachet on 1/5/2017.
 */

public class OnlineOrderDao {
    Boolean success;
    List<OnlineOrderDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<OnlineOrderDataDao> getData() {
        return data;
    }

    public void setData(List<OnlineOrderDataDao> data) {
        this.data = data;
    }
}
