package main.sample;

import main.neural.ImageConverter;
import main.neural.PictureAccessor;
import main.neural.Thresholds;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GUIMain {
    static int width = 60;
    static int height = 60;
    static string networkFilePath = "rockpaperscissors.eg";

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        GUIMain guiMain = new GUIMain();
        guiMain.startGUI();
    }

    static int counter = 0;
    Thresholds thresholds;
    PictureAccessor pictureAccessor;
    ImageConverter imageConverter;

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that realizes the video capture
    private VideoCapture capture = new VideoCapture();
    // a flag to change the button behavior
    private boolean cameraActive = false;
    static HashMap<Integer, String> reverseMap = new HashMap<Integer, String>();
    JFrame frame;
    JButton btn_play;
    ImageIcon currentFrame;
    ImageIcon userPic;
    JLabel label;
    JLabel userScore;
    JLabel vaScore;
    JScrollBar scrolHueLowerBoundLower;
    JScrollBar scrolHueLowerBoundUpper;
    JScrollBar scrolHueUperBoundLower;
    JScrollBar scrolHueUperBoundUpper;
    JScrollBar scrolSatLow;
    JScrollBar scrolSatUp;

    JLabel labelHueLowerBoundLower;
    JLabel labelHueLowerBoundUpper;
    JLabel labelHueUpperBoundLower;
    JLabel labelHueUpperBoundUpper;
    JLabel labelSatLow;
    JLabel labelSatUp;
    int hueLowLowerBound;
    int hueLowUpperBound;
    int hueUpLowerBound;
    int hueUpUpperBound;
    int satLowLowerBound;
    int satUpUpperBound;
    int userS;
    int vaS;
    String vaChoice;
    JLabel vaMovePic;
    JLabel vaMoveLabel;
    JLabel userMoveLabel;
    JScrollPane scrollPane;

    String predictedAction;
    BufferedImage blackAndWhite;
    static Game game;
    JLabel round;
    BasicNetwork network;
    // Method to run GUI
    public GUIMain() {
        frame = new JFrame("Rock Paper Scissors");
        frame.setLayout(new BorderLayout());
        btn_play = new JButton("Play");
        hueLowLowerBound = 25;
        hueLowUpperBound = 1200;
        hueUpLowerBound = 800;
        hueUpUpperBound = 1100;
        satLowLowerBound = 1;
        satUpUpperBound = 10;

        scrolHueLowerBoundLower = new JScrollBar(Adjustable.HORIZONTAL, hueLowLowerBound, 1, hueLowLowerBound, hueLowUpperBound);
        scrolHueLowerBoundUpper = new JScrollBar(Adjustable.HORIZONTAL, hueLowLowerBound, 1, hueLowLowerBound, hueLowUpperBound);
        scrolHueUperBoundLower = new JScrollBar(Adjustable.HORIZONTAL, hueUpLowerBound, 1, hueUpLowerBound, hueUpUpperBound);
        scrolHueUperBoundUpper = new JScrollBar(Adjustable.HORIZONTAL, hueUpLowerBound, 1, hueUpLowerBound, hueUpUpperBound);

        scrolSatUp = new JScrollBar(Adjustable.HORIZONTAL, satLowLowerBound, 1, satLowLowerBound, satUpUpperBound);
        scrolSatLow = new JScrollBar(Adjustable.HORIZONTAL, satLowLowerBound, 1, satLowLowerBound, satUpUpperBound);
        labelHueLowerBoundLower = new JLabel("   Hue(LowerBoundLower): ");
        labelHueLowerBoundUpper = new JLabel("   Hue(LowerBoundUpper): ");
        labelHueUpperBoundLower = new JLabel("   Hue(UpperBoundLower): ");
        labelHueUpperBoundUpper = new JLabel("   Hue(UpperBoundUpper): ");
        labelSatLow = new JLabel("   Saturation(LowerBound):");
        labelSatUp = new JLabel("   Saturation(UpperBound):");
        thresholds = new Thresholds((double) hueLowLowerBound / 100000, (double) hueLowUpperBound / 100000, (double) hueUpLowerBound / 10000, (double) hueUpUpperBound / 10000, (double) satLowLowerBound / 100, (double) satUpUpperBound / 100);
        pictureAccessor = new PictureAccessor(thresholds);
        imageConverter = new ImageConverter(thresholds);

        label = new JLabel();
        label.setFont(label.getFont().deriveFont(30.0f));
        round = new JLabel();
        game = new Game();
        Agent agent = new Agent();

        JPanel jPanelWest = new JPanel(new BorderLayout());
        JPanel jPanelSubWest = new JPanel(new FlowLayout());
        Panel jPanelSouth = new Panel(new BorderLayout());
        JPanel jPanelnorth = new JPanel();
        JPanel jpanelSubSouth = new JPanel(new GridLayout(6, 2));
        JPanel jpanelEast = new JPanel(new BorderLayout());
        JPanel jpanelSubEast = new JPanel(new GridLayout(2, 2));

        jpanelSubSouth.add(scrolHueLowerBoundLower);
        jpanelSubSouth.add(labelHueLowerBoundLower);
        jpanelSubSouth.add(scrolHueLowerBoundUpper);
        jpanelSubSouth.add(labelHueLowerBoundUpper);

        jpanelSubSouth.add(scrolHueUperBoundLower);
        jpanelSubSouth.add(labelHueUpperBoundLower);
        jpanelSubSouth.add(scrolHueUperBoundUpper);
        jpanelSubSouth.add(labelHueUpperBoundUpper);

        jpanelSubSouth.add(scrolSatLow);
        jpanelSubSouth.add(labelSatLow);

        jpanelSubSouth.add(scrolSatUp);
        jpanelSubSouth.add(labelSatUp);

        scrollPane = new JScrollPane(jpanelSubSouth);
        jPanelSouth.add(scrollPane, BorderLayout.CENTER);
        jPanelSouth.add(btn_play, BorderLayout.NORTH);

        userScore = new JLabel("User Player: " + userS);
        vaScore = new JLabel("Virtual Agent Score: " + vaS);
        currentFrame = new ImageIcon();
        userPic = new ImageIcon();
        JLabel video = new JLabel("", currentFrame, JLabel.CENTER);
        JLabel userLabelPic = new JLabel("", userPic, JLabel.CENTER);
        vaMovePic = new JLabel();
        userMoveLabel = new JLabel();
        vaMoveLabel = new JLabel();

        jPanelSubWest.add(vaScore);
        jPanelSubWest.add(userScore);
        jPanelWest.add(jPanelSubWest, BorderLayout.SOUTH);
        jPanelWest.add(video, BorderLayout.CENTER);

        jpanelSubEast.add(userLabelPic);
        jpanelSubEast.add(vaMovePic);
        jpanelSubEast.add(userMoveLabel);
        jpanelSubEast.add(vaMoveLabel);

        jPanelnorth.add(label);
        jpanelEast.add(jpanelSubEast, BorderLayout.CENTER);
        jpanelEast.add(round, BorderLayout.SOUTH);

        frame.add(jpanelEast, BorderLayout.EAST);
        frame.add(jPanelWest, BorderLayout.WEST);
        frame.add(jPanelSouth, BorderLayout.SOUTH);
        frame.add(jPanelnorth, BorderLayout.NORTH);
        scrolHueUperBoundLower.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {

                labelHueUpperBoundLower.setText("   Hue(UpperBoundLower): " + (double) scrolHueUperBoundLower.getValue() / 1000);
                hueUpLowerBound = scrolHueUperBoundLower.getValue();

                thresholds = new Thresholds((double) hueLowLowerBound / 10000, (double) hueLowUpperBound / 10000, (double) hueUpLowerBound / 1000, (double) hueUpUpperBound / 1000, (double) satLowLowerBound / 10, (double) satUpUpperBound / 10);
                pictureAccessor = new PictureAccessor(thresholds);
                imageConverter = new ImageConverter(thresholds);
            }
        });

        scrolHueUperBoundUpper.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                labelHueUpperBoundUpper.setText("   Hue(UpperBoundUpper): " + (double) scrolHueUperBoundUpper.getValue() / 1000);
                hueUpUpperBound = scrolHueUperBoundUpper.getValue();

                thresholds = new Thresholds((double) hueLowLowerBound / 10000, (double) hueLowUpperBound / 10000, (double) hueUpLowerBound / 1000, (double) hueUpUpperBound / 1000, (double) satLowLowerBound / 10, (double) satUpUpperBound / 10);
                pictureAccessor = new PictureAccessor(thresholds);
                imageConverter = new ImageConverter(thresholds);
            }
        });


        scrolHueLowerBoundLower.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                labelHueLowerBoundLower.setText("   Hue(LowerBoundLower): " + (double) scrolHueLowerBoundLower.getValue() / 10000);
                hueLowLowerBound = scrolHueLowerBoundLower.getValue();

                thresholds = new Thresholds((double) hueLowLowerBound / 10000, (double) hueLowUpperBound / 10000, (double) hueUpLowerBound / 1000, (double) hueUpUpperBound / 1000, (double) satLowLowerBound / 10, (double) satUpUpperBound / 10);
                pictureAccessor = new PictureAccessor(thresholds);
                imageConverter = new ImageConverter(thresholds);
            }
        });


        scrolHueLowerBoundUpper.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                labelHueLowerBoundUpper.setText("   Hue(LowerBoundUpper): " + (double) scrolHueLowerBoundUpper.getValue() / 10000);
                hueLowUpperBound = scrolHueLowerBoundUpper.getValue();

                thresholds = new Thresholds((double) hueLowLowerBound / 10000, (double) hueLowUpperBound / 10000, (double) hueUpLowerBound / 1000, (double) hueUpUpperBound / 1000, (double) satLowLowerBound / 10, (double) satUpUpperBound / 10);
                pictureAccessor = new PictureAccessor(thresholds);
                imageConverter = new ImageConverter(thresholds);
            }
        });

        scrolSatLow.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                labelSatLow.setText("   Sat: " + (double) scrolSatLow.getValue() / 10);
                satLowLowerBound = scrolSatLow.getValue();

                thresholds = new Thresholds((double) hueLowLowerBound / 10000, (double) hueLowUpperBound / 10000, (double) hueUpLowerBound / 1000, (double) hueUpUpperBound / 1000, (double) satLowLowerBound / 10, (double) satUpUpperBound / 10);
                pictureAccessor = new PictureAccessor(thresholds);
                imageConverter = new ImageConverter(thresholds);
            }
        });
        scrolSatUp.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                labelSatUp.setText("   Sat: " + (double) scrolSatUp.getValue() / 10);
                satUpUpperBound = scrolSatUp.getValue();

                thresholds = new Thresholds((double) hueLowLowerBound / 10000, (double) hueLowUpperBound / 10000, (double) hueUpLowerBound / 1000, (double) hueUpUpperBound / 1000, (double) satLowLowerBound / 10, (double) satUpUpperBound / 10);
                pictureAccessor = new PictureAccessor(thresholds);
                imageConverter = new ImageConverter(thresholds);
            }
        });
        btn_play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btn_play.getText().equals("Reset")) {
                    btn_play.setText("Play");
                    game.reset();
                    userScore.setText("User Score: " + 0);
                    vaScore.setText("Opponent Score:" + 0);
                    round.setText("Round: " + 0);
                    userMoveLabel.setText("User: ");
                    vaMoveLabel.setText("AI: ");
                } else {
                    btn_play.setText("loading ...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String usersChoice = getPrediction(blackAndWhite);
                            updateUserChoice(usersChoice, agent, userLabelPic);
                        }
                    }).start();
                    btn_play.setEnabled(false);
                }
                if (game.gameIsFinished == true) {
                    JOptionPane.showMessageDialog(null, "The Winner Is: " + game.calculateFinalWinner(), "Winner:", JOptionPane.INFORMATION_MESSAGE);
                    btn_play.setText("Reset");
                }
            }
        });
    }

    MatrixComponents matrixComponents = new MatrixComponents();

    private String getPrediction(BufferedImage image) {
        return getPrediction(network, matrixComponents.getHand(image));
    }

    private void updateUserChoice(String userChoice, Agent agent, JLabel userLabelPic) {
        vaChoice = agent.getVaMove();
        game.calculateWinner(userChoice, vaChoice);
        userMoveLabel.setText("User: " + userChoice);
        vaMoveLabel.setText("AI: " + vaChoice);
        vaS = game.getVaScore();
        userS = game.getUserScore();
        userScore.setText("User Score: " + vaS);
        vaScore.setText("Virtual Agent Score:" + userS);
        round.setText("Round: " + game.getRound());
        Image temp = imageConverter.resizeImageIcon(matrixComponents.getHand(blackAndWhite), 100, 100);
        ImageIcon icon = new ImageIcon(temp, "user");
        userLabelPic.setIcon(icon);
        temp = imageConverter.resizeImageIcon(agent.getImage(), 100, 100);
        icon = new ImageIcon(temp, "virtual agent");
        vaMovePic.setIcon(icon);
        btn_play.setText("Play");
        btn_play.setEnabled(true);
    }

    public void startGUI() {
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startCamera();
    }

    private String getPrediction(BasicNetwork network, BufferedImage img) {
        double[] predict = pictureAccessor.convertToInputArray(img, width, height);
        predictedAction = reverseMap.get(getMaxIndex(network.compute(new BasicMLData(predict)).getData()));
        return predictedAction;
    }

    private static int getMaxIndex(double[] data) {
        int maxIndex = 0;
        for (int i = 1; i < data.length; i++) {
            double newnumber = data[i];
            if ((newnumber > data[maxIndex])) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    protected void startCamera() {
        network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(networkFilePath));
        reverseMap.put(0, "paper");
        reverseMap.put(1, "rock");
        reverseMap.put(2, "scissors");
        if (!this.cameraActive) {
            // start the video capture
            this.capture.open(0);

            // is the video stream available?
            if (this.capture.isOpened()) {
                this.cameraActive = true;
                Runnable frameGrabber = new Runnable() {
                    @Override
                    public void run() {
                        BufferedImage imageToShow = grabFrame();
                        blackAndWhite = imageConverter.getBlackAndWhite(imageToShow);
                        currentFrame.setImage(imageConverter.resizeImageIcon(blackAndWhite, 400, 300));
                        frame.repaint();
                        label.setText("System time:" + new Date() + " " +
                                getPrediction(network, blackAndWhite));
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                // grab a frame every 33 ms (30 frames/sec)
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 10, TimeUnit.MILLISECONDS);

            } else {
                // log the error
                System.err.println("Impossible to open the camera connection...");
            }
        } else {
            this.cameraActive = false;
            // stop the timer
            try {
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log the exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }
    }

    private BufferedImage grabFrame() {
        // init everything
        BufferedImage imageToShow = null;
        Mat frame = new Mat();
        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);
                // if the frame is not empty, process it
                if (!frame.empty()) {
                    // convert the Mat object (OpenCV) to Image (JavaFX)
                    imageToShow = mat2Image(frame);
                }

            } catch (Exception e) {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return imageToShow;
    }

    private BufferedImage mat2Image(Mat frame) throws IOException {
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer
        Imgcodecs.imencode(".png", frame, buffer);
        return ImageIO.read(new ByteArrayInputStream(buffer.toArray()));
    }
}
