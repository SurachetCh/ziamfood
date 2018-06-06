package revenue_express.ziamfood.dao;

/**
 * Created by chaleamsuk on 12/25/2016.
 */

public class InfoDataDao {
    Integer id;
    String name,phone,website,state_code,address,zipcode,map_position,img_cover,img_cover_thumb,img_logo,img_logo_thumb;
    Double distance,score_rate,price_rate;
    public InfoDataDao(Integer id, String name, String phone, String website, String state_code, String address, String zipcode, String map_position, String img_cover, String img_cover_thumb, String img_logo, String img_logo_thumb, Double distance, Double score_rate, Double price_rate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.state_code = state_code;
        this.address = address;
        this.zipcode = zipcode;
        this.map_position = map_position;
        this.img_cover = img_cover;
        this.img_cover_thumb = img_cover_thumb;
        this.img_logo = img_logo;
        this.img_logo_thumb = img_logo_thumb;
        this.distance = distance;
        this.score_rate = score_rate;
        this.price_rate = price_rate;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMap_position() {
        return map_position;
    }

    public void setMap_position(String map_position) {
        this.map_position = map_position;
    }

    public String getImg_cover() {
        return img_cover;
    }

    public void setImg_cover(String img_cover) {
        this.img_cover = img_cover;
    }

    public String getImg_cover_thumb() {
        return img_cover_thumb;
    }

    public void setImg_cover_thumb(String img_cover_thumb) {
        this.img_cover_thumb = img_cover_thumb;
    }

    public String getImg_logo() {
        return img_logo;
    }

    public void setImg_logo(String img_logo) {
        this.img_logo = img_logo;
    }

    public String getImg_logo_thumb() {
        return img_logo_thumb;
    }

    public void setImg_logo_thumb(String img_logo_thumb) {
        this.img_logo_thumb = img_logo_thumb;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getScore_rate() {
        return score_rate;
    }

    public void setScore_rate(Double score_rate) {
        this.score_rate = score_rate;
    }

    public Double getPrice_rate() {
        return price_rate;
    }

    public void setPrice_rate(Double price_rate) {
        this.price_rate = price_rate;
    }
}
