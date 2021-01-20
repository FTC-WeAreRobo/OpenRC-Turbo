package org.firstinspires.ftc.teamcode.robot.camera.algorithms;

import com.acmerobotics.dashboard.FtcDashboard;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.robot.Controller;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class OpenCVInterface implements Controller{

    private OpenCvCamera openCvPassthrough;
    private Telemetry telemetry;
    private RingDetector ContourRingDetector;

    public OpenCVInterface(VuforiaLocalizer vuforia, VuforiaLocalizer.Parameters paramaters, int[] viewportContainerIds, Telemetry telemetry) {
        this.telemetry = telemetry;

        // Create a Vuforia passthrough "virtual camera"
        openCvPassthrough = OpenCvCameraFactory.getInstance().createVuforiaPassthrough(vuforia, paramaters, viewportContainerIds[1]);

        openCvPassthrough.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Using GPU acceleration can be particularly helpful when using Vuforia passthrough
                // mode, because Vuforia often chooses high resolutions (such as 720p) which can be
                // very CPU-taxing to rotate in software. GPU acceleration has been observed to cause
                // issues on some devices, though, so if you experience issues you may wish to disable it.
                //TODO: Test GPU Acceleration
                openCvPassthrough.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                openCvPassthrough.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
                openCvPassthrough.setPipeline(new RingDetectorKt(telemetry, true));

                // We don't get to choose resolution, unfortunately. The width and height parameters
                // are entirely ignored when using Vuforia passthrough mode. However, they are left
                // in the method signature to provide interface compatibility with the other types
                // of cameras.
                openCvPassthrough.startStreaming(0,0, OpenCvCameraRotation.UPRIGHT);
                FtcDashboard.getInstance().startCameraStream(openCvPassthrough, 0);
            }
        });
    }

    public int getRingCount() {
        return ContourRingDetector.getRingCount();
    }

    public String getRingCountStr() {
        return ContourRingDetector.getRingCountStr();
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
//        openCvPassthrough.stopStreaming(); //TODO: this? or stop camera async
        FtcDashboard.getInstance().stopCameraStream();
    }


}
