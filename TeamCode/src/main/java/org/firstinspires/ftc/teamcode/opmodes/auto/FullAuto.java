package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


import org.firstinspires.ftc.teamcode.opmodes.auto.sequence.RedRightSequence;
import org.firstinspires.ftc.teamcode.opmodes.auto.sequence.Sequence;
import org.firstinspires.ftc.teamcode.robot.ControllerManager;
import org.firstinspires.ftc.teamcode.robot.camera.CameraController;
import org.firstinspires.ftc.teamcode.robot.drive.DriveLocalizationController;

import java.util.HashMap;
import java.util.Map;

@Autonomous(name="fullAuto", group="Iterative Opmode")
public class FullAuto extends OpMode {

    // Maps case-insensitive name to a sequence
    private Map<String, Sequence> sequences = new HashMap<>();
    private ControllerManager controllers;
    private String strNumRings;
    private int ringCount;
    private Sequence currentSequence;
    protected final static Object lock = new Object();

    @Override
    public void init() {
        telemetry.addLine("Initializing...");

        controllers = new ControllerManager(telemetry);
        controllers.add(Constants.Camera, new CameraController(hardwareMap, telemetry));
        controllers.add(Constants.Drive, new DriveLocalizationController(hardwareMap, telemetry));
        controllers.init();

        makeSequences();

        telemetry.addLine("Initialized");
    }

    @Override
    public void init_loop() {
        synchronized (lock) {
            CameraController camera = controllers.get(CameraController.class, Constants.Camera);
            if (camera != null) {
                strNumRings = camera.rankRings();
                telemetry.addLine(strNumRings);
            }
        }
    }

    @Override
    public void start() { //code to run once when play is hit
        synchronized (lock) {
            CameraController camera = controllers.get(CameraController.class, Constants.Camera);
            if (camera != null) {
                ringCount = camera.ringsToInt(strNumRings);
                telemetry.addData("Rings: ", ringCount);
            }

            controllers.start(); //stop camera instance

            // TODO: use different opmodes for alliance, side. For now, we are assuming Red Alliance, Left side:
            String sequenceName = makeSequenceName(Constants.RedAlliance, Constants.LeftSide);
            currentSequence = getSequence(sequenceName);

            if (currentSequence == null) {
                telemetry.addLine("No sequence found!");
                return;
            }

            try {
                telemetry.addLine("Initializing sequence: " + getSequenceName(currentSequence));
                currentSequence.init(ringCount);

                telemetry.addLine("Executing sequence: " + getSequenceName(currentSequence));

                // execute runs async
                currentSequence.execute();
            } catch (Exception e) {
                telemetry.addLine("Exception while executing sequence: " + e.toString());
            }
        }
    }

    @Override
    public void loop() {
    }

    @Override
    public void stop() {
        synchronized (lock) {
            currentSequence.stop();
            controllers.stop();
        }
    }

    private void makeSequences() {
        synchronized (lock) {
            sequences.put(makeSequenceName(
                    Constants.RedAlliance, Constants.LeftSide),
                    new RedRightSequence(controllers, telemetry));

            sequences.put(makeSequenceName(
                    Constants.RedAlliance, Constants.RightSide),
                    new RedRightSequence(controllers, telemetry));

            sequences.put(makeSequenceName(
                    Constants.BlueAlliance, Constants.LeftSide),
                    new RedRightSequence(controllers, telemetry));

            sequences.put(makeSequenceName(
                    Constants.BlueAlliance, Constants.RightSide),
                    new RedRightSequence(controllers, telemetry));
        }
    }

    private String makeSequenceName(String alliance, String side) {
        return (alliance + side).toLowerCase();
    }

    private Sequence getSequence(String name) {
        return sequences.get(name.toLowerCase());
    }

    private String getSequenceName(Sequence sequence){
        return sequence.getClass().getSimpleName();
    }
}
