package rpg.engine.common;

import rpg.engine.levels.Level;

import java.util.function.Consumer;

public class GameLoop extends BaseGameLoop
{
    private final Level level;
    private final Consumer<Integer> fpsReporter;
    private final Runnable renderer;

    public GameLoop(Level level, Consumer<Integer> fpsReporter, Runnable renderer)
    {
        this.level = level;
        this.fpsReporter = fpsReporter;
        this.renderer = renderer;
    }

    private static final float timeStep = 0.0166f;

    private long previousTime = 0;
    private float accumulatedTime = 0;

    private float secondsElapsedSinceLastFpsUpdate = 0f;
    private int framesSinceLastFpsUpdate = 0;

    @Override
    public void handle(long currentTime)
    {
        if (previousTime == 0) {
            previousTime = currentTime;
            return;
        }

        float secondsElapsed = (currentTime - previousTime) / 1e9f;
        float secondsElapsedCapped = Math.min(secondsElapsed, getMaximumStep());
        accumulatedTime += secondsElapsedCapped;
        previousTime = currentTime;

        if (accumulatedTime < timeStep) {
            float remainderOfTimeStepSincePreviousInterpolation =
                    timeStep - (accumulatedTime - secondsElapsed);
            float alphaInRemainderOfTimeStep =
                    secondsElapsed / remainderOfTimeStepSincePreviousInterpolation;
            try {
                level.interpolate(alphaInRemainderOfTimeStep);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            return;
        }

        while (accumulatedTime >= 2 * timeStep) {
            try {
                level.update();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            accumulatedTime -= timeStep;
        }
        renderer.run();
         try {
                level.update();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        accumulatedTime -= timeStep;
        float alpha = accumulatedTime / timeStep;
        try {
            level.interpolate(alpha);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        secondsElapsedSinceLastFpsUpdate += secondsElapsed;
        framesSinceLastFpsUpdate++;
        if (secondsElapsedSinceLastFpsUpdate >= 0.5f) {
            int fps = Math.round(framesSinceLastFpsUpdate / secondsElapsedSinceLastFpsUpdate);
            fpsReporter.accept(fps);
            secondsElapsedSinceLastFpsUpdate = 0;
            framesSinceLastFpsUpdate = 0;
        }
    }

    @Override
    public void stop()
    {
        previousTime = 0;
        accumulatedTime = 0;
        secondsElapsedSinceLastFpsUpdate = 0f;
        framesSinceLastFpsUpdate = 0;
        super.stop();
    }

    @Override
    public String toString()
    {
        return "Fixed time steps";
    }
}