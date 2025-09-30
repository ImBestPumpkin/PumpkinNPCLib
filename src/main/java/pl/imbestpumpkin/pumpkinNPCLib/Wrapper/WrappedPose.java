package pl.imbestpumpkin.pumpkinNPCLib.Wrapper;

import net.minecraft.world.entity.Pose;

public enum WrappedPose {
    STANDING(0, Pose.STANDING),
    FALL_FLYING(1, Pose.FALL_FLYING),
    SLEEPING(2, Pose.SLEEPING),
    SWIMMING(3, Pose.SWIMMING),
    SPIN_ATTACK(4, Pose.SPIN_ATTACK),
    CROUCHING(5, Pose.CROUCHING),
    LONG_JUMPING(6, Pose.LONG_JUMPING),
    DYING(7, Pose.DYING),
    CROAKING(8, Pose.CROAKING),
    USING_TONGUE(9, Pose.USING_TONGUE),
    SITTING(10, Pose.SITTING),
    ROARING(11, Pose.ROARING),
    SNIFFING(12, Pose.SNIFFING),
    EMERGING(13, Pose.EMERGING),
    DIGGING(14, Pose.DIGGING),
    SLIDING(15, Pose.SLIDING),
    SHOOTING(16, Pose.SHOOTING),
    INHALING(17, Pose.INHALING);

    private final int id;
    private final Pose vanillaPose;

    private WrappedPose(int id, Pose vanillaPose) {
        this.id = id;
        this.vanillaPose = vanillaPose;
    }

    public int id() {
        return this.id;
    }

    public Pose toVanilla() {
        return vanillaPose;
    }
}
