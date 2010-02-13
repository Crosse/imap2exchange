
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageTrackingScopeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageTrackingScopeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Organization"/>
 *     &lt;enumeration value="Forest"/>
 *     &lt;enumeration value="Site"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MessageTrackingScopeType")
@XmlEnum
public enum MessageTrackingScopeType {

    @XmlEnumValue("Organization")
    ORGANIZATION("Organization"),
    @XmlEnumValue("Forest")
    FOREST("Forest"),
    @XmlEnumValue("Site")
    SITE("Site");
    private final String value;

    MessageTrackingScopeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageTrackingScopeType fromValue(String v) {
        for (MessageTrackingScopeType c: MessageTrackingScopeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
