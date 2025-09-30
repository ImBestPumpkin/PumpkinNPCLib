package pl.imbestpumpkin.pumpkinNPCLib.Wrapper;

import com.comphenix.protocol.wrappers.EnumWrappers;

public enum WrappedHand {
    MAIN_HAND(EnumWrappers.Hand.MAIN_HAND),
    OFF_HAND(EnumWrappers.Hand.OFF_HAND);

    private final EnumWrappers.Hand protocolHand;

    private WrappedHand(EnumWrappers.Hand protocolHand) {
        this.protocolHand = protocolHand;
    }

    public EnumWrappers.Hand toProtocolHand() {
        return protocolHand;
    }

    public static WrappedHand fromProtocolHand(EnumWrappers.Hand protocolHand) {
        for (WrappedHand hand : values()) {
            if (hand.toProtocolHand().equals(protocolHand)) {
                return hand;
            }
        }
        return null;
    }
}
