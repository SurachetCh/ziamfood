package revenue_express.ziamfood.model;

import io.realm.RealmObject;

/**
 * Created by surachet on 1/19/2017.
 */

public class User extends RealmObject {

    private Integer id;
    private String access_user_key,name,firstname,lastname,email,level,gender,photo_thumb,checkconnect;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccess_user_key() {
        return access_user_key;
    }

    public void setAccess_user_key(String access_user_key) {
        this.access_user_key = access_user_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto_thumb() {
        return photo_thumb;
    }

    public void setPhoto_thumb(String photo_thumb) {
        this.photo_thumb = photo_thumb;
    }

    public String getCheckconnect() {
        return checkconnect;
    }

    public void setCheckconnect(String checkconnect) {
        this.checkconnect = checkconnect;
    }
}
