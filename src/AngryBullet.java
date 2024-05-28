/**
 * This program is an AngryBullet game. just like AngryBirds.
 * @author Celaleddin Ömer Sağlam, Student ID:2023400348
 * @since Date: 21.03.2024
 */

import java.awt.event.KeyEvent;
public class AngryBullet {
    // We declare objects globally. so we can reach it anytime.

    // Box coordinates for obstacles and targets
    // Each row stores a box containing the following information:
    // x and y coordinates of the lower left rectangle corner, width, and height
    public static double[][] obstacleArray = {
            {1200, 0, 60, 220},
            {1000, 0, 60, 160},
            {600, 0, 60, 80},
            {600, 180, 60, 160},
            {220, 0, 120, 180}
    };
    public static double[][] targetArray = {
            {1160, 0, 30, 30},
            {730, 0, 30, 30},
            {150, 0, 20, 20},
            {1480, 0, 60, 60},
            {340, 80, 60, 30},
            {1500, 600, 60, 60}
    };

    // Declare Global Game Parameters
    public static int canvas_width = 1600; //screen width
    public static int canvas_height = 800; // screen height
    public static double gravity = 9.80665; // gravity
    public static double x0 = 120; // x coordinates of the bullet’s starting position on the platform
    public static double y0 = 120; // y coordinates of the bullet’s starting position on the platform
    public static double r0 = 3; // radius of the ball
    public static double bulletVelocity = 180; // initial velocity
    public static double bulletAngle = 45.0; // initial angle
    public static double gameSpeed = 0.12; // game speed


    public static void main(String[] args) {

        while (true){ // This loop restarts the game every iteration

            // Prepare the game environment
            initializeVariables();
            StdDraw.setCanvasSize(canvas_width, canvas_height);
            StdDraw.setXscale(0, 1600);
            StdDraw.setYscale(0, 800);
            drawPlayground();

            // Take shooting inputs
            double[] tempArr = shootTheBall(x0, y0, r0, bulletAngle, bulletVelocity);
            bulletVelocity = tempArr[0];
            bulletAngle = tempArr[1];
            double xF = x0;
            double yF = y0;

            // Main game loop comes right here. Every iteration is a frame. It breaks when game ends
            while (true){
                StdDraw.pause(10); // 100 fps
                x0 = xF;
                y0 = yF;

                // We move the ball to next frame
                tempArr =  moveBall(x0, y0, bulletVelocity, bulletAngle);
                xF = tempArr[0]; yF = tempArr[1]; bulletVelocity = tempArr[2]; bulletAngle = tempArr[3];

                // We draw its trajectory for this frame
                drawTrajectory(x0, y0, xF, yF);

                // Stop when you reach an end.
                int stopCondition = isTimeToStop(xF, yF);
                boolean doWeStop = false;
                StdDraw.setPenColor(StdDraw.BLACK);
                switch (stopCondition){
                    case 0:
                        StdDraw.textLeft(20, canvas_height-20, "Max X reached. Press 'r' to shoot again.");
                        doWeStop = true;
                        break;
                    case 1:
                        StdDraw.textLeft(20, canvas_height-20, "Hit the ground. Press 'r' to shoot again.");
                        doWeStop = true;
                        break;
                    case 2:
                        StdDraw.textLeft(20, canvas_height-20, "Hit an obstacle. Press 'r' to shoot again.");
                        doWeStop = true;
                        break;
                    case 3:
                        StdDraw.textLeft(20, canvas_height-20, "Congratulations: You hit the target!");
                        doWeStop = true;
                        break;
                    case 4:
                        continue;
                }
                if (doWeStop){
                    StdDraw.show();
                    break;
                }
            }

            // Wait until R is pressed, then restart the game
            while (true) {
                if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                    break;  // Break the waiting loop
                }
                StdDraw.pause(100);  // Wait a bit before checking again
            }
        }
    }

    public static void initializeVariables(){

        // Game Parameters
        canvas_width = 1600; //screen width
        canvas_height = 800; // screen height
        gravity = 9.80665; // gravity
        x0 = 120; // x and y coordinates of the bullet’s starting position on the platform double y0 = 120;
        y0 = 120;
        r0 = 5; // radius of the ball
        bulletVelocity = 180; // initial velocity
        bulletAngle = 45.0; // initial angle
    }

    public static void drawPlayground(){

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(59, 59,59,59);

        // draw the ball
        drawBall(x0, y0, r0);

        // draw obstacles and targets on canvas
        for (double[] obstacle: obstacleArray){
            drawRectangle(obstacle, "Black");
        }
        for (double[] target: targetArray){
            drawRectangle(target, "Orange");
        }
    }

    public static void drawRectangle(double[] objPos, String color){

        if (color.equals("Orange")) {
            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            StdDraw.filledRectangle(objPos[0] + objPos[2] / 2.0,
                    objPos[1] + objPos[3] / 2.0,
                    objPos[2] / 2.0,
                    objPos[3] / 2.0);
        } else if (color.equals("Black")){
            StdDraw.setPenColor(StdDraw.DARK_GRAY);
            StdDraw.filledRectangle(objPos[0] + objPos[2] / 2.0,
                    objPos[1] + objPos[3] / 2.0,
                    objPos[2] / 2.0,
                    objPos[3] / 2.0);
        }
    }

    public static void drawBall(double xBall, double yBall, double rBall){

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledCircle(xBall, yBall, rBall);
    }

    public static double[] shootTheBall(double x0, double y0, double r0, double angle, double velocity){

        StdDraw.enableDoubleBuffering();

        while (true) {
            // Recreate the screen
            StdDraw.clear();
            drawPlayground();

            // Calculate the end point of the line
            double radianAngle = Math.toRadians(angle);
            double endX = x0 + velocity * Math.cos(radianAngle) / 2.0;
            double endY = y0 + velocity * Math.sin(radianAngle) / 2.0;

            // Draw the line
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.line(x0, y0, endX, endY);

            // Display angle and velocity
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(10, 70, "Angle: " + (int)angle);
            StdDraw.textLeft(10, 50, "Velocity: " + (int)velocity);
            StdDraw.setPenColor(StdDraw.BLACK);


            // User input for angle and velocity adjustments
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP) && angle < 90) angle += 1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN) && angle > -90) angle -= 1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) velocity += 1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) && velocity > 0) velocity -= 1;
            if (StdDraw.isKeyPressed(KeyEvent.VK_R)) break;

            // Show the updated drawing
            StdDraw.show();
        }

        return new double[]{velocity, Math.toRadians(angle)};
    }

    public static double[] moveBall(double xBall, double yBall, double vBall, double thetaBall){

        double time = gameSpeed; // will be used to scale all variables in case balls in between frames are too far or too close
        double scaledTime = time * 2.95; // just a timescale for game to work smoothly

        // Calculate the x and y components of the velocity
        double vx = vBall * Math.cos(thetaBall);
        double vy = vBall * Math.sin(thetaBall);

        // Calculate the new position of the ball
        double xBallNew = xBall + vx * time;
        double yBallNew = yBall + vy * time - 0.5 * gravity * scaledTime * scaledTime;

        // Calculate the new velocity of the ball
        double vyFinal = vy - gravity * scaledTime;
        double vBallNew = Math.sqrt(vx * vx + vyFinal * vyFinal);

        // Calculate the new angle of the ball's motion
        double thetaBallNew = Math.atan2(vyFinal, vx);

        // Return the new values
        return new double[]{xBallNew, yBallNew, vBallNew, thetaBallNew};
    }

    public static void drawTrajectory (double lastX, double lastY, double nowX, double nowY){

        StdDraw.setPenColor(StdDraw.BLACK);
        drawBall(nowX, nowY, r0);
        StdDraw.setPenRadius(0.0006);
        StdDraw.line(lastX, lastY, nowX, nowY);
        StdDraw.setPenRadius();
        StdDraw.show();
    }

    public static int isTimeToStop(double xBall, double yBall){
        // return values will have different meanings.
        // 0:we got out of X bound.
        // 1:we hit the ground.
        // 2:we hit an obstacle.
        // 3:we hit a target.
        // 4:nothing interesting, just keep the game working:)

        // condition 0
        if ((xBall) >= canvas_width){
            return 0;

        // condition 1
        } else if ((yBall) <= 0) {
            return 1;

            // condition 2
        } else {
            for (double[] obstacle: obstacleArray){
                if ((xBall) >= obstacle[0] &&
                        (xBall) <= (obstacle[0] + obstacle[2]) &&
                        (yBall) <= (obstacle[1] + obstacle[3]) &&
                        (yBall) >= obstacle[1]){
                    return 2;
                }
            }
        }

        // condition 3
        for (double[] target: targetArray){
            if ((xBall) >= target[0] &&
                    (xBall) <= (target[0] + target[2]) &&
                    (yBall) <= (target[1] + target[3]) &&
                    (yBall) >= target[1]){
                return 3;
            }
        }
        return 4;
    }
}