import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Vec3d {

    public double X;
    public double Y;
    public double Z;

    private static  Vec3d zero = new Vec3d(0f, 0f, 0f);
    public static Vec3d getZero()
    {
        return zero.Clone();
    }


    public Vec3d(double x, double y, double z)
    {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public double Length()
    {
        double result = DistanceSquared(this, zero);
        return Math.sqrt(result);
    }

    public Vec3d Clone()
    {
        return new Vec3d(X, Y, Z);
    }

    public static double DistanceSquared(Vec3d value1, Vec3d value2)
    {
        return (value1.X - value2.X) * (value1.X - value2.X) +
                (value1.Y - value2.Y) * (value1.Y - value2.Y) +
                (value1.Z - value2.Z) * (value1.Z - value2.Z);
    }

    public Vec3d add(double x, double y, double z)
    {
        return new Vec3d(
                X + x,
                Y + y,
                Z + z
        );
    }

    public Vec3d multiply(double x, double y, double z)
    {
        return new Vec3d(
                X * x,
                Y * y,
                Z * z
        );
    }

    public static Vec3d Add(Vec3d value1, Vec3d value2)
    {
        return new Vec3d(
                value1.X + value2.X,
                value1.Y + value2.Y,
                value1.Z + value2.Z
        );
    }

    public static Vec3d Substract(Vec3d value1, Vec3d value2)
    {
        return new Vec3d(
                value1.X - value2.X,
                value1.Y - value2.Y,
                value1.Z - value2.Z
        );
    }

    public boolean equals(Vec3d otherValue)
    {
        throw new NotImplementedException();
    }


}
