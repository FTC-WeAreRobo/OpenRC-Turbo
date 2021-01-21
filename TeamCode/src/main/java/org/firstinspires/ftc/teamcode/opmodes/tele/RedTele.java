package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.auto.params.FieldConstants;

import java.util.Arrays;

import static org.firstinspires.ftc.teamcode.opmodes.tele.params.MechConstants.PowerShotDelay;
import static org.firstinspires.ftc.teamcode.opmodes.tele.params.MechConstants.RPMGoal;
import static org.firstinspires.ftc.teamcode.opmodes.tele.params.MechConstants.RPMPowerShot;
import static org.firstinspires.ftc.teamcode.opmodes.tele.params.MechConstants.Red;
import static org.firstinspires.ftc.teamcode.util.Sleep.sleep;

@TeleOp(name="RedTele", group="Iterative Opmode")
public class RedTele extends Tele {
    private int powerShotCount = 0;

    public RedTele() {
        super();
    }

    @Override
    protected void autoShoot() {
        autoShoot = true;
        if (drive.getPoseEstimate().getY() < Red.AutoShootLine) {
            //TODO: try turn and linear move
            shooter.spinUp(RPMGoal);
            drive.turn(FieldConstants.RedField.GoalShotPos.getHeading());
            Trajectory trajectory = drive.trajectoryBuilder(drive.getPoseEstimate())
                    .lineTo(new Vector2d(FieldConstants.RedField.GoalShotPos.getX(), FieldConstants.RedField.GoalShotPos.getY()),
                            new MinVelocityConstraint(Arrays.asList(
                                    drive.getMaxAngVelConstraint(),
                                    drive.getCustomVelConstraint(12)
                            )
                            ), drive.getMaxAccelConstraint())

                    .build();
            drive.followTrajectory(trajectory);
            shooter.shoot(3);
        }
        else {
            shooter.spinUp(RPMPowerShot);
            drive.turn(Math.toRadians(Red.PowerShotInitialAngle));
            Trajectory trajectory = drive.trajectoryBuilder(drive.getPoseEstimate())
                    .lineTo(FieldConstants.RedField.PowerShotPos)
                    .build();
            drive.followTrajectory(trajectory);

            //TODO: TEST THIS, finish powershot!
            Trajectory turn = drive.trajectoryBuilder(drive.getPoseEstimate())
                    .lineToLinearHeading(new Pose2d(
                            drive.getPoseEstimate().getX(), drive.getPoseEstimate().getY(),
                                    drive.getPoseEstimate().getHeading() + Math.toRadians(Red.PowerShotAngleIncrement))
                            ,
                            new MinVelocityConstraint(Arrays.asList(
                                    drive.getMaxAngVelConstraint(),
                                    drive.getCustomVelConstraint(7.5)
                            )
                            ), drive.getMaxAccelConstraint())
                    .build();

            for (int i = 0; i < 3; i++) {
                shooter.powerShot(RPMPowerShot);
                drive.followTrajectory(turn);
//                drive.turn(Math.toRadians(Red.PowerShotAngleIncrement));
                sleep(PowerShotDelay);
            }

            shooter.stop();
        }
        autoShoot = false;

    }

    @Override
    protected void powerShot() {
        autoShoot = true;

        for (int i = 0; i < 3; i++) {
            shooter.powerShot(RPMPowerShot);
            if (i < 2) drive.turn(Math.toRadians(Red.PowerShotAngleIncrement));
        }

        shooter.stop();

        autoShoot = false;
    }

    @Override
    protected void manShoot() {
        manShoot = true;
        if (drive.getPoseEstimate().getY() < Red.AutoShootLine) {
            shooter.shoot(3, RPMGoal);
            //TODO: make sure manShoot boolean works
        }
        else {
            powerShotCount++;
            shooter.powerShot(RPMPowerShot);
            if (powerShotCount >= 3) {
                shooter.stop();
                powerShotCount = 0;
            }
        }
    }
}