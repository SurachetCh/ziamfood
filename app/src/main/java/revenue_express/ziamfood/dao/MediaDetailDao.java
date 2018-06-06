package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class MediaDetailDao {
    Boolean success;
    List<PostDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<PostDataDao> getData() {
        return data;
    }

    public void setData(List<PostDataDao> data) {
        this.data = data;
    }
}
