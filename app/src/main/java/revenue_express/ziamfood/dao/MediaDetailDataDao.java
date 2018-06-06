package revenue_express.ziamfood.dao;

import org.json.JSONArray;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class
MediaDetailDataDao {

    String type_social, title_social,description_social,score_social;
    Float rating;
    Integer id_social;
    String created_date_social, writer_id, writer_name, writer_photo;
    private JSONArray data_media_choice;

    public MediaDetailDataDao(Integer id_social, String type_social, Float rating, String title_social, String description_social, String score_social, String created_date_social, String writer_id, String writer_name, String writer_photo, JSONArray data_media_choice) {
        this.type_social = type_social;
        this.title_social = title_social;
        this.description_social = description_social;
        this.score_social = score_social;
        this.id_social = id_social;
        this.type_social = type_social;
        this.title_social = title_social;
        this.description_social = description_social;
        this.score_social = score_social;
        this.created_date_social = created_date_social;
        this.writer_id = writer_id;
        this.writer_name = writer_name;
        this.writer_photo = writer_photo;
        this.rating = rating;
        this.data_media_choice = data_media_choice;
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

    public Integer getId_social() {
        return id_social;
    }

    public void setId_social(Integer id_social) {
        this.id_social = id_social;
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

    public JSONArray getData_media_choice() {
        return data_media_choice;
    }

    public void setData_media_choice(JSONArray data_media_choice) {
        this.data_media_choice = data_media_choice;
    }
}
