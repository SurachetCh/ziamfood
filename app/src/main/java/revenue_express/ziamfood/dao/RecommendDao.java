package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class RecommendDao {
    Boolean success;
    List<RecommendDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<RecommendDataDao> getData() {
        return data;
    }

    public void setData(List<RecommendDataDao> data) {
        this.data = data;
    }
}
