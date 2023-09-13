public interface Collidable
{
    public double getCollisionTime(Particle particle); 

    public void updateAfterCollision(double now, Particle particle);
}