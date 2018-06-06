package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class SumOrderListDao {
    Boolean success;
    List<SumOrderListDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<SumOrderListDataDao> getData() {
        return data;
    }

    public void setData(List<SumOrderListDataDao> data) {
        this.data = data;
    }
}
