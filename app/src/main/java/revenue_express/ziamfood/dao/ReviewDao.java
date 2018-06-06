package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class ReviewDao {
    Boolean success;
    List<ReviewDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ReviewDataDao> getData() {
        return data;
    }

    public void setData(List<ReviewDataDao> data) {
        this.data = data;
    }
}
