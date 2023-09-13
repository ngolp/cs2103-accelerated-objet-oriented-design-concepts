import java.awt.*;
import java.util.*;

public class Wall implements Collidable
{
    public enum Orientation
    {
        Top,
        Bottom,
        Left,
        Right
    }

    private Orientation _orientation;
    private double _offset;

    public Wall(Orientation orientation, int offset)
    {
        _orientation = orientation;
        _offset = offset;
    }

    public Orientation getOrientation()
    {
        return _orientation;
    }

    public double getOffset()
    {
        return _offset;
    }

    /**
     * Gets the time of collision between the particle this wall.
     * @param particle the particle colliding with the wall
     * @return the time of collision between this wall and the particle
     */
	public double getCollisionTime(Particle particle)
	{
		double wallOffset = _offset;
		Orientation wallOrientation = _orientation;

		if(wallOrientation == Orientation.Left)
		{
			if(particle.getVX() < 0)
				return Math.abs((particle.getX() - particle.getRadius()) / particle.getVX());
		}
		else if(wallOrientation == Orientation.Right)
		{
			if(particle.getVX() > 0)
				return Math.abs((wallOffset - (particle.getX() + particle.getRadius())) / particle.getVX());
		}
		else if(wallOrientation == Orientation.Top)
		{
			if(particle.getVY() < 0)
				return Math.abs((particle.getY() - particle.getRadius()) / particle.getVY());
		}
		else if(wallOrientation == Orientation.Bottom)
		{
			if(particle.getVY() > 0)
				return Math.abs((wallOffset - (particle.getY() + particle.getRadius())) / particle.getVY());
		}

		return Double.POSITIVE_INFINITY;
	}

    /**
     * Updates the particle after a collision with the wall.
     * @param now the time that the collision occurs
     * @param particle the particle involved in the collision
     */
	public void updateAfterCollision(double now, Particle particle)
	{
		if(_orientation == Orientation.Top || _orientation == Orientation.Bottom)
		{
            particle.invertVY();
		}
		else
		{
            particle.invertVX();
		}

		particle._lastUpdateTime = now;
	}
}