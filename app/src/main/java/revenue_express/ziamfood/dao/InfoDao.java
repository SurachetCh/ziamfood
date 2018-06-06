package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class InfoDao {
    Boolean success;
    List<InfoDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<InfoDataDao> getData() {
        return data;
    }

    public void setData(List<InfoDataDao> data) {
        this.data = data;
    }
}
