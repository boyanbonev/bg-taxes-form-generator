package tax.nra.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Builder;
import lombok.ToString;
import lombok.ToString.Exclude;

/**
 * Based on https://nra.bg/wps/portal/nra/documents/documents_priority/7cce6e29-6159-4b05-a1a1-71d8316355cd
 */
@ToString
@Builder
@JacksonXmlRootElement(localName = "dec50")
public class Declaration {

    @JacksonXmlProperty(localName = "part1")
    @Exclude
    private final Object part1 = null;

    @JacksonXmlProperty(localName = "part3")
    @Exclude
    private Part3 part3;

    @JacksonXmlProperty(localName = "part4")
    @Exclude
    private final Object part4 = null;

    @JacksonXmlProperty(localName = "part5")
    @Exclude
    private final Object part5 = null;

    @JacksonXmlProperty(localName = "app5")
    private Appendix5 appendix5;

    @JacksonXmlProperty(localName = "app8")
    private Appendix8 appendix8;
}
