package pl.imbestpumpkin.pumpkinNPCLib.Wrapper;

import com.comphenix.protocol.wrappers.EnumWrappers;

public enum WrappedEntityUseAction {
    INTERACT(EnumWrappers.EntityUseAction.INTERACT),
    ATTACK(EnumWrappers.EntityUseAction.ATTACK),
    INTERACT_AT(EnumWrappers.EntityUseAction.INTERACT_AT);

    private final EnumWrappers.EntityUseAction protocolAction;

    private WrappedEntityUseAction(EnumWrappers.EntityUseAction protocolAction) {
        this.protocolAction = protocolAction;
    }

    public EnumWrappers.EntityUseAction toProtocolAction() {
        return protocolAction;
    }

    public static WrappedEntityUseAction fromProtocolAction(EnumWrappers.EntityUseAction protocolAction) {
        for (WrappedEntityUseAction action : values()) {
            if (action.toProtocolAction().equals(protocolAction)) {
                return action;
            }
        }
        return null;
    }
}
