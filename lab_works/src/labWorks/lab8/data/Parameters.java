package labWorks.lab8.data;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class Parameters {

    @XmlElement
    public List<ParameterItem> item;

    public Parameters() {
        this(new ArrayList<>());
    }

    public Parameters(List<ParameterItem> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "item=" + item +
                '}';
    }
}