package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by chaleamsuk on 12/26/2016.
 */

public class WriteReviewsMenuDao {
    Boolean success;
    List<WriteReviewsMenuDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<WriteReviewsMenuDataDao> getData() {
        return data;
    }

    public void setData(List<WriteReviewsMenuDataDao> data) {
        this.data = data;
    }
}
