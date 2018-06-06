package revenue_express.ziamfood.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class WriteReviewsSetImageDataDao {

    String id_image,name_image;

    public WriteReviewsSetImageDataDao(String id_image , String name_image) {
        this.id_image = id_image;
        this.name_image = name_image;
    }

    public String getId_image() {
        return id_image;
    }

    public void setId_image(String id_image) {
        this.id_image = id_image;
    }

    public String getName_image() {
        return name_image;
    }

    public void setName_image(String name_image) {
        this.name_image = name_image;
    }
}
