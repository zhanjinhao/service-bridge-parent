package sb.rest.springcloud;

import java.io.Serializable;
import java.util.Date;

public class OrderDetail implements Serializable {

    private String name;
    private Date date;
    private Double price;
    private String buyer;
    private String seller;

    public OrderDetail(String name, Date date, Double price, String buyer, String seller) {
        this.name = name;
        this.date = date;
        this.price = price;
        this.buyer = buyer;
        this.seller = seller;
    }

    public OrderDetail() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", price=" + price +
                ", buyer='" + buyer + '\'' +
                ", seller='" + seller + '\'' +
                '}';
    }
}