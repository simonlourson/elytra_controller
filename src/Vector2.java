public class Vector2 {
    public float X;
    public float Y;

    public static  Vector2 Zero = new Vector2(0f, 0f);

    public Vector2(float x, float y)
    {
        this.X = x;
        this.Y = y;
    }

    public Vector2 multiplyByFloat(float f)
    {
        return new Vector2(
            X * f,
            Y * f
        );
    }

    public static Vector2 add(Vector2 v1, Vector2 v2)
    {
        return new Vector2(
                v1.X + v2.X,
                v1.Y + v2.Y
        );
    }
}
