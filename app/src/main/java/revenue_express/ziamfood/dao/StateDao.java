package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class StateDao {
    Boolean success;
    List<StateDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<StateDataDao> getData() {
        return data;
    }

    public void setData(List<StateDataDao> data) {
        this.data = data;
    }
}
