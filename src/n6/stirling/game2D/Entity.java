package n6.stirling.game2D;

public class Entity
{
	/** The sprite of the entity. */
	private Sprite s;
	/** The life of the entity. */
	private int life;
	/** The jumps of the entity. */
	private int jumps = 1;
	/** The moving direction of the entity. */
	private int movingDirection = 1;
	/** Flag for the entity's life. */
	private boolean isDead;
	
	/**
	 * Constructor method for the Entity object.
	 * 
	 * @param sprite The sprite of the entity.
	 * @param life The life value of the entity.
	 */
	public Entity(Sprite sprite, int life)
	{
		this.s = sprite;
		this.life = life;
		isDead = false;
	}
	
	/**
	 * This method handles the death of an entity.
	 */
	public void kill()
	{
		s.setX(-50f);
		s.setY(-50f);
		s.stop();
		s.hide();
	}

	/**
	 * Accessor method for the sprite of the entity.
	 * 
	 * @return The entity's sprite.
	 */
	public Sprite getSprite()
	{
		return s;
	}

	/**
	 * Accessor method for the entity's life.
	 * 
	 * @return The current value for the entity's life.
	 */
	public int getLife()
	{
		return life;
	}
	
	/**
	 * Mutator method for the entity's life.
	 * 
	 * @param newLife The new value for the entity's life.
	 */
	public void setLife(int newLife)
	{
		life = newLife;
	}
	
	/**
	 * Accessor method for the entity's jumps.
	 * 
	 * @return The jumps left.
	 */
	public int getJumps()
	{
		return jumps;
	}
	
	/**
	 * Mutator method for the entity's jumps.
	 * 
	 * @param jumps The new value of jumps left;
	 */
	public void setJumps(int jumps)
	{
		this.jumps = jumps;
	}
	
	/**
	 * Accessor method for the entity's moving direction.
	 * 
	 * @return The direction in which the entity is moving.
	 */
	public int getMovingDirection()
	{
		return movingDirection;
	}

	/**
	 * Mutator method for the entity's moving direction.
	 * 
	 * @param movingDirection The new moving direction.
	 */
	public void setMovingDirection(int movingDirection)
	{
		this.movingDirection = movingDirection;
	}

	/**
	 * Accessor method for the living state of the entity.
	 * 
	 * @return The living state of the entity.
	 */
	public boolean isDead()
	{
		return isDead;
	}
	
	/**
	 * Mutator method for the living state of the entity.
	 * 
	 * @param state The new living stage of the entity.
	 */
	public void setIsDead(boolean state)
	{
		isDead = state;
	}
}
