package bouncingsprites;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Sprite implements Runnable {

    private int x;
    private int y;
    private int dx;
    private int dy;
    private int size;
    private Color color;
    private SpritePanel panel;
    private static final int MAX_SIZE = 40;
    private static final int MIN_SIZE = 10;
    private static final int MAX_SPEED = 5;
    private static final int MIN_SPEED = 1;
    private static final Random random = new Random();
    private static int spritesInside = 0;
    private static int movingSpritesInside = 0;
    private static final int MAX_SPRITES_INSIDE = 4;

    public Sprite(SpritePanel panel) {
        this.panel = panel;
        size = random.nextInt(MAX_SIZE - MIN_SIZE + 1) + MIN_SIZE;
        color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        x = random.nextInt(panel.getWidth() - size);
        y = random.nextInt(panel.getHeight() - size);
        dx = random.nextInt(MAX_SPEED - MIN_SPEED + 1) + MIN_SPEED;
        dy = random.nextInt(MAX_SPEED - MIN_SPEED + 1) + MIN_SPEED;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawOval(panel.getWidth() / 2 - 100, panel.getHeight() / 2 - 100, 200, 200);
    }

    public void move() throws InterruptedException {
        x += dx;
        y += dy;
        if (x < 0 || x + size > panel.getWidth()) {
            dx = -dx;
        }
        if (y < 0 || y + size > panel.getHeight()) {
            dy = -dy;
        }

        synchronized (Sprite.class) {
            if (isInsideCircle()) {
                if (movingSpritesInside >= MAX_SPRITES_INSIDE) {
                    wait();
                } else {
                    movingSpritesInside++;
                }
            } else {
                if (spritesInside >= MAX_SPRITES_INSIDE) {
                    wait();
                } else {
                    spritesInside++;
                }
            }
        }

        panel.repaint();

        synchronized (Sprite.class) {
            if (isInsideCircle()) {
                if (movingSpritesInside > 0) {
                    movingSpritesInside--;
                } else {
                    wait();
                }
            } else {
                if (spritesInside > 0) {
                    spritesInside--;
                } else {
                    wait();
                }
            }
            notifyAll();
        }
    }

    private boolean isInsideCircle() {
        int cx = panel.getWidth() / 2;
        int cy = panel.getHeight() / 2;
        int radius = 100;
        int dx = x + size / 2 - cx;
        int dy = y + size / 2 - cy;
        return dx * dx + dy * dy < radius * radius;
    }

    @Override
    public void run() {
        while (true) {
            try {
                move();
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
