package labWorks.lab8.data;

import javax.xml.bind.annotation.XmlElement;

public class ParameterItem {

    @XmlElement
    public String Type;

    @XmlElement
    public int Price;

    public ParameterItem() {
        this("", 0);
    }

    public ParameterItem(String type, int price) {
        Type = type;
        Price = price;
    }

    @Override
    public String toString() {
        return "ParameterItem{" +
                "Type='" + Type + '\'' +
                ", Price=" + Price +
                '}';
    }
}
