package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class NearByDao {
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
