package tax.nra.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Builder;

@Builder
@JacksonXmlRootElement(localName = "part3")
public class Part3 {

    @JacksonXmlProperty(localName = "issetapp1")
    public final int issetapp1 = 0;
    @JacksonXmlProperty(localName = "issetapp2")
    public final int issetapp2 = 0;
    @JacksonXmlProperty(localName = "issetapp3")
    public final int issetapp3 = 0;
    @JacksonXmlProperty(localName = "issetapp4")
    public final int issetapp4 = 0;

    @JacksonXmlProperty(localName = "issetapp5")
    public int issetapp5;

    @JacksonXmlProperty(localName = "issetapp6")
    public final int issetapp6 = 0;
    @JacksonXmlProperty(localName = "issetapp7")
    public final int issetapp7 = 0;

    @JacksonXmlProperty(localName = "issetapp8")
    public int issetapp8;

    @JacksonXmlProperty(localName = "issetapp9")
    public final int issetapp9 = 0;
    @JacksonXmlProperty(localName = "issetapp10")
    public final int issetapp10 = 0;
    @JacksonXmlProperty(localName = "issetapp11")
    public final int issetapp11 = 0;
    @JacksonXmlProperty(localName = "issetapp12")
    public final int issetapp12 = 0;
    @JacksonXmlProperty(localName = "issetapp13")
    public final int issetapp13 = 0;
    @JacksonXmlProperty(localName = "hasnoidoc")
    public final int hasnoidoc = 0;
    @JacksonXmlProperty(localName = "noidocs")
    public final Object noidocs = null;
    @JacksonXmlProperty(localName = "issetapp2005")
    public final int issetapp2005 = 0;
    @JacksonXmlProperty(localName = "issetapp2006")
    public final int issetapp2006 = 0;
    @JacksonXmlProperty(localName = "finnum")
    public final Object finnum = null;
    @JacksonXmlProperty(localName = "findata")
    public final Object findata = null;
    @JacksonXmlProperty(localName = "otherdocs")
    public final Object otherdocs = null;
    @JacksonXmlProperty(localName = "partstatus")
    public final int partstatus = 1;
}
