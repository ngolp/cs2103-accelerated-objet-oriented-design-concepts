/**
 * Represents a collision between a particle and another particle, or a particle and a wall.
 */
public class Event implements Comparable<Event> 
{
	public Particle _mainParticle;
	public Particle _otherParticle;
	public Wall _wall;
	public double _timeOfEvent;
	public double _timeEventCreated;

	private CollisionType _collisionType;

	public enum CollisionType
	{
		Particle,
		Wall
	}

	/**
	 * @param timeOfEvent the time when the event will take place (only used for termination event)
	 */
	public Event(double timeOfEvent)
	{
		_timeOfEvent = timeOfEvent;
	}

	/**
	 * @param mainParticle the main particle involved in the event
	 * @param otherParticle the other particle involved in the event
	 * @param timeOfEvent the time when the collision will take place
	 * @param timeEventCreated the time when the event was first instantiated and added to the queue
	 */
	public Event (Particle mainParticle, Particle otherParticle, double timeOfEvent, double timeEventCreated) 
	{
		_mainParticle = mainParticle;
		_otherParticle = otherParticle;
		_timeOfEvent = timeOfEvent;
		_timeEventCreated = timeEventCreated;

		_collisionType = CollisionType.Particle;
	}

	/**
	 * @param mainParticle the main particle involved in the event
	 * @param wall the wall involved in the event
	 * @param timeOfEvent the time when the collision will take place
	 * @param timeEventCreated the time when the event was first instantiated and added to the queue
	 */
	public Event (Particle mainParticle, Wall wall, double timeOfEvent, double timeEventCreated)
	{
		_mainParticle = mainParticle;
		_wall = wall;
		_timeOfEvent = timeOfEvent;
		_timeEventCreated = timeEventCreated;

		_collisionType = CollisionType.Wall;
	}

	public CollisionType getCollisionType()
	{
		return _collisionType;
	}

	@Override
	/**
	 * Compares two Events based on their event times. Since you are implementing a maximum heap,
	 * this method assumes that the event with the smaller event time should receive higher priority.
	 */
	public int compareTo (Event e) 
	{
		if (_timeOfEvent < e._timeOfEvent) 
		{
			return +1;
		} 
		else if (_timeOfEvent == e._timeOfEvent) 
		{
			return 0;
		} 
		else 
		{
			return -1;
		}
	}
}
