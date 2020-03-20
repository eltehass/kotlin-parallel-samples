package labWorks.lab8.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Tariffs {

    @XmlElement
    public List<Tariff> tariffElement;

    public Tariffs() {
        this(null);
    }

    public Tariffs(List<Tariff> tariffElement) {
        this.tariffElement = tariffElement;
    }

    @Override
    public String toString() {
        return "Tariffs{" +
                "tariffElement=" + tariffElement +
                '}';
    }
}
