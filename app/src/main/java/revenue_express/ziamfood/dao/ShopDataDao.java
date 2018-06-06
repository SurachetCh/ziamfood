package revenue_express.ziamfood.dao;

import java.util.List;

/**
 * Created by chaleamsuk on 12/26/2016.
 */

public class ShopDataDao {

    List<ShopDataDaoInfo> bssh_info;
    List<ShopDataDaoEnviroment> img_environment;
    List<ShopDataDaoMenu> img_menu;

    public List<ShopDataDaoInfo> getBssh_info() {
        return bssh_info;
    }

    public void setBssh_info(List<ShopDataDaoInfo> bssh_info) {
        this.bssh_info = bssh_info;
    }

    public List<ShopDataDaoEnviroment> getImg_environment() {
        return img_environment;
    }

    public void setImg_environment(List<ShopDataDaoEnviroment> img_environment) {
        this.img_environment = img_environment;
    }

    public List<ShopDataDaoMenu> getImg_menu() {
        return img_menu;
    }

    public void setImg_menu(List<ShopDataDaoMenu> img_menu) {
        this.img_menu = img_menu;
    }
}
