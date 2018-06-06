package revenue_express.ziamfood.model;

/**
 * Created by surachet on 1/12/2017.
 */

public class OrderListDAO {
    private int id,id_menu,amount;
    private double price,total;
    private String title,addition,note;

    public OrderListDAO(Integer id, Integer id_menu, String title, Double price, Double total, Integer amount, String addition, String note) {
        this.id = id;
        this.id_menu = id_menu;
        this.title = title;
        this.price = price;
        this.total = total;
        this.amount = amount;
        this.addition = addition;
        this.note = note;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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
