package mvc.JavaBean;

public class CartItem {
    private Cake cake;//蛋糕实体
    private int cakenum;//蛋糕数量
    private int price;//该购物项的总价

    public Cake getCake() {
        return cake;
    }

    public void setCake(Cake cake) {
        this.cake = cake;
    }

    public int getPrice() {
        int price=0;
        price = cake.getCake_price()*cakenum;
        return price;
    }

    public int getCakenum() {
        return cakenum;
    }

    public void setCakenum(int cakenum) {
        this.cakenum = cakenum;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
