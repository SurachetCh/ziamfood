package revenue_express.ziamfood.dao;

/**
 * Created by chaleamsuk on 12/25/2016.
 */

public class RecommendMenuReviewDataDao {
    String recommend_menu_name,recommend_menu_photo;

    public RecommendMenuReviewDataDao(String recommend_menu_name, String recommend_menu_photo) {
        this.recommend_menu_name = recommend_menu_name;
        this.recommend_menu_photo = recommend_menu_photo;
    }

    public String getRecommend_menu_name() {
        return recommend_menu_name;
    }

    public void setRecommend_menu_name(String recommend_menu_name) {
        this.recommend_menu_name = recommend_menu_name;
    }

    public String getRecommend_menu_photo() {
        return recommend_menu_photo;
    }

    public void setRecommend_menu_photo(String recommend_menu_photo) {
        this.recommend_menu_photo = recommend_menu_photo;
    }
}
