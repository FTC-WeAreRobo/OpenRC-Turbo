package org.firstinspires.ftc.teamcode.opmodes.tele.params;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.opmodes.auto.params.FieldConstants;

@Config
public class MechConstants {

    public static Pose2d StartingPose = new Pose2d(FieldConstants.RedRight.StartingPos, 0);
    /**
     * Set by Sequence
     * @see org.firstinspires.ftc.teamcode.opmodes.auto.sequence.Sequence#stop()
     */


    public static double DriveFullPower = 0.85;
    public static double DriveSlowPower = 0.25;

    public static double RPMGoal = 3800;
    public static double RPMGoalFromStack = 3600;
    public static double RPMPowerShot = 3500; //3400-3600?
    public static double PowerShotDelay = 250;

    @Config
    public static class Red {
        public static double PowerShotAngleIncrement = -9.5;
        public static double AutoShootLine = -24.5;
    }

    @Config
    public static class Blue {

    }



}
