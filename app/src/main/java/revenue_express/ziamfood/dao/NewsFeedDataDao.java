package revenue_express.ziamfood.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class NewsFeedDataDao {

    String post_id,ref_id,type,post_title,stick_top,highlight,author_name,author_url,author_domain,created_timeago,created_date,modified_date,img_cover,shop_id,shop_name,address,city,state_code,img_logo,img_logo_thumb,writer_id,writer_name,writer_photo,writer_photo_thumb;
    Double review_score,review_price_rate,rating;

    private ArrayList<String> photo_img = new ArrayList<String>();
    private JSONObject jsonData,jsonObjectNewsFeed;
    private JSONArray media;

    public NewsFeedDataDao(JSONObject jsonObjectNewsFeed, JSONArray media, String post_id, String ref_id, String type, String post_title, String stick_top, String highlight, String author_name, String author_url, String author_domain, String created_timeago, String created_date, String modified_date, String img_cover, String shop_id, String shop_name, String address, String city, String state_code, String img_logo, String img_logo_thumb, String writer_id, String writer_name, String writer_photo, String writer_photo_thumb, Double review_score, Double review_price_rate, Double rating) {
        this.jsonObjectNewsFeed = jsonObjectNewsFeed;
        this.media = media;
        this.post_id =post_id;
        this.ref_id =ref_id;
        this.type = type;
        this.post_title = post_title;
        this.rating = rating;
        this.stick_top = stick_top;
        this.highlight = highlight;
        this.author_name = author_name;
        this.author_url = author_url;
        this.author_domain = author_domain;
        this.created_timeago = created_timeago;
        this.created_date = created_date;
        this.modified_date = modified_date;
        this.img_cover = img_cover;
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.address = address;
        this.city = city;
        this.state_code = state_code;
        this.img_logo = img_logo;
        this.img_logo_thumb = img_logo_thumb;
        this.writer_id = writer_id;
        this.writer_name = writer_name;
        this.writer_photo = writer_photo;
        this.writer_photo_thumb = writer_photo_thumb;

        this.review_score = review_score;
        this.review_price_rate = review_price_rate;
        this.rating = rating;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getStick_top() {
        return stick_top;
    }

    public void setStick_top(String stick_top) {
        this.stick_top = stick_top;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }

    public String getAuthor_domain() {
        return author_domain;
    }

    public void setAuthor_domain(String author_domain) {
        this.author_domain = author_domain;
    }

    public String getCreated_timeago() {
        return created_timeago;
    }

    public void setCreated_timeago(String created_timeago) {
        this.created_timeago = created_timeago;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getImg_cover() {
        return img_cover;
    }

    public void setImg_cover(String img_cover) {
        this.img_cover = img_cover;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
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

    public String getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(String writer_id) {
        this.writer_id = writer_id;
    }

    public String getWriter_name() {
        return writer_name;
    }

    public void setWriter_name(String writer_name) {
        this.writer_name = writer_name;
    }

    public String getWriter_photo() {
        return writer_photo;
    }

    public void setWriter_photo(String writer_photo) {
        this.writer_photo = writer_photo;
    }

    public String getWriter_photo_thumb() {
        return writer_photo_thumb;
    }

    public void setWriter_photo_thumb(String writer_photo_thumb) {
        this.writer_photo_thumb = writer_photo_thumb;
    }

    public Double getReview_score() {
        return review_score;
    }

    public void setReview_score(Double review_score) {
        this.review_score = review_score;
    }

    public Double getReview_price_rate() {
        return review_price_rate;
    }

    public void setReview_price_rate(Double review_price_rate) {
        this.review_price_rate = review_price_rate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public ArrayList<String> getPhoto_img() {
        return photo_img;
    }

    public void setPhoto_img(ArrayList<String> photo_img) {
        this.photo_img = photo_img;
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }

    public JSONObject getJsonObjectNewsFeed() {
        return jsonObjectNewsFeed;
    }

    public void setJsonObjectNewsFeed(JSONObject jsonObjectNewsFeed) {
        this.jsonObjectNewsFeed = jsonObjectNewsFeed;
    }

    public JSONArray getMedia() {
        return media;
    }

    public void setMedia(JSONArray media) {
        this.media = media;
    }
}
