package com.example.loginyokaiinfection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Random;

public class RedParticlesView extends View {
    private static final int COUNT = 45;
    private final Particle[] particles = new Particle[COUNT];
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();
    private final float density;
    private long lastFrame;

    public RedParticlesView(Context context) {
        super(context);
        density = getResources().getDisplayMetrics().density;
        setClickable(false);
        setFocusable(false);

        for (int i = 0; i < COUNT; i++) {
            particles[i] = new Particle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (Particle particle : particles) {
            reset(particle, true);
        }
    }

    private void reset(Particle p, boolean anywhere) {
        p.x = random.nextFloat() * getWidth();
        p.y = anywhere
                ? random.nextFloat() * getHeight()
                : getHeight() + random.nextFloat() * 100 * density;
        p.radius = (2 + random.nextFloat() * 3) * density;
        p.speed = (35 + random.nextFloat() * 65) * density;
        p.drift = (-18 + random.nextFloat() * 36) * density;
        p.petal = random.nextFloat() < 0.3f;
        p.angle = random.nextFloat() * 360;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long now = System.nanoTime();
        float dt = lastFrame == 0
                ? 0.016f
                : Math.min((now - lastFrame) / 1_000_000_000f, 0.05f);
        lastFrame = now;

        for (Particle p : particles) {
            p.x += p.drift * dt;
            p.y -= p.speed * dt;
            p.angle += 50 * dt;

            if (p.y < -20 * density) reset(p, false);
            if (p.x < -20 * density) p.x = getWidth() + 20 * density;
            if (p.x > getWidth() + 20 * density) p.x = -20 * density;

            paint.setColor(Color.argb(35, 255, 20, 45));
            canvas.drawCircle(p.x, p.y, p.radius * 3, paint);

            paint.setColor(Color.argb(190, 255, 35, 50));
            if (p.petal) {
                canvas.save();
                canvas.rotate(p.angle, p.x, p.y);
                canvas.drawOval(
                        p.x - p.radius * 2, p.y - p.radius / 2,
                        p.x + p.radius * 2, p.y + p.radius / 2,
                        paint
                );
                canvas.restore();
            } else {
                canvas.drawCircle(p.x, p.y, p.radius, paint);
            }
        }

        postInvalidateOnAnimation(); // Mantiene el movimiento en bucle.
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        lastFrame = 0;
        if (visibility == VISIBLE) postInvalidateOnAnimation();
    }

    private static class Particle {
        float x, y, radius, speed, drift, angle;
        boolean petal;
    }
}

