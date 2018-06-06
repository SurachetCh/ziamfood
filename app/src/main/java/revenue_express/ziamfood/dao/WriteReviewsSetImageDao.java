package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by chaleamsuk on 12/26/2016.
 */

public class WriteReviewsSetImageDao {
    Boolean success;
    List<WriteReviewsSetImageDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<WriteReviewsSetImageDataDao> getData() {
        return data;
    }

    public void setData(List<WriteReviewsSetImageDataDao> data) {
        this.data = data;
    }
}
