package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class NewsFeedDao {
    Boolean success;
    List<NewsFeedDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<NewsFeedDataDao> getData() {
        return data;
    }

    public void setData(List<NewsFeedDataDao> data) {
        this.data = data;
    }
}
