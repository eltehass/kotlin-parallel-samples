package labWorks.lab8.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Tariff {

    @XmlAttribute
    public String id;

    @XmlElement
    public String name;

    @XmlElement
    public String operatorName;

    @XmlElement
    public int payroll;

    @XmlElement
    public int smsPrice;

    @XmlElement
    public CallPrices callPrices;

    @XmlElement
    public Parameters parameters;

    public Tariff() {
        this("", "","",0,0,null,null);
    }

    public Tariff(String name, String operatorName, int payroll, int smsPrice, CallPrices callPrices, Parameters parameters) {
        this.id = "_4327c0d60046c109";
        this.name = name;
        this.operatorName = operatorName;
        this.payroll = payroll;
        this.smsPrice = smsPrice;
        this.callPrices = callPrices;
        this.parameters = parameters;
    }

    public Tariff(String id, String name, String operatorName, int payroll, int smsPrice, CallPrices callPrices, Parameters parameters) {
        this.id = id;
        this.name = name;
        this.operatorName = operatorName;
        this.payroll = payroll;
        this.smsPrice = smsPrice;
        this.callPrices = callPrices;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "Tariff{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", payroll=" + payroll +
                ", smsPrice=" + smsPrice +
                ", callPrices=" + callPrices +
                ", parameters=" + parameters +
                '}';
    }
}