package mvc.JavaBean;

public class Cake {
    private String cake_name;
    private String cake_class;
    private int cake_price;
    private int cake_num;
    private String cake_time;
    private String introduce;//介绍
    private String img;//图片链接

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCake_name() {
        return cake_name;
    }

    public void setCake_name(String cake_name) {
        this.cake_name = cake_name;
    }

    public String getCake_class() {
        return cake_class;
    }

    public void setCake_class(String cake_class) {
        this.cake_class = cake_class;
    }

    public int getCake_price() {
        return cake_price;
    }

    public void setCake_price(int cake_price) {
        this.cake_price = cake_price;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getCake_num() {
        return cake_num;
    }

    public void setCake_num(int cake_num) {
        this.cake_num = cake_num;
    }

    public String getCake_time() {
        return cake_time;
    }

    public void setCake_time(String cake_time) {
        this.cake_time = cake_time;
    }
}
