package revenue_express.ziamfood.dao;

/**
 * Created by chaleamsuk on 12/25/2016.
 */

public class DealDataDao {
    
    String bssh_title,bssh_detail,bssh_distance,bssh_imgfront,bssh_imghead,bssh_loc1,bssh_loc2,bssh_maps,bssh_phone,bssh_score,bssh_website,bssh_address,bssh_id;

    public DealDataDao(String bssh_id, String bssh_imghead) {
        this.bssh_id = bssh_id;
        this.bssh_imghead = bssh_imghead;
    }

    public String getBssh_id() {
        return bssh_id;
    }

    public void setBssh_id(String bssh_id) {
        this.bssh_id = bssh_id;
    }

    public String getBssh_title() {
        return bssh_title;
    }

    public void setBssh_title(String bssh_title) {
        this.bssh_title = bssh_title;
    }

    public String getBssh_detail() {
        return bssh_detail;
    }

    public void setBssh_detail(String bssh_detail) {
        this.bssh_detail = bssh_detail;
    }

    public String getBssh_distance() {return bssh_distance;}

    public void setBssh_distance(String bssh_distance) {
        this.bssh_distance = bssh_distance;
    }

    public String getBssh_imgfront() {
        return bssh_imgfront;
    }

    public void setBssh_imgfront(String bssh_imgfront) {
        this.bssh_imgfront = bssh_imgfront;
    }

    public String getBssh_imghead() {
        return bssh_imghead;
    }

    public void setBssh_imghead(String bssh_imghead) {
        this.bssh_imghead = bssh_imghead;
    }

    public String getBssh_loc1() {
        return bssh_loc1;
    }

    public void setBssh_loc1(String bssh_loc1) {
        this.bssh_loc1 = bssh_loc1;
    }

    public String getBssh_loc2() {
        return bssh_loc2;
    }

    public void setBssh_loc2(String bssh_loc2) {
        this.bssh_loc2 = bssh_loc2;
    }

    public String getBssh_maps() {
        return bssh_maps;
    }

    public void setBssh_maps(String bssh_maps) {
        this.bssh_maps = bssh_maps;
    }

    public String getBssh_phone() {
        return bssh_phone;
    }

    public void setBssh_phone(String bssh_phone) {
        this.bssh_phone = bssh_phone;
    }

    public String getBssh_score() {
        return bssh_score;
    }

    public void setBssh_score(String bssh_score) {
        this.bssh_score = bssh_score;
    }

    public String getBssh_website() {
        return bssh_website;
    }

    public void setBssh_website(String bssh_website) {
        this.bssh_website = bssh_website;
    }

    public String getBssh_address() {
        return bssh_address;
    }

    public void setBssh_address(String bssh_address) {
        this.bssh_address = bssh_address;
    }

}
