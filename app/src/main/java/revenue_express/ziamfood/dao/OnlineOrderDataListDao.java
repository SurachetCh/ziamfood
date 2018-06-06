package revenue_express.ziamfood.dao;

/**
 * Created by surachet on 1/5/2017.
 */

public class OnlineOrderDataListDao {
    int id;
    String name,description,photo,price,recommend;

    public OnlineOrderDataListDao(Integer id, String name, String description, String photo, String price, String recommend) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.price = price;
        this.recommend = recommend;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}
