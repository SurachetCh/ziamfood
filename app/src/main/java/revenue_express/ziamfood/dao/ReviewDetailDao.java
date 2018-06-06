package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class ReviewDetailDao {
    Boolean success;
    List<ReviewDetailDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ReviewDetailDataDao> getData() {
        return data;
    }

    public void setData(List<ReviewDetailDataDao> data) {
        this.data = data;
    }
}
