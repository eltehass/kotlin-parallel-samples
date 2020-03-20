package labWorks.lab8.data;

import javax.xml.bind.annotation.XmlElement;

public class CallItem {

    @XmlElement
    public String Type;

    @XmlElement
    public int Price;

    public CallItem() {
        this("", 0);
    }

    public CallItem(String type, int price) {
        Type = type;
        Price = price;
    }

    @Override
    public String toString() {
        return "CallItem{" +
                "Type=" + Type +
                ", Price=" + Price +
                '}';
    }

    enum CallType {
        This,
        Others,
        Stationary
    }

}
