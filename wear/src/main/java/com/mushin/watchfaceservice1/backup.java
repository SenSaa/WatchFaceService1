/*
package com.mushin.watchfaceservice1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.TimeZone;

/**
 * Watch Face is a Service (runs in the background).
 */
/*
// Inherit from "CanvasWatchFaceService" class, which is an implementation of "WatchFaceService" that also provides a Canvas for drawing out your watch face.
public class WatchFaceService1 extends CanvasWatchFaceService {



    // Override the "onCreateEngine" method to return the "WatchFaceEngine" inner class.
    // This will associate the watch face service with the code that will drive the display.
    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    // The Engine object associated with your service is what drives your watch face.
    // It handles timers, displaying your user interface, moving in and out of ambient mode, and getting information about the physical watch display.
    private class Engine extends CanvasWatchFaceService.Engine {

        // ------------------- Declare Variables---------------------- //

        // Implement a set of member variables in your engine to keep track of device states, timer intervals, painting graphics, and attributes for your display.

        protected static final int MSG_UPDATE_TIME = 0;

        // Graphic Paint variables.
        private Paint mHourPaint;
        private Paint mMinutePaint;
        private Paint mSecondPaint;
        private Paint mTickPaint;

        private Paint mTickPaint2;
        private Paint innerCirclePaint;
        private Paint outerCirclePaint;
        private Paint pathPaint;
        private Paint centreCirPaint;
        private Paint centreCirPaint2;
        private Paint jTextPaint;
        private Paint jTextPaint2;

        private Paint mTickPaint3;
        private Paint mTickPaint4;
        private Paint mTickPaint5;
        private Paint mTickPaint6;
        private Paint mTickPaint7;

        // Time variable
        protected Time mTime;

        // Variables for the hardware properties of the watch.
        protected boolean mIsRound;
        protected boolean mLowBitAmbient;

        // Screen variables.
        private int mHalfWidth, mHalfHeight;

        int mHourColorInteractive = Color.argb(255, 255,0,0);


        // The timer is implemented by a Handler, which will force a repaint (invalidate) and sent a delayed message.
        // The Handler is used to schedule messages and runnables to be executed at some point in the future.
        // Handler updates the time once a second in interactive mode.
        // In our example, the handler sends a message which is processed a half of a second later.
        final Handler mUpdateTimeHandler = new Handler() {
            @Override
            // Callback method - called when the messages are sent by the thread.
            public void handleMessage(Message message) {
                // when the handler is active, call invalidate() to make the screen redraw.
                invalidate();

                // only visible and interaction mode, we active the handler
                // AmbientMode use another time.
                if (isVisible() && !isInAmbientMode()) {

                    // "sendEmptyMessageDelayed" delays message sending. The delay value passed is 500 milliseconds.
                    // The first parameter is message code, we don't use it here, so put 0.
                    mUpdateTimeHandler.sendEmptyMessageDelayed(0, 500);
                }
            }
        };

        // ------------------- ____________________ ---------------------- //


        // ------------------- Initialize Variables ---------------------- //

        @Override
        // Initialize Watch Face, create Paint objects, create time object, load images, etc.
        public void onCreate(SurfaceHolder holder) {
            // Call "onCreate" method of the Super class {WatchFaceService.Engine}.
            super.onCreate(holder);

            // Initialize Watch Face (Configure the system UI).

            setWatchFaceStyle(new WatchFaceStyle.Builder(WatchFaceService1.this) // Sets the watch face style.
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT) // Sets how far into the screen the first card will peek while the watch face is displayed.
                    .setAmbientPeekMode(WatchFaceStyle.AMBIENT_PEEK_MODE_HIDDEN) // Sets how the first, peeking card will be displayed while the watch is in ambient mode (black & white mode).
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE) // Set how to display background of the first, peeking card.
                            // Set this to "false" if you are drawing or representing the time on your watch face. But if you want the system-style time to show over the watch face, pass the value "true".
                    .setShowSystemUiTime(false) // Sets if the system will draw the system-style time over the watch face.
                    .build()); // Constructs read-only WatchFaceStyle object.


            // get display size
            Resources resources = WatchFaceService1.this.getResources(); // Return a Resources instance for your application's package.
            DisplayMetrics metrics = resources.getDisplayMetrics(); // Return the current display metrics that are in effect for this resource object.
            mHalfWidth = metrics.widthPixels / 2;
            mHalfHeight = metrics.heightPixels / 2;


            // Create and set Paint objects for hour, minute and seconds...
            mHourPaint = new Paint(); // Create "Hour" Paint object.
            mHourPaint.setARGB(255, 255, 0, 0); // Call "setARGB" method to set color, which takes a,r,g,b values and constructs the color int.
            mHourPaint.setStrokeWidth(6.0f); // Call "setStrokeWidth" to set the width for stroking (Parameter = float width).
            mHourPaint.setAntiAlias(true); // Call "setAntiAlias" used for setting or clearing the ANTI_ALIAS_FLAG bit. AntiAliasing smooths out the edges of what is being drawn.
            mHourPaint.setStrokeCap(Paint.Cap.ROUND); // Call "setStrokeCap" set the style of the paint's line endings. It is used whenever the paint's style is Stroke or StrokeAndFill.

            mMinutePaint = new Paint(); // Create "Minute" Paint object.
            mMinutePaint.setARGB(245, 245, 245, 245); // a - alpha component (0..255). r	- red component (0..255). g - green component (0..255). b- blue component (0..255).
            mMinutePaint.setStrokeWidth(4.0f); // Set the paint's stroke width, the parameter it accepts is float width.
            mMinutePaint.setAntiAlias(true); // "setAntiAlias" sets or clears the ANTI_ALIAS_FLAG. If set to "true" it smooths out the edges of what is being drawn.
            mMinutePaint.setStrokeCap(Paint.Cap.ROUND); // "setStrokeCap" sets the style of the paint's line endings. These line endings can be squared or round for example.

            mSecondPaint = new Paint(); // Create "Second" Paint object.
            mSecondPaint.setARGB(250, 139, 69, 13); // Setting the color for paint in ARGB format/representation.
            mSecondPaint.setStrokeWidth(2.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            mSecondPaint.setAntiAlias(true); // Smooth out the edges of what is being drawn, if the argument value passed is set to true.
            mSecondPaint.setStrokeCap(Paint.Cap.SQUARE); // Call "setStrokeCap" set the style of the paint's line endings. It is used whenever the paint's style is Stroke or StrokeAndFill.

            mTickPaint = new Paint(); // Create the 12 hour "Tick Marks" Paint object.
            mTickPaint.setARGB(200, 199, 21, 133); // Setting the color for paint in ARGB format/representation.
            mTickPaint.setStrokeWidth(4.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            mTickPaint.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            mTickPaint.setStrokeCap(Paint.Cap.BUTT); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.


            mTickPaint2 = new Paint(); // Create another Paint object for the smaller 60 minute/second "Tick Marks" of the watch face.
            mTickPaint2.setARGB(100, 75, 0, 130); // Setting the color for paint in ARGB format/representation.
            mTickPaint2.setStrokeWidth(4.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            mTickPaint2.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            mTickPaint2.setStrokeCap(Paint.Cap.BUTT); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.

            innerCirclePaint = new Paint(); // Create a Paint object for the inner circle of the watch face.
            innerCirclePaint.setARGB(150, 255, 255, 255); // Setting the color for paint in ARGB format/representation.
            innerCirclePaint.setStrokeWidth(4.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            innerCirclePaint.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            innerCirclePaint.setStrokeCap(Paint.Cap.BUTT); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.

            outerCirclePaint = new Paint(); // Create another Paint object for the outer circle of the watch face.
            outerCirclePaint.setARGB(255, 0, 0, 0); // Setting the color for paint in ARGB format/representation.
            outerCirclePaint.setStrokeWidth(4.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            outerCirclePaint.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            outerCirclePaint.setStrokeCap(Paint.Cap.BUTT); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.


            pathPaint = new Paint(); // Create a Paint object for the path shape that will be drawn in the "onDraw" method.
            pathPaint.setARGB(25, 255, 255, 255); // Setting the color for paint in ARGB format/representation.
            pathPaint.setStrokeWidth(2.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            pathPaint.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            pathPaint.setStrokeCap(Paint.Cap.SQUARE); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.


            centreCirPaint = new Paint(); // Create a Paint object for the for the bigger background circle of the watch face.
            centreCirPaint.setARGB(10, 255, 255, 255); // Setting the color for paint in ARGB format/representation.
            centreCirPaint.setStrokeWidth(2.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            centreCirPaint.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            centreCirPaint.setStrokeCap(Paint.Cap.SQUARE); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.

            centreCirPaint2 = new Paint(); // Create a Paint object for the for the smaller background circle of the watch face.
            centreCirPaint2.setARGB(15, 255, 0, 0); // Setting the color for paint in ARGB format/representation.
            centreCirPaint2.setStrokeWidth(2.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            centreCirPaint2.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            centreCirPaint2.setStrokeCap(Paint.Cap.SQUARE); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.


            jTextPaint = new Paint(); // Create a Paint object for the Japanese text on the outside area.
            jTextPaint.setARGB(200, 250, 250, 250); // Setting the color for paint in ARGB format/representation.
            jTextPaint.setTextSize(20); // Set the Paint text size.
            jTextPaint.setStrokeWidth(4.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            jTextPaint.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            jTextPaint.setStrokeCap(Paint.Cap.SQUARE); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.

            jTextPaint2 = new Paint(); // Create a Paint object for the Japanese text that is closer to the centre area.
            jTextPaint2.setARGB(200, 154, 205, 50); // Setting the color for paint in ARGB format/representation.
            jTextPaint2.setTextSize(20); // Set the Paint text size.
            jTextPaint2.setStrokeWidth(2.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            jTextPaint2.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            jTextPaint2.setStrokeCap(Paint.Cap.ROUND); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.


            mTickPaint3 = new Paint(); // Create another Paint object for "Tick Marks" of the watch face.
            mTickPaint3.setARGB(250, 255, 0, 255); // Setting the color for paint in ARGB format/representation.
            mTickPaint3.setStrokeWidth(4.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            mTickPaint3.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            mTickPaint3.setStrokeCap(Paint.Cap.BUTT); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.

            mTickPaint4 = new Paint(); // Create another Paint object for "Tick Marks" of the watch face.
            mTickPaint4.setARGB(200, 255, 0, 255); // Setting the color for paint in ARGB format/representation.
            mTickPaint4.setStrokeWidth(2.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            mTickPaint4.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            mTickPaint4.setStrokeCap(Paint.Cap.BUTT); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.


            mTickPaint5 = new Paint(); // Create another Paint object for "Tick Marks" of the watch face.
            mTickPaint5.setARGB(150, 169, 169, 169); // Setting the color for paint in ARGB format/representation.
            mTickPaint5.setStrokeWidth(2.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            mTickPaint5.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            mTickPaint5.setStrokeCap(Paint.Cap.BUTT); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.

            mTickPaint6 = new Paint(); // Create another Paint object for "Tick Marks" of the watch face.
            mTickPaint6.setARGB(200, 105, 105, 105); // Setting the color for paint in ARGB format/representation.
            mTickPaint6.setStrokeWidth(2.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            mTickPaint6.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            mTickPaint6.setStrokeCap(Paint.Cap.BUTT); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.

            mTickPaint7 = new Paint(); // Create another Paint object for "Tick Marks" of the watch face.
            mTickPaint7.setARGB(200, 0, 0, 0); // Setting the color for paint in ARGB format/representation.
            mTickPaint7.setStrokeWidth(2.0f); // Set the width of the paint's stroke, the value passed must be in float width (1.0f) for example.
            mTickPaint7.setAntiAlias(true); // Smooth out the edges of what is being drawn, assuming the argument value passed is set to true.
            mTickPaint7.setStrokeCap(Paint.Cap.BUTT); // Call "setStrokeCap" to set the style of the paint's line ending to square or round.

            mTime = new Time(); // Construct a Time object in the default timezone, which will hold the time value.

        }

        // ------------------- ____________________ ---------------------- //


        // ------------------- Timer Update ---------------------- //
        // If we're in interactive mode, we need to take care of the timing.
        // But we have to make sure, that our timer only runs if we're in interactive mode.
        // If the mode is changed to ambient mode, we should deactivate the timer.
        private void updateTimer() {
            // Remove the messages which are already in the queue.
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            // If the the watch face is visible and not in ambient mode,
            if (shouldTimerBeRunning()) {
                // send the instant message into the queue.
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        public void onDestroy() {
            // Remove the messages which are already in the queue.
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            // Call "onDestroy" method of the Super class {WatchFaceService.Engine}.
            super.onDestroy();
        }


        // The "onVisibilityChanged" method is called when the user hides or shows the watch face.
        @Override
        public void onVisibilityChanged(boolean visible) {
            // Call "onVisibilityChanged" method of the Super class {WatchFaceService.Engine}.
            super.onVisibilityChanged(visible);

            // Remove the messages which are already in the queue.
            mUpdateTimeHandler.removeMessages(0);

            // If the the watch face is visible and not in ambient mode,
            if (isVisible() && !isInAmbientMode()) {
                // send the instant message into the queue.
                mUpdateTimeHandler.sendEmptyMessage(0);
            }
        }

        // If we're in ambient mode, Android makes sure that we get an update once a minute. Now we need to redraw our watchface. The easiest way for this is to call the invalidate method.
        @Override
        public void onTimeTick() {
            // Call "onTimeTick" method of the Super class {WatchFaceService.Engine}.
            super.onTimeTick();
            // The event is only invoked once a minute when the wear is in ambient mode.
            // Call "invalidate" method to invalidate the canvas to redraw the watch face.
            invalidate();
        }

        // ------------------- ____________________ ---------------------- //


        // ------------------- Mode Changing ---------------------- //
        // Implement various methods from the Engine class that are triggered by changes to the device state.
        // "onPropertiesChanged" method is called when the hardware properties for the Wear device are determined,
        // for example, if the device supports burn-in protection or low bit ambient mode.
        public void onPropertiesChanged(Bundle properties) {
            // Call "onPropertiesChanged" method of the Super class {WatchFaceService.Engine}.
            super.onPropertiesChanged(properties);
            // Get the value of the property "LOW_BIT_AMBIENT" as a boolean (convert property to boolean), make "false" its default value, and assign it to a member variable.
            mLowBitAmbient = properties.getBoolean(CanvasWatchFaceService.PROPERTY_LOW_BIT_AMBIENT, false);
        }

/*
        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);
            mIsRound = insets.isRound();
            if (mIsRound){
                mTextPaint.setTextSize(30);
            } else{
                mTextPaint.setTextSize(25);
            }
        }
*/
/*
        // Implement "onAmbientModeChanged", which is called when the device moves in or out of ambient mode.
        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            // Call "onAmbientModeChanged" method of the Super class {WatchFaceService.Engine}.
            super.onAmbientModeChanged(inAmbientMode);

            // * The following block of code is required for the second "hand" to update every second after the screen recovers from Ambient mode.
            // "mLowBitAmbient" is one of the Ambient modes, its' the low-bit version where more pixels are turned off.
            // Be mindful of anti-aliasing for devices that request low bit ambient support.
            if (mLowBitAmbient) {
                boolean antiAlias = !inAmbientMode;
                setAntiAlias(antiAlias);
            }

            // Schedules a call to "onDraw" to draw the next frame.
            invalidate();
            // Call "updateTimer" method.
            updateTimer();

        }

        // Method for smoothing out the edges of the hour, minute, and second hands as well as the tick markings.
        private void setAntiAlias(boolean antiAlias) {
            mHourPaint.setAntiAlias(antiAlias);
            mMinutePaint.setAntiAlias(antiAlias);
            mSecondPaint.setAntiAlias(antiAlias);
            mTickPaint.setAntiAlias(antiAlias);
        }

// ------------------- ____________________ ---------------------- //


        // ------------------- Watchface Drawing ---------------------- //
        // CanvasWatchFaceService uses a standard Canvas object, so you will need to add "onDraw" to the Engine and manually draw out your watch face.
        @Override
        // Draw the Clock.
        public void onDraw(Canvas canvas, Rect bounds) {
            mTime.setToNow(); // Sets the time of the given Time object to the current time.

            canvas.drawColor(Color.BLACK); //black background
            canvas.save(); // save transformation matrix
            canvas.translate(mHalfWidth, mHalfHeight); // translate origin to center

            // Declare and initialise the length of the hour and minute hands.
            float hourLen = mHalfHeight - 40;
            float minLen = mHalfHeight - 10;


            // If not in ambient mode,
            //if(!isInAmbientMode()) {

            // ____________________________ Draw the "Path shape". ____________________________

                /* Note!!!
                * It seems like the top and bottom (y-axis) are reversed somehow!
                */
/*
                // Draw Path Shape (triangle facing downwards).
                Path path2 = new Path(); // Create a Path object.
                // Set the beginning of the next contour to the point (x,y).
                path2.moveTo(-40, 0); // (left, top)
                // Add a line from the last point to the specified point (x,y).
                path2.lineTo(0, 40); // (right, top)
                path2.lineTo(40, 0); // (right, bottom)
                path2.lineTo(0, 0); // (left, bottom)
                path2.close(); // Close the current contour.
                canvas.drawPath(path2, pathPaint); // Draw the path.


                // Draw Path Shape (triangle facing upwards).
                Path path3 = new Path(); // Create a Path object.
                // Set the beginning of the next contour to the point (x,y).
                path3.moveTo(-40, 0); // (left, top)
                // Add a line from the last point to the specified point (x,y).
                path3.lineTo(0, -40); // (right, top)
                path3.lineTo(40, 0); // (right, bottom)
                path3.lineTo(0, 0); // (left, bottom)
                path3.close(); // Close the current contour.
                canvas.drawPath(path3, pathPaint); // Draw the path.
*/
/*
            // Draw Path Shape (triangle facing upwards + triangle facing downwards = diamonds).
            Path path4 = new Path(); // Create a Path object.
            // Set the beginning of the next contour to the point (x,y).
            path4.moveTo(-40, 0); // (left, top)
            // Add a line from the last point to the specified point (x,y).
            path4.lineTo(0, -40); // (right, top)
            path4.lineTo(40, 0); // (right, bottom)
            path4.lineTo(0, 40); // (left, bottom)
            path4.close(); // Close the current contour.
            canvas.drawPath(path4, pathPaint); // Draw the path.


            // __________ Draw the big background "circles" at the centre of watch. __________

            canvas.drawCircle(0, 0, 120, centreCirPaint);
            canvas.drawCircle(0, 0, 90, centreCirPaint2);

            // _____________________________ Draw "tick markings". _____________________________

            /*
            * The first set of tick markings will be 12 representing the hours.
            */
/*
            // While between 0-12, increment the counter variable "i".
            for (int i = 0; i < 12; i++) {
                // Draw tick markings.
                    /*
                        * The main tick markings on a clock watch represent the 12 hours.
                        * A full circle has 2π radians.
                        * The hour hand goes around full circle in 1*12 hours = 12 hours.
                        * Since velocity (and therefore angular velocity) is a ratio of how far something gets in a unit of time.
                        * Therefore, the speed of the hour hand is: 2π/12 rad/hours = π/6 rad/hours.
                        * So, that is the angle (θ).
                    */
/*
                float rad = i / 6f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                // The length of the tick markings will be the difference between the minute hand and the hour hand.
                // For the inner position point (closer to the centre):
                float innerX = (float) Math.sin(rad) * hourLen; // Calculate the x-component of the inner "hour tick mark".
                float innerY = -(float) Math.cos(rad) * hourLen; // Calculate the y-component of the inner "hour tick mark".
                // For the outer position point (towards the edge of the screen):
                float outerX = (float) Math.sin(rad) * minLen; // Calculate the x-component of the outer "hour tick mark".
                float outerY = -(float) Math.cos(rad) * minLen; // Calculate the y-component of the outer "hour tick mark".
                canvas.drawLine(innerX, innerY, outerX, outerY, mTickPaint); // Draw the line shape of the tick markings.
            }


            // If not in ambient mode,
            if(!isInAmbientMode()) {


                /*
                * The second set of tick markings will be 60 representing the mins/secs.
                * And should only appear in interactive mode, but not in Ambient mode.
                */
/*
                // While between 0-60, increment the counter variable "i2".
                for (int i2 = 0; i2 < 60; i2++) {

                    // This introduces a new set of tick marks.
                    float rad2 = i2 / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                    float innerTickX = -(float) Math.sin(rad2) * (160); // Calculate the x-component of the inner "hour tick mark".
                    float innerTickY = (float) Math.cos(rad2) * (140); // Calculate the y-component of the inner "hour tick mark".
                    float outerTickX = -(float) Math.sin(rad2) * (140); // Calculate the x-component of the inner "hour tick mark".
                    float outerTickY = (float) Math.cos(rad2) * (160); // Calculate the y-component of the inner "hour tick mark".
                    canvas.drawLine(innerTickX, innerTickY, outerTickX, outerTickY, mTickPaint2); // Draw the line shape of the tick markings.

/*
                    // This introduces a new set of tick marks.
                    float rad2 = i2 / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                    float innerTickX = (float) Math.sin(rad2) * (hourLen * 1/2); // Calculate the x-component of the inner "hour tick mark".
                    float innerTickY = -(float) Math.cos(rad2) * (hourLen * 1/2); // Calculate the y-component of the inner "hour tick mark".
                    float outerTickX = (float) Math.sin(rad2) * minLen; // Calculate the x-component of the inner "hour tick mark".
                    float outerTickY = -(float) Math.cos(rad2) * minLen; // Calculate the y-component of the inner "hour tick mark".
                    canvas.drawLine(innerTickX, innerTickY, outerTickX, outerTickY, mTickPaint2); // Draw the line shape of the tick markings.
*/
/*
                    // This introduces a new set of tick marks on the outside area beyond the hour tick marks.
                    float rad2 = i2 / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                    float innerTickX = (float) Math.sin(rad2) * (hourLen * 2); // Calculate the x-component of the inner "hour tick mark".
                    float innerTickY = -(float) Math.cos(rad2) * (hourLen * 2); // Calculate the y-component of the inner "hour tick mark".
                    float outerTickX = (float) Math.sin(rad2) * minLen; // Calculate the x-component of the inner "hour tick mark".
                    float outerTickY = -(float) Math.cos(rad2) * minLen; // Calculate the y-component of the inner "hour tick mark".
                    canvas.drawLine(innerTickX, innerTickY, outerTickX, outerTickY, mTickPaint2); // Draw the line shape of the tick markings.
*/
/*
                    // This introduces a new set of tick marks on the inside area of the hour tick marks.
                    float rad2 = i2 / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                    float innerTickX = (float) Math.sin(rad2) * hourLen; // Calculate the x-component of the inner "hour tick mark".
                    float innerTickY = -(float) Math.cos(rad2) * hourLen; // Calculate the y-component of the inner "hour tick mark".
                    float outerTickX = (float) Math.sin(rad2) * (minLen * 1/2); // Calculate the x-component of the inner "hour tick mark".
                    float outerTickY = -(float) Math.cos(rad2) * (minLen * 1/2); // Calculate the y-component of the inner "hour tick mark".
                    canvas.drawLine(innerTickX, innerTickY, outerTickX, outerTickY, mTickPaint2); // Draw the line shape of the tick markings.
*/
/*
                    // This introduces a new set of line marks across the centre.
                    float rad2 = i2 / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                    float innerTickX = (float) Math.sin(rad2) * minLen - 20; // Calculate the x-component of the inner "hour tick mark".
                    float innerTickY = (float) Math.cos(rad2) * minLen - 20; // Calculate the y-component of the inner "hour tick mark".
                    float outerTickX = (float) Math.sin(rad2) * minLen; // Calculate the x-component of the inner "hour tick mark".
                    float outerTickY = -(float) Math.cos(rad2) * minLen; // Calculate the y-component of the inner "hour tick mark".
                    canvas.drawLine(innerTickX, innerTickY, outerTickX, outerTickY, mTickPaint2); // Draw the line shape of the tick markings.
*/
/*
                }

                /*
                !Note!
                - The built-in super class method "onTimeTick" natively updates the time every minutes instead of seconds in Ambient mode.
                - So, we might as well avoid drawing the "second" watch hand when in Ambient mode, since it will not update and freeze in its last ticking state.
                */

                // _____________________________ Draw "second" hand. _____________________________

                /*
                * Using trigonometry and geometry to calculate the angle and rotation of the clock watch hand:
                * sin(θ) = O/H  _  cos(θ) = A/H.   ->   O = sin(θ) * H, A = cos(θ) * H.
                * where, H = is the length of the clock watch hand.
                * And where, (θ) = Angle of the clock watch hand.
                *
                *  (y)
                *  |
                *  |      ___O___
                *  |     |      /
                *  |     |     /
                *  |     A    H
                *  |     |   /
                *  |     |  /
                *  |     |θ/
                *  |     |
                *  --------------------------------- (x)
                *
                * The opposite (O) represents the x-axis, where the adjacent (A) represents the y-axis.
                * x = sin(θ) * length  _  y = cos(θ) * length.
                * For the length, we are using the display size (Display metrics_Height or Width), and halving it. So that we can have clock watch hands that start from the centre, and near the edge of the screen.
                * Since the display resolution is square (length = width), it doesn't matter which one to use for as the kength for x & y.
                * For the angle (θ), it changes with the rotation of the clock watch hand, and it differs for each clock watch hand (secs, mins, hrs).
                *
                * For the "second" hand:
                * Since the "second" hand rotates or makes a complete cycle every minute (60 secs), that is 1 revolution per minute (rpm).
                * Taking into account, ω = 2πf , f = ω/2π  ->  1Hz = 2π rad/s , 1 rad/s = 1/2π Hz.
                * Since a revolution is a full cycle = 2π rad, and 1 minute = 60 secs.
                * Converting 1 rad/s to 1 rpm  ->  1 rpm = (2π/60) * 1/2π Hz = 1/60 Hz.
                * Using 1Hz = 2π rad/s in the above, 1 rpm = 1/60 * (2π rad/s) = 2π/60 rad/s.
                * So, the angle (θ) of second hand = 2π/60.
                * And to find the angle of each second, we multiply (2π/60) by the current second.
                *
                * A simpler way:
                * A full circle has 2π radians. Since velocity (and therefore angular velocity) is a ratio of how far something gets in a unit of time, then...
                * The second hand goes around full circle in 1 min; therefore, the speed of the second hand is: 2π/1 rad/min = 2π rad/min.
                * Now, since the standard unit for angular velocity is radians/second rather than radians/minute, convert the results from rad/min to rad/s.
                * You do this by multiplying the minutes by 60 since 1 min has 60 s: 2π/1 rad/min = 2π/(1*60) rad/s = 2π/60 rad/s = π/30 rad/s.
                *
                * Now, putting everything together, using rad/s result:
                * x = sin(sec*2π/60)*secLength  ,  y = cos(sec*2π/60)*secLength.
                * Since 2/60 = 1/30, then...
                * or x = sin(sec*π/30)*secLength  ,  y = cos(sec*π/30)*secLength.
                * And since the rotation is clockwise (negative rotation), we add the negative sign to one of the angles, the cosine angle.
                 */
                // Angle of rotating "second" hand.
/*
                float second = mTime.second / 30f * (float)Math.PI; // Access/call constant variable "second" from the Time class to get the current second, and multiply it by the calculation of the angle of rotation.
                float secX = (float) Math.sin(second) * minLen; // Calculate the x-component of the "second" hand.
                float secY = (float) -Math.cos(second) * minLen; // Calculate the y-component of the "second" hand.
                canvas.drawLine(0, 0, secX, secY, mSecondPaint); // Draw the line shape of the "second" hand.


                // Adding modifications to the "second" watch hand, such as the additional line shapes, and the black circular line end.
                canvas.drawCircle(secX, secY, 10, outerCirclePaint);
                canvas.drawLine(secX, secY, 5, -5, mSecondPaint);
                canvas.drawLine(secX, secY, -5, 5, mSecondPaint);



                // Illuminating ticks following the "second" watch hand (before & after the "second" hand.
                float rad3 = (mTime.second - 1) / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                float innerTickX2 = (float) Math.sin(rad3) * (160); // Calculate the x-component of the inner "hour tick mark".
                float innerTickY2 = -(float) Math.cos(rad3) * (140); // Calculate the y-component of the inner "hour tick mark".
                float outerTickX2 = (float) Math.sin(rad3) * (140); // Calculate the x-component of the inner "hour tick mark".
                float outerTickY2 = -(float) Math.cos(rad3) * (160); // Calculate the y-component of the inner "hour tick mark".
                canvas.drawLine(innerTickX2, innerTickY2, outerTickX2, outerTickY2, mTickPaint3); // Draw the line shape of the tick markings.

                float rad4 = (mTime.second + 1) / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                float innerTickX3 = (float) Math.sin(rad4) * (160); // Calculate the x-component of the inner "hour tick mark".
                float innerTickY3 = -(float) Math.cos(rad4) * (140); // Calculate the y-component of the inner "hour tick mark".
                float outerTickX3 = (float) Math.sin(rad4) * (140); // Calculate the x-component of the inner "hour tick mark".
                float outerTickY3 = -(float) Math.cos(rad4) * (160); // Calculate the y-component of the inner "hour tick mark".
                canvas.drawLine(innerTickX3, innerTickY3, outerTickX3, outerTickY3, mTickPaint3); // Draw the line shape of the tick markings.

                float rad5 = (mTime.second - 2) / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                float innerTickX4 = (float) Math.sin(rad5) * (160); // Calculate the x-component of the inner "hour tick mark".
                float innerTickY4 = -(float) Math.cos(rad5) * (140); // Calculate the y-component of the inner "hour tick mark".
                float outerTickX4 = (float) Math.sin(rad5) * (140); // Calculate the x-component of the inner "hour tick mark".
                float outerTickY4 = -(float) Math.cos(rad5) * (160); // Calculate the y-component of the inner "hour tick mark".
                canvas.drawLine(innerTickX4, innerTickY4, outerTickX4, outerTickY4, mTickPaint4); // Draw the line shape of the tick markings.

                float rad6 = (mTime.second + 2) / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                float innerTickX5 = (float) Math.sin(rad6) * (160); // Calculate the x-component of the inner "hour tick mark".
                float innerTickY5 = -(float) Math.cos(rad6) * (140); // Calculate the y-component of the inner "hour tick mark".
                float outerTickX5 = (float) Math.sin(rad6) * (140); // Calculate the x-component of the inner "hour tick mark".
                float outerTickY5 = -(float) Math.cos(rad6) * (160); // Calculate the y-component of the inner "hour tick mark".
                canvas.drawLine(innerTickX5, innerTickY5, outerTickX5, outerTickY5, mTickPaint4); // Draw the line shape of the tick markings.


                float rad7 = (mTime.second - 3) / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                float innerTickX6 = (float) Math.sin(rad7) * (160); // Calculate the x-component of the inner "hour tick mark".
                float innerTickY6 = -(float) Math.cos(rad7) * (140); // Calculate the y-component of the inner "hour tick mark".
                float outerTickX6 = (float) Math.sin(rad7) * (140); // Calculate the x-component of the inner "hour tick mark".
                float outerTickY6 = -(float) Math.cos(rad7) * (160); // Calculate the y-component of the inner "hour tick mark".
                canvas.drawLine(innerTickX6, innerTickY6, outerTickX6, outerTickY6, mTickPaint6); // Draw the line shape of the tick markings.

                float rad8 = (mTime.second - 4) / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                float innerTickX7 = (float) Math.sin(rad8) * (160); // Calculate the x-component of the inner "hour tick mark".
                float innerTickY7 = -(float) Math.cos(rad8) * (140); // Calculate the y-component of the inner "hour tick mark".
                float outerTickX7 = (float) Math.sin(rad8) * (140); // Calculate the x-component of the inner "hour tick mark".
                float outerTickY7 = -(float) Math.cos(rad8) * (160); // Calculate the y-component of the inner "hour tick mark".
                canvas.drawLine(innerTickX7, innerTickY7, outerTickX7, outerTickY7, mTickPaint7); // Draw the line shape of the tick markings.

                float rad9 = (mTime.second - 5) / 30f * (float) Math.PI; // We manipulate the counter variable with the rotation angle, and store it in variable "rad".
                float innerTickX8 = (float) Math.sin(rad9) * (160); // Calculate the x-component of the inner "hour tick mark".
                float innerTickY8 = -(float) Math.cos(rad9) * (140); // Calculate the y-component of the inner "hour tick mark".
                float outerTickX8 = (float) Math.sin(rad9) * (140); // Calculate the x-component of the inner "hour tick mark".
                float outerTickY8 = -(float) Math.cos(rad9) * (160); // Calculate the y-component of the inner "hour tick mark".
                canvas.drawLine(innerTickX8, innerTickY8, outerTickX8, outerTickY8, mTickPaint7); // Draw the line shape of the tick markings.



                // The part of the "second" hand watch extending beyond the center and going in the opposite direction.
                float second2 = second - (float)Math.PI; // Introduce 180 degrees (1π radians) shift in angle to the "second" watch hand.
                float secX2 = (float) Math.sin(second2) * (minLen/4); // Calculate the x-component.
                float secY2 = (float) -Math.cos(second2) * (minLen/4); // Calculate the y-component.
                canvas.drawLine(0, 0, secX2, secY2, mSecondPaint); // Draw the line shape.
            }

            // _______________________________ Draw "minute" hand. _______________________________
            /*
                * Using the same principle as seconds to calculate the angle and rotation of the clock watch hand:
                *
                * For the "minute" hand:
                * A full circle has 2π radians. Since velocity (and therefore angular velocity) is nothing but a ratio of how far something gets in a unit of time,
                * You can think about the angular speed like this: "How much time does it take for a hand to go around fully?" or "How far around does a hand get in 1 minute?"
                *
                * The minute hand goes around full circle in 1*60 min = 60 min (there's 60 minute notches on a clock).
                * Therefore, the speed of the minute hand is: 2π/60 rad/min = π/30 rad/min.
                *
                * Now, since the standard unit for angular velocity is radians/second rather than radians/minute, you can convert the results you got if you want.
                * You do this by multiplying the minutes by 60 since 1 min has 60s.
                * 2π/60 rad/min = 2π/(60*60) rad/s = 2π/3600 rad/s = π/1800 rad/s.
                * So, that is the angle (θ) of "minute" hand.
                * And to find the angle of each minute, we multiply it by the current minute.
                *
                * Now, putting everything together, using the rad/min value for the angle:
                * x = sin(min*2π/60)*minLength  ,  y = cos(sec*2π/60)*minLength.
                * Since 2/60 = 1/30, then...
                * or x = sin(min*π/30)*minLength  ,  y = cos(min*π/30)*minLength.
                * And since the rotation is clockwise (negative rotation), we add the negative sign to one of the angles, the cosine angle.
                 */
            // Angle of rotating "minute" hand.
/*
            float minute = mTime.minute / 30f * (float)Math.PI; // Access/call constant variable "minute" from the Time class to get the current minute, and multiply it by the calculation of the angle of rotation.
            float minX = (float) Math.sin(minute) * (minLen * 4/5); // Calculate the x-component of the minute "hand".
            float minY = (float) -Math.cos(minute) * (minLen * 4/5); // Calculate the y-component of the minute "hand".
            canvas.drawLine(0, 0, minX, minY, mMinutePaint); // Draw the line shape of the "minute" hand.

            // ________________________________ Draw "hour" hand. ________________________________
            /*
                * Using the same principle as seconds to calculate the angle and rotation of the clock watch hand:
                *
                * For the "hour" hand:
                * A full circle has 2π radians. Since velocity (and therefore angular velocity) is nothing but a ratio of how far something gets in a unit of time,
                * You can think about the angular speed like this: "How much time does it take for a hand to go around fully?" or "How far around does a hand get in 1 minute?"
                *
                * The hour hand goes around full circle in 60*12 min = 720 min (there's 12 hour notches on a clock, and each hour has 60 minutes);
                * Therefore, the speed of the hour hand is: 2π/720 rad/min = π/360 rad/min.
                * Now, since the standard unit for angular velocity is radians/second rather than radians/minute, you can convert the results you got if you want.
                * You do this by multiplying the minutes by 60 since 1 min has 60s.
                * 2π/720 rad/min = 2π/(720*60) rad/s = 2π/43200 rad/s = π/21600 rad/s
                *
                * The hour hand goes around full circle in 1*12 hours = 12 hours (there's 12hour large notches on a clock).
                * Therefore, the speed of the hour hand is: 2π/12 rad/hours = π/6 rad/hours.
                * So, that is the angle (θ) of "hour" hand.
                * And to find the angle of each hour, we multiply it by the current hour.
                *
                * Now, putting everything together, using the rad/hour result for the angle:
                * x = sin(hour*π/6)*hourLength  ,  y = cos(hour*π/6)*hourLength.
                * And since the rotation is clockwise (negative rotation), we add the negative sign to one of the angles, the cosine angle.
                *
                * *** We also need to add the minute rotation!
                 */
/*
            // Angle of rotating "hour" hand.
            float hour = ((mTime.hour + (mTime.minute / 60f)) / 6f) * (float)Math.PI; // Access/call constant variable "hour" from the Time class to get the current hour, and multiply it by the calculation of the angle of rotation.
            float hrX = (float) Math.sin(hour) * (hourLen * 3/4);  // Calculate the x-component of the hour "hand".
            float hrY = (float) -Math.cos(hour) * (hourLen * 3/4); // Calculate the y-component of the hour "hand".
            canvas.drawLine(0, 0, hrX, hrY, mHourPaint); // Draw the line shape of the "hour" hand.

            // __________________ Draw the small "circles" at the centre of watch. __________________

            // The outer circle in the center.
            float outerCircleX = 0; // Circle x-component.
            float outerCircleY = 0; // Circle y-component.
            float outerCircleR = 20; // Circle radius.
            canvas.drawCircle(outerCircleX, outerCircleY, outerCircleR, outerCirclePaint);

            // The inner white circle in the center.
            // *** The inner white circle must be coded after the outer circle, so that it can become the overlay circle.
            float innerCircleX = 0; // Circle x-component.
            float innerCircleY = 0; // Circle y-component.
            float innerCircleR = 10; // Circle radius.
            canvas.drawCircle(innerCircleX, innerCircleY, innerCircleR, innerCirclePaint);

            // __________________ Draw the "Japanese Text" for time indication. __________________

            //canvas.drawText("一", 0, 150, jTextPaint);

            //canvas.drawText("二", 0, 150, jTextPaint);

            canvas.drawText("三", 130, 7, jTextPaint);
            canvas.drawText("三", 100, 7, jTextPaint2);

            //canvas.drawText("四", 0, 150, jTextPaint);

            //canvas.drawText("五", 0, 150, jTextPaint);

            canvas.drawText("六", -10, 150, jTextPaint);
            canvas.drawText("六", -10, 110, jTextPaint2);

            //canvas.drawText("七", 0, 150, jTextPaint);

            //canvas.drawText("八", 0, 150, jTextPaint);

            canvas.drawText("九", -150, 7, jTextPaint);
            canvas.drawText("九", -120, 7, jTextPaint2);

            //canvas.drawText("十", 0, 150, jTextPaint);

            //canvas.drawText("十一", 0, 150, jTextPaint);

            canvas.drawText("十二", -20, -140, jTextPaint);
            canvas.drawText("十二", -20, -100, jTextPaint2);


            canvas.restore(); // restore transformation matrix
        }


    }

}
*/
