package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class AllShopDao {
    Boolean success;
    List<AllShopDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<AllShopDataDao> getData() {
        return data;
    }

    public void setData(List<AllShopDataDao> data) {
        this.data = data;
    }
}
