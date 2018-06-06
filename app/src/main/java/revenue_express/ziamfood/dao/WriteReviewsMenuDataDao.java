package revenue_express.ziamfood.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class WriteReviewsMenuDataDao {

    String id,menu_name,img_menu;

    public WriteReviewsMenuDataDao(String id , String menu_name, String img_menu) {
        this.id = id;
        this.menu_name = menu_name;
        this.img_menu = img_menu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getImg_menu() {
        return img_menu;
    }

    public void setImg_menu(String img_menu) {
        this.img_menu = img_menu;
    }
}
