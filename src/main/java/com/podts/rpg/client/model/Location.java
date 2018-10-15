package com.podts.rpg.client.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Location implements Locatable {
	
	public enum Direction {
		UP(0,-1),
		TOP_LEFT(1,-1),
		LEFT(-1,0),
		BOTTOM_LEFT(-1,-1),
		DOWN(0,1),
		BOTTOM_RIGHT(1,-1),
		RIGHT(1,0),
		TOP_RIGHT(-1,1);
		
		private static final Direction[] vals = Direction.values();
		private static final Direction[] diagVals = new Direction[4];
		private static final List<Direction> all = Collections.unmodifiableList(Arrays.asList(vals));
		private static final List<Direction> diagAll;
		
		static {
			diagAll = Collections.unmodifiableList(Arrays.asList(diagVals));
			int i = 0;
			for(Direction d : vals) {
				if(d.isDiagonal())
					diagVals[i++] = d;
			}
		}
		
		public static Collection<Direction> getAll() {
			return all;
		}
		
		public static Collection<Direction> getDiagonals() {
			return diagAll;
		}
		
		public static Stream<Direction> all() {
			return getAll().stream();
		}
		
		public static Stream<Direction> diagonals() {
			return getDiagonals().stream();
		}
		
		public static Optional<Direction> get(Locatable first, Locatable second) {
			return get(first.getLocation(), second.getLocation());
		}
		
		public static Optional<Direction> get(int dx, int dy) {
			if((Math.abs(dx) > 0 || Math.abs(dy) > 0)) {
				if(Math.abs(dx) != Math.abs(dy))
					return Optional.empty();
			}
			
			dx = Integer.signum(dx);
			dy = Integer.signum(dy);
			
			for(Direction dir : vals) {
				if(dir.getX() == dx && dir.getY() == dy)
					return Optional.of(dir);
			}
			throw new AssertionError("No direction found after filter! Method should be re-evaluated!");
		}
		
		public static Optional<Direction> get(Location first, Location second) {
			int dx = second.getX() - first.getX();
			int dy = second.getY() - first.getY();
			return get(dx, dy);
		}
		
		private final int dx, dy;
		private final boolean isDiagonal;
		
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
		
		public final boolean isDiagonal() {
			return isDiagonal;
		}
		
		public Direction left(int amount) {
			return convert(RelationalDirection.LEFT, amount);
		}
		
		public Direction left() {
			return left(1);
		}
		
		public Direction right(int amount) {
			return convert(RelationalDirection.RIGHT, amount);
		}
		
		public Direction right() {
			return right(1);
		}
		
		public final Direction opposite() {
			return convert(RelationalDirection.BACKWARD);
		}
		
		public final Direction convert(RelationalDirection d, int i) {
			return d.convert(this, i);
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
			isDiagonal = dx != 0 && dy != 0;
		}
		
	}
	
	public enum RelationalDirection {
		
		FORWARD((d,i) -> d),
		BACKWARD((d,i) -> Direction.vals[(d.ordinal() + 4) % Direction.vals.length] ),
		LEFT((d,i) -> Direction.vals[(d.ordinal() + i) % Direction.vals.length] ),
		RIGHT((d,i) -> Direction.vals[Math.floorMod(d.ordinal() - i, Direction.vals.length)] );
		
		private static final RelationalDirection[] vals = RelationalDirection.values();
		private static final List<RelationalDirection> all = Collections.unmodifiableList(Arrays.asList(vals));
		
		public static Collection<RelationalDirection> getAll() {
			return all;
		}
		
		public static Stream<RelationalDirection> stream() {
			return getAll().stream();
		}
		
		private final BiFunction<Direction,Integer,Direction> operator;
		
		public Direction convert(Direction d, int i) {
			return operator.apply(d, i);
		}
		
		public Direction convert(Direction d) {
			return convert(d, 1);
		}
		
		public boolean turns() {
			return ordinal() > 1;
		}
		
		private RelationalDirection(BiFunction<Direction,Integer,Direction> operator) {
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
