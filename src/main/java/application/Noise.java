package application;

public class Noise {
    private final OpenSimplexNoise os;
    private final double spreadX;
    private final double spreadY;
    public final double multi;
    private final double z;

    public Noise(double spreadX, double spreadY, double multi) {
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.multi = multi;
        os = new OpenSimplexNoise();
        z = Math.random() * 100000;
    }
    public Noise(double spread, double multi) {
        this(spread, spread, multi);
    }

    public double getVal(double x, double y) {
        return os.eval(x / spreadX, y / spreadY, z) * multi;
    }
}