package revenue_express.ziamfood.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class ReviewDetailDataDao {
    private String photo_img,photo_caption;

    public ReviewDetailDataDao(String photo_img, String photo_caption) {
        this.photo_img = photo_img;
        this.photo_caption = photo_caption;
    }

    public String getPhoto_img() {
        return photo_img;
    }

    public void setPhoto_img(String photo_img) {
        this.photo_img = photo_img;
    }

    public String getPhoto_caption() {
        return photo_caption;
    }

    public void setPhoto_caption(String photo_caption) {
        this.photo_caption = photo_caption;
    }
}
