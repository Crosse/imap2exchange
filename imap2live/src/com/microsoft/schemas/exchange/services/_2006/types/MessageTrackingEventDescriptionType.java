
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageTrackingEventDescriptionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageTrackingEventDescriptionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Submitted"/>
 *     &lt;enumeration value="Resolved"/>
 *     &lt;enumeration value="Expanded"/>
 *     &lt;enumeration value="Delivered"/>
 *     &lt;enumeration value="MovedToFolderByInboxRule"/>
 *     &lt;enumeration value="RulesCc"/>
 *     &lt;enumeration value="FailedGeneral"/>
 *     &lt;enumeration value="FailedModeration"/>
 *     &lt;enumeration value="FailedTransportRules"/>
 *     &lt;enumeration value="SmtpSend"/>
 *     &lt;enumeration value="SmtpSendCrossSite"/>
 *     &lt;enumeration value="SmtpSendCrossForest"/>
 *     &lt;enumeration value="SmtpReceive"/>
 *     &lt;enumeration value="Forwarded"/>
 *     &lt;enumeration value="Pending"/>
 *     &lt;enumeration value="PendingModeration"/>
 *     &lt;enumeration value="ApprovedModeration"/>
 *     &lt;enumeration value="QueueRetry"/>
 *     &lt;enumeration value="QueueRetryNoRetryTime"/>
 *     &lt;enumeration value="MessageDefer"/>
 *     &lt;enumeration value="TransferredToForeignOrg"/>
 *     &lt;enumeration value="TransferredToPartnerOrg"/>
 *     &lt;enumeration value="TransferredToLegacyExchangeServer"/>
 *     &lt;enumeration value="DelayedAfterTransferToPartnerOrg"/>
 *     &lt;enumeration value="Read"/>
 *     &lt;enumeration value="NotRead"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MessageTrackingEventDescriptionType")
@XmlEnum
public enum MessageTrackingEventDescriptionType {

    @XmlEnumValue("Submitted")
    SUBMITTED("Submitted"),
    @XmlEnumValue("Resolved")
    RESOLVED("Resolved"),
    @XmlEnumValue("Expanded")
    EXPANDED("Expanded"),
    @XmlEnumValue("Delivered")
    DELIVERED("Delivered"),
    @XmlEnumValue("MovedToFolderByInboxRule")
    MOVED_TO_FOLDER_BY_INBOX_RULE("MovedToFolderByInboxRule"),
    @XmlEnumValue("RulesCc")
    RULES_CC("RulesCc"),
    @XmlEnumValue("FailedGeneral")
    FAILED_GENERAL("FailedGeneral"),
    @XmlEnumValue("FailedModeration")
    FAILED_MODERATION("FailedModeration"),
    @XmlEnumValue("FailedTransportRules")
    FAILED_TRANSPORT_RULES("FailedTransportRules"),
    @XmlEnumValue("SmtpSend")
    SMTP_SEND("SmtpSend"),
    @XmlEnumValue("SmtpSendCrossSite")
    SMTP_SEND_CROSS_SITE("SmtpSendCrossSite"),
    @XmlEnumValue("SmtpSendCrossForest")
    SMTP_SEND_CROSS_FOREST("SmtpSendCrossForest"),
    @XmlEnumValue("SmtpReceive")
    SMTP_RECEIVE("SmtpReceive"),
    @XmlEnumValue("Forwarded")
    FORWARDED("Forwarded"),
    @XmlEnumValue("Pending")
    PENDING("Pending"),
    @XmlEnumValue("PendingModeration")
    PENDING_MODERATION("PendingModeration"),
    @XmlEnumValue("ApprovedModeration")
    APPROVED_MODERATION("ApprovedModeration"),
    @XmlEnumValue("QueueRetry")
    QUEUE_RETRY("QueueRetry"),
    @XmlEnumValue("QueueRetryNoRetryTime")
    QUEUE_RETRY_NO_RETRY_TIME("QueueRetryNoRetryTime"),
    @XmlEnumValue("MessageDefer")
    MESSAGE_DEFER("MessageDefer"),
    @XmlEnumValue("TransferredToForeignOrg")
    TRANSFERRED_TO_FOREIGN_ORG("TransferredToForeignOrg"),
    @XmlEnumValue("TransferredToPartnerOrg")
    TRANSFERRED_TO_PARTNER_ORG("TransferredToPartnerOrg"),
    @XmlEnumValue("TransferredToLegacyExchangeServer")
    TRANSFERRED_TO_LEGACY_EXCHANGE_SERVER("TransferredToLegacyExchangeServer"),
    @XmlEnumValue("DelayedAfterTransferToPartnerOrg")
    DELAYED_AFTER_TRANSFER_TO_PARTNER_ORG("DelayedAfterTransferToPartnerOrg"),
    @XmlEnumValue("Read")
    READ("Read"),
    @XmlEnumValue("NotRead")
    NOT_READ("NotRead");
    private final String value;

    MessageTrackingEventDescriptionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageTrackingEventDescriptionType fromValue(String v) {
        for (MessageTrackingEventDescriptionType c: MessageTrackingEventDescriptionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
