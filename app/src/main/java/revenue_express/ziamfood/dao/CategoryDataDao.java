package revenue_express.ziamfood.dao;

/**
 * Created by chaleamsuk on 12/25/2016.
 */

public class CategoryDataDao {
    Integer id;
    String name;
    public CategoryDataDao(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
