package mvc.JavaBean;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> list = new ArrayList<>();
    private int price;//总价

    public List<CartItem> getList() {
        return list;
    }

    public void setList(List<CartItem> list) {
        this.list = list;
    }

    public int getPrice() {
        int price=0;
        for(CartItem item:list){
            price +=item.getPrice();
        }
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isList(){
        return !(list==null||list.size()==0);//为空则返回false
    }

    public int getNum(){
        int num = 0;
        for(CartItem item:list){
            num += item.getCakenum();
        }
        return num;
    }
}
