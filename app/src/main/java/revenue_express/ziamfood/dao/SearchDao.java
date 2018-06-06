package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class SearchDao {
    Boolean success;
    List<SearchDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<SearchDataDao> getData() {
        return data;
    }

    public void setData(List<SearchDataDao> data) {
        this.data = data;
    }
}
