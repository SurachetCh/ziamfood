package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class RecommendMenuReviewDao {
    Boolean success;
    List<RecommendMenuReviewDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<RecommendMenuReviewDataDao> getData() {
        return data;
    }

    public void setData(List<RecommendMenuReviewDataDao> data) {
        this.data = data;
    }
}
