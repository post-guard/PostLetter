package top.rrricardo.postletter.controllers;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import top.rrricardo.postletter.PostLetter;
import top.rrricardo.postletter.utils.ControllerBase;
import top.rrricardo.postletter.utils.SceneManager;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FaceRecognitionController implements ControllerBase{
    @FXML
    private ImageView screen;

    @FXML
    private Button openCameraButton;

    private final VideoCapture videoCapture = new VideoCapture();
    private static final CascadeClassifier faceClassifier = new CascadeClassifier(
            PostLetter.class.getResource("haarcascade_frontalface_default.xml").getPath());

    private ScheduledExecutorService executor;
    private boolean isCameraOpen;

    /**
     * 点击返回，回到账号密码登录页面
     */
    @FXML
    protected void onReturnClick() throws IOException {
        SceneManager.replaceScene("log-view.fxml", 390, 430, "登录");
    }

    @FXML
    protected void onOpenCameraClick() {
        if (isCameraOpen) {
            isCameraOpen = false;

            stopExecutor();

        } else {

            videoCapture.open(0);

            if (videoCapture.isOpened()) {
                isCameraOpen = true;

                /*executor = Executors.newSingleThreadScheduledExecutor();
                executor.scheduleAtFixedRate(() -> {
                    var frame = getFrame();
                    var face = getFaceInImage(frame);

                    if (face == null) {
                        System.out.println("获得脸部失败");
                    } else {
                        frame = face;
                        System.out.println("获得脸部成功");
                    }
                    var image = mat2Image(frame);

                    var test = Imgcodecs.imread("test.png");


                    if (image != null) {
                        Platform.runLater(() -> screen.setImage(image));
                    }

                }, 0, 33, TimeUnit.MILLISECONDS);*/
                var thread = new Thread(() -> {
                    var frame = getFrame();
                    var image = mat2Image(frame);

                    if (image != null) {
                        Image finalImage = image;
                        Platform.runLater(() -> screen.setImage(finalImage));
                    }

                    System.out.println("尝试获得脸部");
                    var face = getFaceInImage(frame);

                    if (face != null) {
                        image = mat2Image(face);
                        if (image != null) {
                            Image finalImage1 = image;
                            Platform.runLater(() -> screen.setImage(finalImage1));
                        }
                    } else {
                        System.out.println("获得脸部失败");
                    }
                });

                thread.start();
            } else {
                System.out.println("Open camera failed!");
            }
        }
    }

    @Override
    public void close() {
        stopExecutor();
    }

    private void stopExecutor() {
        if ( executor != null && !executor.isShutdown()) {
            try {
                executor.shutdown();
                executor.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (videoCapture.isOpened()) {
            videoCapture.release();
        }
    }

    @NotNull
    private Mat getFrame()
    {
        var frame = new Mat();

        if (videoCapture.isOpened())
        {
            try {
                videoCapture.read(frame);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return frame;
    }

    @Nullable
    private static Image mat2Image(@Nullable Mat frame) {
        if (frame == null) {
            return null;
        }

        BufferedImage image;
        var width = frame.width();
        var height = frame.height();
        var channels = frame.channels();
        var sourcePixels = new byte[width * height * channels];
        frame.get(0, 0, sourcePixels);

        if (channels > 1) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final var pixels = ((DataBufferByte)(image.getRaster().getDataBuffer())).getData();
        System.arraycopy(sourcePixels, 0, pixels, 0, sourcePixels.length);

        try {
            return SwingFXUtils.toFXImage(image, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static double compareFace(Mat a, Mat b) {
        // 颜色范围
        var ranges = new MatOfFloat(0f, 256f);
        // 直方图大小
        // 越大识别的准确度越高
        var histSize = new MatOfInt(1000000);

        var histA = new Mat();
        var histB = new Mat();

        Imgproc.calcHist(Collections.singletonList(a), new MatOfInt(0), new Mat(), histA, histSize, ranges);
        Imgproc.calcHist(Collections.singletonList(b), new MatOfInt(0), new Mat(), histB, histSize, ranges);

        return Imgproc.compareHist(histA, histB, Imgproc.CV_COMP_CORREL);
    }

    @Nullable
    private static Mat getFaceInImage(Mat input) {
        var output = new Mat();
        // 灰度化图像
        Imgproc.cvtColor(input, output, Imgproc.COLOR_BGR2GRAY);

        // 探测人脸
        var faceRect = new MatOfRect();
        faceClassifier.detectMultiScale(output, faceRect);

        Mat face = null;
        for (var rect : faceRect.toArray()) {
            face = new Mat(input, rect);
        }

        return face;
    }

}
