import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private int pixel[][];
    private int height, width;

    private class SeamFinder {
        class DistRecord {
            int prex;
            int prey;
            double dist;

            DistRecord() {
                dist = Double.MAX_VALUE;
                prex = prey = Integer.MAX_VALUE;
            }
        }

        class PixelPosition {
            int x, y;

            PixelPosition(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }

        LinkedQueue<PixelPosition> queue;
        boolean[][] inQueue;
        DistRecord[][] records;

        SeamFinder() {
            queue = new LinkedQueue<>();
            inQueue = new boolean[height][width];
            records = new DistRecord[height][width];
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    records[y][x] = new DistRecord();
                }
            }
        }

        private void relax(int x, int y, int prex, int prey) {
            if (x < 0 || x >= width) return;
            if (y < 0 || y >= height) return;

            double relaxDist = records[prey][prex].dist + energy(x, y);
            if (records[y][x].dist > relaxDist) {
                records[y][x].dist = relaxDist;
                records[y][x].prex = prex;
                records[y][x].prey = prey;
                if (!inQueue[y][x]) {
                    inQueue[y][x] = true;
                    queue.enqueue(new PixelPosition(x, y));
                }
            }
        }

        public int[] findHorizontal() {
            for (int y = 0; y < height; ++y) {
                records[y][0].dist = energy(0, y);
                records[y][0].prex = records[y][0].prey = -1;
                queue.enqueue(new PixelPosition(0, y));
            }

            while (!queue.isEmpty()) {
                PixelPosition pos = queue.dequeue();
                int x = pos.x;
                int y = pos.y;
                inQueue[y][x] = false;
                relax(x + 1, y - 1, x, y);
                relax(x + 1, y    , x, y);
                relax(x + 1, y + 1, x, y);
            }

            int x = width - 1;
            double minDist = records[0][x].dist;
            int minIndex = 0;
            for (int y = 1; y < height; ++y) {
                if (records[y][x].dist < minDist) {
                    minDist = records[y][x].dist;
                    minIndex = y;
                }
            }

            int[] ret = new int[width];
            int y = minIndex;
            // Note that x = width - 1
            while (x != -1 && y != -1) {
                ret[x] = y;
                DistRecord r = records[y][x];
                x = r.prex;
                y = r.prey;
            }
            return ret;
        }

        public int[] findVertical() {
            for (int x = 0; x < width; ++x) {
                records[0][x].dist = energy(x, 0);
                records[0][x].prex = records[0][x].prey = -1;
                queue.enqueue(new PixelPosition(x, 0));
            }

            while(!queue.isEmpty()) {
                PixelPosition pos = queue.dequeue();
                int x = pos.x;
                int y = pos.y;
                inQueue[y][x] = false;
                relax(x - 1, y + 1, x, y);
                relax(x    , y + 1, x, y);
                relax(x + 1, y + 1, x, y);
            }

            int y = height - 1;
            double minDist = records[y][0].dist;
            int minIndex = 0;
            for (int x = 1; x < width; ++x) {
                if (records[y][x].dist < minDist) {
                    minDist = records[y][x].dist;
                    minIndex = x;
                }
            }

            int[] ret = new int[height];
            int x = minIndex;
            // Note that x = width - 1
            while (x != -1 && y != -1) {
                ret[y] = x;
                DistRecord r = records[y][x];
                x = r.prex;
                y = r.prey;
            }
            return ret;
        }
    }

    public SeamCarver(Picture picture) {
        if (picture == null) throw new NullPointerException();
        height = picture.height();
        width = picture.width();

        pixel = new int[height][width];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                pixel[y][x] = picture.get(x, y).getRGB();
            }
        }
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width) throw new IndexOutOfBoundsException("x out of bound");
        if (y < 0 || y >= height) throw new IndexOutOfBoundsException("y out of bound");

        final int BORDER_ENERGY = 1000;
        if (x == 0 || x == width - 1 ||
            y == 0 || y == height - 1) {
            return BORDER_ENERGY;
        }

        int diff = 0;
        int v;
        int color1, color2;
        color1 = pixel[y][x + 1];
        color2 = pixel[y][x - 1];
        v = (color1 & 0xff) - (color2 & 0xff);  diff += v * v;
        v = ((color1 >> 8) & 0xff) - ((color2 >> 8) & 0xff);  diff += v * v;
        v = ((color1 >> 16) & 0xff) - ((color2 >> 16) & 0xff);  diff += v * v;

        color1 = pixel[y - 1][x];
        color2 = pixel[y + 1][x];
        v = (color1 & 0xff) - (color2 & 0xff);  diff += v * v;
        v = ((color1 >> 8) & 0xff) - ((color2 >> 8) & 0xff);  diff += v * v;
        v = ((color1 >> 16) & 0xff) - ((color2 >> 16) & 0xff);  diff += v * v;
        return Math.sqrt(diff);
    }

    public Picture picture() {
        Picture p = new Picture(width, height);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                p.set(x, y, new Color(pixel[y][x]));
            }
        }
        return p;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int[] findHorizontalSeam() {
        SeamFinder finder = new SeamFinder();
        return finder.findHorizontal();
    }

    public int[] findVerticalSeam() {
        SeamFinder finder = new SeamFinder();
        return finder.findVertical();
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new NullPointerException();
        if (seam.length != width) throw new IllegalArgumentException("seam length error");
        if (height <= 1) throw new IllegalArgumentException("height too small");

        int prev = seam[0];
        for (int x = 0; x < width; ++x) {
            int mid = seam[x];
            if (mid < 0 || mid >= height || Math.abs(mid - prev) > 1) {
                throw new IllegalArgumentException("seam value out of range");
            }
            prev = mid;
        }

        for (int x = 0; x < width; ++x) {
            int mid = seam[x];
            for (int y = mid + 1; y < height; ++y) {
                pixel[y - 1][x] = pixel[y][x];
            }
        }
        --height;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new NullPointerException();
        if (seam.length != height) throw new IllegalArgumentException("seam length error");
        if (width <= 1) throw new IllegalArgumentException("width too small");

        int prev = seam[0];
        for (int y = 0; y < height; ++y) {
            int mid = seam[y];
            if (mid < 0 || mid >= width || Math.abs(mid - prev) > 1) {
                throw new IllegalArgumentException("seam value out of range");
            }
            prev = mid;
        }

        for (int y = 0; y < height; ++y) {
            int mid = seam[y];
            System.arraycopy(pixel[y], mid + 1, pixel[y], mid, width - (mid + 1));
        }
        --width;
    }
}
