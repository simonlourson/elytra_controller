public class Elytra {
    public double Yaw;
    public double Pitch;

    public Vec3d Position;
    public Vec3d Motion;

    public void ResetElytra()
    {
        Yaw = 0;
        Pitch = 0;
        Position = new Vec3d(0, 350, 0);
        Motion = Vec3d.getZero();
    }

    public void Tick() {

        Vec3d vec3d_6 = Motion.Clone();

        Vec3d vec3d_7 = this.getVectorForRotation(Pitch, Yaw);
        double double_1 = 0.08D;
        float float_1 = (float)Pitch * 0.017453292F;
        double double_6 = Math.sqrt(vec3d_7.X * vec3d_7.X + vec3d_7.Z * vec3d_7.Z);
        double double_7 = Math.sqrt(squaredHorizontalLength(vec3d_6));
        double double_4 = vec3d_7.Length();
        float float_5 = (float)Math.cos(float_1);
        float_5 = (float)((double)float_5 * (double)float_5 * Math.min(1.0D, double_4 / 0.4D));
        vec3d_6 = Motion.add(0.0D, double_1 * (-1.0D + (double)float_5 * 0.75D), 0.0D);
        double double_11;
        if (vec3d_6.Y < 0.0D && double_6 > 0.0D) {
            double_11 = vec3d_6.Y * -0.1D * (double)float_5;
            vec3d_6 = vec3d_6.add(vec3d_7.X * double_11 / double_6, double_11, vec3d_7.Z * double_11 / double_6);
        }

        if (float_1 < 0.0F && double_6 > 0.0D) {
            double_11 = double_7 * (double)(-Math.sin(float_1)) * 0.04D;
            vec3d_6 = vec3d_6.add(-vec3d_7.X * double_11 / double_6, double_11 * 3.2D, -vec3d_7.Z * double_11 / double_6);
        }

        if (double_6 > 0.0D) {
            vec3d_6 = vec3d_6.add((vec3d_7.X / double_6 * double_7 - vec3d_6.X) * 0.1D, 0.0D, (vec3d_7.Z / double_6 * double_7 - vec3d_6.Z) * 0.1D);
        }

        Motion = (vec3d_6.multiply(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D));


        Position.X += Motion.X;
        Position.Y += Motion.Y;
        Position.Z += Motion.Z;
        //this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
    }

    public static double squaredHorizontalLength(Vec3d vec3d_1) {
        return vec3d_1.X * vec3d_1.X + vec3d_1.Z * vec3d_1.Z;
    }

    protected final Vec3d getVectorForRotation(double pitch, double yaw)
    {
        double f = Math.cos(-yaw * 0.017453292F - Math.PI);
        double f1 = Math.sin(-yaw * 0.017453292F - Math.PI);
        double f2 = -Math.cos(-pitch * 0.017453292F);
        double f3 = Math.sin(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

}
