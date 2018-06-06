package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class PostDao {
    Boolean success;
    List<BlogDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<BlogDataDao> getData() {
        return data;
    }

    public void setData(List<BlogDataDao> data) {
        this.data = data;
    }
}
