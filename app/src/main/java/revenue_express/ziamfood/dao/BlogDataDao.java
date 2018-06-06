package revenue_express.ziamfood.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class BlogDataDao {

    String ref_id,shop_name,author_name,author_domain,created_timeago,writer_name,title,highlight,img_cover,writer_photo;


    public BlogDataDao(String ref_id, String shop_name, String author_name, String author_domain, String created_timeago, String writer_name, String title, String highlight, String img_cover, String writer_photo ) {
        this.ref_id = ref_id;
        this.shop_name =shop_name;
        this.author_name = author_name;
        this.author_domain = author_domain;
        this.created_timeago = created_timeago;
        this.writer_name = writer_name;

        this.title = title;
        this.highlight = highlight;
        this.img_cover = img_cover;
        this.writer_photo = writer_photo;

    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
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

    public String getWriter_name() {
        return writer_name;
    }

    public void setWriter_name(String writer_name) {
        this.writer_name = writer_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getImg_cover() {
        return img_cover;
    }

    public void setImg_cover(String img_cover) {
        this.img_cover = img_cover;
    }

    public String getWriter_photo() {
        return writer_photo;
    }

    public void setWriter_photo(String writer_photo) {
        this.writer_photo = writer_photo;
    }
}
