package com.podts.rpg.client.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Location implements Locatable {
	
	public enum Direction {
		UP(0,-1),
		LEFT(-1,0),
		DOWN(0,1),
		RIGHT(1,0);
		
		private static final Direction[] vals = Direction.values();
		private static final List<Direction> all = Collections.unmodifiableList(Arrays.asList(vals));
		
		public static Collection<Direction> all() {
			return all;
		}
		
		public static final Stream<Direction> stream() {
			return all().stream();
		}
		
		public static final Optional<Direction> get(Locatable first, Locatable second) {
			return get(first.getLocation(), second.getLocation());
		}
		
		public static final Optional<Direction> get(Location first, Location second) {
			int dx = Integer.signum(second.getX() - first.getX());
			int dy = Integer.signum(second.getY() - first.getY());
			if((dx != 0 && dy != 0) ||
					(dx == 0 && dy == 0))
				return Optional.empty();
			for(Direction dir : vals) {
				if(dir.getX() == dx && dir.getY() == dy)
					return Optional.of(dir);
			}
			return Optional.empty();
		}
		
		public static final Optional<Direction> get(int dx, int dy) {
			if(dx != 0 && dy != 0)
				return Optional.empty();
			dx = Integer.signum(dx);
			dy = Integer.signum(dy);
			for(Direction d : vals) {
				if(d.getX() == dx && d.getY() == dy)
					return Optional.of(d);
			}
			return Optional.empty();
		}
		
		private final int dx, dy;
		
		public int getX(int distance) {
			return dx * distance;
		}
		
		public int getX() {
			return dx;
		}
		
		public int getY(int distance) {
			return dy * distance;
		}
		
		public int getY() {
			return dy;
		}
		
		public final Direction opposite() {
			return convert(RelationalDirection.BACKWARD);
		}
		
		public final Direction convert(RelationalDirection d) {
			return d.convert(this);
		}
		
		public final Location MoveFromLocation(Location origin, int distance) {
			return origin.shift(getX(distance), getY(distance));
		}
		
		public final Location MoveFromLocation(Location origin) {
			return origin.shift(getX(), getY());
		}
		
		private Direction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
		
	}
	
	public enum RelationalDirection {
		
		FORWARD(d -> d),
		BACKWARD(d -> Direction.vals[(d.ordinal() + 2) % Direction.vals.length] ),
		LEFT(d -> Direction.vals[(d.ordinal() + 1) % Direction.vals.length] ),
		RIGHT(d -> Direction.vals[Math.floorMod(d.ordinal() - 1, Direction.vals.length)] );
		
		private static final RelationalDirection[] vals = RelationalDirection.values();
		
		public static final Stream<RelationalDirection> stream() {
			return Stream.of(vals);
		}
		
		private UnaryOperator<Direction> operator;
		
		public Direction convert(Direction d) {
			return operator.apply(d);
		}
		
		public boolean turns() {
			return ordinal() > 2;
		}
		
		private RelationalDirection(UnaryOperator<Direction> operator) {
			this.operator = operator;
		}
		
	}
	
	/**
	 * Declares two double variables(x and y).
	 */
	
	private int x, y, z;
	
	/**
	 * Gets a locations X variable.
	 * @return x
	 */
	
	public int getX() {
		return x;
	}
	
	/**
	 * Gets a locations Y variable.
	 * @return y
	 */
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY(), getZ());
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof Location) {
			Location oL = (Location) o;
			return x == oL.x
					&& y == oL.y
					&& z == oL.z;
		}
		return false;
	}
	
	public Location move(int dx, int dy, int dz) {
		return new Location(x + dx, y + dy, z + dz);
	}
	
	public final Location getLocation() {
		return this;
	}
	
	public Location shift(int dx, int dy, int dz) {
		return new Location(getX() + dx, getY() + dy, getZ() + dz);
	}
	
	public Location shift(int dx, int dy) {
		return shift(dx, dy, 0);
	}
	
	public Location shift(Direction dir) {
		return new Location(x + dir.dx, y + dir.dy, getZ());
	}
	
	public Location() {
		
	}
	
	/**
	 * Constructs a Location object.
	 * @param x the x coordinate.
	 * @param y the y coordinate.
	 */
	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
}
