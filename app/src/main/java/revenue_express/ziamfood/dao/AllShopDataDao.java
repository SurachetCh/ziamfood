package revenue_express.ziamfood.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class AllShopDataDao {

    String img_cover_thumb ,now_open, name_menu, description, photo,price, recommend, type_social, title_social,description_social,score_social,ref_id;
    Float rating;
    Integer id_menu,id_social,type_data;
    String created_date_social, writer_id, writer_name, writer_photo, video_last;
    private ArrayList<String> photo_img = new ArrayList<String>();
    private JSONObject jsonData,jsonObjectTimeline;
    private JSONArray media;

    public AllShopDataDao(JSONObject jsonObjectTimeline, Integer type_data, JSONObject jsonData, String img_cover_thumb, Float rating, String now_open, Integer id_menu, String name_menu, String description, String photo, String price, String recommend, Integer id_social, String ref_id, String type_social , String title_social, String description_social, String score_social, String created_date_social, String writer_id, String writer_name, String writer_photo, String video_last, JSONArray media) {
        this.jsonObjectTimeline = jsonObjectTimeline;
        this.type_data =type_data;
        this.jsonData = jsonData;
        this.img_cover_thumb = img_cover_thumb;
        this.rating = rating;
        this.now_open = now_open;

        this.id_menu = id_menu;
        this.name_menu = name_menu;
        this.description = description;
        this.photo = photo;
        this.price = price;

        this.recommend = recommend;
        this.type_social = type_social;
        this.title_social = title_social;
        this.description_social = description_social;
        this.score_social = score_social;
        this.video_last = video_last;
        this.media = media;
        this.id_social = id_social;
        this.ref_id = ref_id;
        this.type_social = type_social;
        this.title_social = title_social;
        this.description_social = description_social;
        this.score_social = score_social;
        this.created_date_social = created_date_social;
        this.writer_id = writer_id;
        this.writer_name = writer_name;
        this.writer_photo = writer_photo;
    }

    public JSONObject getJsonObjectTimeline() {
        return jsonObjectTimeline;
    }

    public void setJsonObjectTimeline(JSONObject jsonObjectTimeline) {
        this.jsonObjectTimeline = jsonObjectTimeline;
    }

    public JSONArray getMedia() {
        return media;
    }

    public void setMedia(JSONArray media) {
        this.media = media;
    }

    public Integer getType_data() {
        return type_data;
    }

    public void setType_data(Integer type_data) {
        this.type_data = type_data;
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }

    public String getImg_cover_thumb() {
        return img_cover_thumb;
    }

    public void setImg_cover_thumb(String img_cover_thumb) {
        this.img_cover_thumb = img_cover_thumb;
    }

    public String getNow_open() {
        return now_open;
    }

    public void setNow_open(String now_open) {
        this.now_open = now_open;
    }

    public String getName_menu() {
        return name_menu;
    }

    public void setName_menu(String name_menu) {
        this.name_menu = name_menu;
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

    public String getType_social() {
        return type_social;
    }

    public void setType_social(String type_social) {
        this.type_social = type_social;
    }

    public String getTitle_social() {
        return title_social;
    }

    public void setTitle_social(String title_social) {
        this.title_social = title_social;
    }

    public String getDescription_social() {
        return description_social;
    }

    public void setDescription_social(String description_social) {
        this.description_social = description_social;
    }

    public String getScore_social() {
        return score_social;
    }

    public void setScore_social(String score_social) {
        this.score_social = score_social;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getId_menu() {
        return id_menu;
    }

    public void setId_menu(Integer id_menu) {
        this.id_menu = id_menu;
    }

    public Integer getId_social() {
        return id_social;
    }

    public void setId_social(Integer id_social) {
        this.id_social = id_social;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getCreated_date_social() {
        return created_date_social;
    }

    public void setCreated_date_social(String created_date_social) {
        this.created_date_social = created_date_social;
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

    public String getVideo_last() {
        return video_last;
    }

    public void setVideo_last(String video_last) {
        this.video_last = video_last;
    }

    public ArrayList<String> getPhoto_img() {
        return photo_img;
    }

    public void setPhoto_img(ArrayList<String> photo_img) {
        this.photo_img = photo_img;
    }
}
