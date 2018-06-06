package revenue_express.ziamfood.dao;

/**
 * Created by chaleamsuk on 12/25/2016.
 */

public class ReviewDataDao {

    String post_id,post_type,post_ref_id,post_title,post_stick_top,post_highlight,review_score,review_price_rate,author_name,author_url,author_domain,created_timeago,created_date,modified_date,post_img_cover,
            shop_id,shop_name,shop_address,city,state_code,rating,shop_img_logo,shop_img_logo_thumb,
            writer_id,writer_name,writer_photo,writer_photo_thumb;

    public ReviewDataDao(String post_id, String post_type, String post_ref_id, String post_title, String post_stick_top, String post_highlight, String review_score, String review_price_rate, String author_name, String author_url, String author_domain, String created_timeago, String created_date, String modified_date, String post_img_cover,
                         String shop_id, String shop_name, String shop_address, String city, String state_code, String rating, String shop_img_logo, String shop_img_logo_thumb,
                         String writer_id, String writer_name, String writer_photo, String writer_photo_thumb) {
        this.post_id = post_id;
        this.post_type =post_type;
        this.post_ref_id = post_ref_id;
        this.post_title = post_title;
        this.post_stick_top = post_stick_top;
        this.post_highlight = post_highlight;
        this.review_score = review_score;
        this.review_price_rate = review_price_rate;
        this.author_name = author_name;
        this.author_url = author_url;

        this.author_domain = author_domain;
        this.created_timeago = created_timeago;
        this.created_date = created_date;
        this.modified_date = modified_date;
        this.post_img_cover = post_img_cover;

        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.shop_address = shop_address;
        this.city = city;
        this.state_code = state_code;
        this.rating = rating;
        this.shop_img_logo = shop_img_logo;
        this.shop_img_logo_thumb = shop_img_logo_thumb;

        this.writer_id = writer_id;
        this.writer_name = writer_name;
        this.writer_photo = writer_photo;
        this.writer_photo_thumb = writer_photo_thumb;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getPost_ref_id() {
        return post_ref_id;
    }

    public void setPost_ref_id(String post_ref_id) {
        this.post_ref_id = post_ref_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_stick_top() {
        return post_stick_top;
    }

    public void setPost_stick_top(String post_stick_top) {
        this.post_stick_top = post_stick_top;
    }

    public String getPost_highlight() {
        return post_highlight;
    }

    public void setPost_highlight(String post_highlight) {
        this.post_highlight = post_highlight;
    }

    public String getReview_score() {
        return review_score;
    }

    public void setReview_score(String review_score) {
        this.review_score = review_score;
    }

    public String getReview_price_rate() {
        return review_price_rate;
    }

    public void setReview_price_rate(String review_price_rate) {
        this.review_price_rate = review_price_rate;
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

    public String getPost_img_cover() {
        return post_img_cover;
    }

    public void setPost_img_cover(String post_img_cover) {
        this.post_img_cover = post_img_cover;
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

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getShop_img_logo() {
        return shop_img_logo;
    }

    public void setShop_img_logo(String shop_img_logo) {
        this.shop_img_logo = shop_img_logo;
    }

    public String getShop_img_logo_thumb() {
        return shop_img_logo_thumb;
    }

    public void setShop_img_logo_thumb(String shop_img_logo_thumb) {
        this.shop_img_logo_thumb = shop_img_logo_thumb;
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
}
