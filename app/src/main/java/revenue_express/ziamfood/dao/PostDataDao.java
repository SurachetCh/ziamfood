package revenue_express.ziamfood.dao;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class PostDataDao {

    String ref_id,created_timeago,writer_name,highlight,writer_photo,shop_name;
    private JSONObject jsonObjectTimeline;
    private JSONArray media;


    public PostDataDao(JSONObject jsonObjectTimeline, String ref_id, String created_timeago, String writer_name, String highlight , JSONArray media, String writer_photo, String shop_name) {
        this.jsonObjectTimeline = jsonObjectTimeline;
        this.ref_id = ref_id;
        this.created_timeago = created_timeago;
        this.writer_name = writer_name;
        this.highlight = highlight;
        this.media = media;
        this.writer_photo = writer_photo;
        this.shop_name = shop_name;

    }

    public JSONObject getJsonObjectTimeline() {
        return jsonObjectTimeline;
    }

    public void setJsonObjectTimeline(JSONObject jsonObjectTimeline) {
        this.jsonObjectTimeline = jsonObjectTimeline;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getCreated_timeago() {
        return created_timeago;
    }

    public void setCreated_timeago(String created_timeago) {
        this.created_timeago = created_timeago;
    }

    public String getWriter_name() {
        return writer_name;
    }

    public void setWriter_name(String writer_name) {
        this.writer_name = writer_name;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public JSONArray getMedia() {
        return media;
    }

    public void setMedia(JSONArray media) {
        this.media = media;
    }

    public String getWriter_photo() {
        return writer_photo;
    }

    public void setWriter_photo(String writer_photo) {
        this.writer_photo = writer_photo;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}
