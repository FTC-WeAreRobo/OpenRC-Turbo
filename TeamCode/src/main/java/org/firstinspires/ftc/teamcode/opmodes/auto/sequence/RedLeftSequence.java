package org.firstinspires.ftc.teamcode.opmodes.auto.sequence;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmodes.auto.Constants;
import org.firstinspires.ftc.teamcode.robot.ControllerManager;

public class RedLeftSequence extends Sequence {

    public RedLeftSequence(int ringCount, ControllerManager controllers, HardwareMap hwMap, Telemetry tel){
        super(ringCount, controllers, hwMap, tel);
    }

    @Override
    public void init() {
        Pose2d startPose = Constants.RedLeft.startingPose; // TODO: rotate 180?

        super.init(startPose);

        makeActions();
    }

    protected void makeActions() {
        switch (ringCount) {
            case 0:
                actions.addAction(() -> moveToSquares());
                break;
            case 1:
                actions.addAction(() -> moveToSquares());
                break;
            case 4:
                actions.addAction(() -> moveToSquares());
                break;
        }
    }
}
