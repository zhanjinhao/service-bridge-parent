package sb.rpc.sofarpc;

import sb.rpc.dubbo.OrderDetail;

import java.io.Serializable;
import java.util.Date;

public class ResellOrderDetail implements Serializable {

    private OrderDetail testPojo = new OrderDetail();

    private Double price;

    private Date date;

    private String buyer;

     public ResellOrderDetail( Double price, Date date, String buyer) {
          this.price = price;
          this.date = date;
          this.buyer = buyer;
     }

     public Double getPrice() {
          return price;
     }

     public void setPrice(Double price) {
          this.price = price;
     }

     public Date getDate() {
          return date;
     }

     public void setDate(Date date) {
          this.date = date;
     }

     public String getBuyer() {
          return buyer;
     }

     public void setBuyer(String buyer) {
          this.buyer = buyer;
     }


    @Override
    public String toString() {
        return "ResellOrderDetail{" +
                "testPojo=" + testPojo +
                ", price=" + price +
                ", date=" + date +
                ", buyer='" + buyer + '\'' +
                '}';
    }
}
