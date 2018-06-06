package revenue_express.ziamfood.dao;

/**
 * Created by ichanpaka on 1/16/2017 AD.
 */

public class SumOrderListDataDao {

    String title,addition,note;
    Double price;
    Integer id,id_menu,amount;

    public SumOrderListDataDao(int id , int id_menu, String title, Double price, Integer amount, String addition, String note) {
        this.id = id;
        this.id_menu = id_menu;
        this.title = title;
        this.price = price;
        this.amount = amount;
        this.addition = addition;
        this.note = note;
    }

    public SumOrderListDataDao() {

    }

    public int getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
