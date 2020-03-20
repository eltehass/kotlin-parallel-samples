package labWorks.lab8.data;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class CallPrices {

    @XmlElement
    public List<CallItem> item;

    public CallPrices() {
        this(new ArrayList<>());
    }

    public CallPrices(List<CallItem> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "CallPrice{" +
                "item=" + item +
                '}';
    }

}