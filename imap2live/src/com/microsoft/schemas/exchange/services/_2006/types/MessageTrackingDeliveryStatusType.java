
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageTrackingDeliveryStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageTrackingDeliveryStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Unsuccessful"/>
 *     &lt;enumeration value="Pending"/>
 *     &lt;enumeration value="Delivered"/>
 *     &lt;enumeration value="Transferred"/>
 *     &lt;enumeration value="Read"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MessageTrackingDeliveryStatusType")
@XmlEnum
public enum MessageTrackingDeliveryStatusType {

    @XmlEnumValue("Unsuccessful")
    UNSUCCESSFUL("Unsuccessful"),
    @XmlEnumValue("Pending")
    PENDING("Pending"),
    @XmlEnumValue("Delivered")
    DELIVERED("Delivered"),
    @XmlEnumValue("Transferred")
    TRANSFERRED("Transferred"),
    @XmlEnumValue("Read")
    READ("Read");
    private final String value;

    MessageTrackingDeliveryStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageTrackingDeliveryStatusType fromValue(String v) {
        for (MessageTrackingDeliveryStatusType c: MessageTrackingDeliveryStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
