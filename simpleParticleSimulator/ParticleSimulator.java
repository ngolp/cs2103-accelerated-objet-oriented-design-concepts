import java.util.*;
import java.util.function.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.sound.sampled.*;

public class ParticleSimulator extends JPanel 
{
	private Heap<Event> _events;
	private java.util.List<Particle> _particles;
	private Wall[] _walls = new Wall[4];
	private double _duration;
	private int _width;

	/**
	 * @param filename the name of the file to parse containing the particles
	 */
	public ParticleSimulator (String filename) throws IOException 
	{
		_events = new HeapImpl<>();

		// Parse the specified file and load all the particles.
		Scanner s = new Scanner(new File(filename));
		_width = s.nextInt();
		_walls[0] = new Wall(Wall.Orientation.Top, 0);
		_walls[1] = new Wall(Wall.Orientation.Bottom, _width);
		_walls[2] = new Wall(Wall.Orientation.Left, 0);
		_walls[3] = new Wall(Wall.Orientation.Right, _width);

		_duration = s.nextDouble();
		s.nextLine();
		_particles = new ArrayList<>();
		while (s.hasNext()) 
		{
			String line = s.nextLine();
			Particle particle = Particle.build(line);
			_particles.add(particle);
		}

		setPreferredSize(new Dimension(_width, _width));
	}

	@Override
	/**
	 * Draws all the particles on the screen at their current locations
	 * DO NOT MODIFY THIS METHOD
	 */
	public void paintComponent (Graphics g) 
	{
		g.clearRect(0, 0, _width, _width);
		for (Particle p : _particles) 
		{
			p.draw(g);
		}
	}

	// Helper class to signify the final event of the simulation.
	private class TerminationEvent extends Event 
	{
		TerminationEvent (double timeOfEvent) 
		{
			super(timeOfEvent);
		}
	}

	/**
	 * Helper method to update the positions of all the particles based on their current velocities.
	 */
	private void updateAllParticles (double delta) 
	{
		for (Particle p : _particles) 
		{
			p.update(delta);
		}
	}

	/**
	 * Helper method for adding events
	 * @param now current time of the solution
	 * @param particles list of particles to be parsed
	 */
	private void addEvents(double now, java.util.List<Particle> particles)
	{
		for (Particle currentParticle : particles) 
		{
			for(Particle otherParticle : _particles)
			{
				if(currentParticle.equals(otherParticle))
					continue;

				double collisionTime = currentParticle.getCollisionTime(otherParticle) + now;
				if(Double.isFinite(collisionTime))
					_events.add(new Event(currentParticle, otherParticle, collisionTime, now));
			}

			for(Wall wall : _walls)
			{
				double collisionTime = wall.getCollisionTime(currentParticle) + now;
				if(Double.isFinite(collisionTime))
					_events.add(new Event(currentParticle, wall, collisionTime, now));
			}
		}
	}

	/**
	 * Helper method to check the validity of an event.
	 * @param event event being evaluated
	 * @return a boolean denoting if the event is valid or not
	 */
	private boolean isValid(Event event)
	{
		if(event.getCollisionType() == Event.CollisionType.Wall && 
		event._mainParticle._lastUpdateTime > event._timeEventCreated)
		{
			return false;
		}
		else if (event.getCollisionType() == Event.CollisionType.Particle &&
		(event._mainParticle._lastUpdateTime > event._timeEventCreated ||
		event._otherParticle._lastUpdateTime > event._timeEventCreated))
		{
			return false;
		}
		
		return true;
	}

	/**
	 * Executes the actual simulation.
	 */
	private void simulate (boolean show) 
	{
		double lastTime = 0;

		// Create initial events, i.e., all the possible
		// collisions between all the particles and each other,
		// and all the particles and the walls.
		addEvents(0, _particles);

		_events.add(new TerminationEvent(_duration));
		while (_events.size() > 0) 
		{
			Event event = _events.removeFirst();
			double now = event._timeOfEvent;
			double delta = now - lastTime;

			if (event instanceof TerminationEvent) 
			{
				updateAllParticles(delta);
				break;
			}

			//Check validity of event
			if(!isValid(event))
				continue;

			// Since the event is valid, then pause the simulation for the right
			// amount of time, and then update the screen.
			if (show) 
			{
				try 
				{
					// Thread.sleep(0);
					Thread.sleep((long) (delta * 100));  // *100 -- slow it down a little
				} 
				catch (InterruptedException ie) {}
			}

			// Update positions of all particles
			updateAllParticles(delta);

			// Update the velocity of the particle(s) involved in the collision
			// (either for a particle-wall collision or a particle-particle collision).
			// You should call the Particle.updateAfterCollision method at some point.
			if(event.getCollisionType() == Event.CollisionType.Wall)
			{
				event._wall.updateAfterCollision(now, event._mainParticle);
			}
			else
			{
				event._mainParticle.updateAfterCollision(now, event._otherParticle);
			}

			// Add events for the affected particles
			java.util.List<Particle> affectedParticles = new ArrayList<>();
			affectedParticles.add(event._mainParticle);
			if(event.getCollisionType() == Event.CollisionType.Particle)
			{
				affectedParticles.add(event._otherParticle);
			}

			addEvents(now, affectedParticles);

			// Update the time of our simulation
			lastTime = now;

			// Redraw the screen
			if (show) 
			{
				repaint();
			}
		}

		// Print out the final state of the simulation
		System.out.println(_width);
		System.out.println(_duration);
		for (Particle p : _particles) 
		{
			System.out.println(p);
		}
	}

	public static void main (String[] args) throws IOException 
	{
		if (args.length < 1) 
		{
			System.out.println("Usage: java ParticalSimulator <filename>");
			System.exit(1);
		}

		ParticleSimulator simulator;

		simulator = new ParticleSimulator(args[0]);
		JFrame frame = new JFrame();
		frame.setTitle("Particle Simulator");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(simulator, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		simulator.simulate(true);
	}
}
