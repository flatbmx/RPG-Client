package com.podts.rpg.client.model.path;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Location.Direction;
import com.podts.rpg.client.model.Tile;

public class StarPathFinder extends PathFinder {
	
	public static final int DEFAULT_MAX_LENGTH = 10;
	
	private final int maxLength;
	
	@Override
	protected Optional<ListPath> doFindPath(Tile start, Tile finish) {
		Queue<Path> paths = new PriorityQueue<>(constructPathComparator(finish));
		Set<Tile> walkedOn = new HashSet<>();
		Optional<ListPath> result = expandPath(new ReferencePath(start), finish, paths, walkedOn);
		if(result.isPresent())
			return result;
		while(!paths.isEmpty()) {
			Path currentPath = paths.poll();
			result = expandPath(currentPath, finish, paths, walkedOn);
			if(result.isPresent())
				return result;
		}
		return result;
	}
	
	private Optional<ListPath> expandPath(Path path, Tile finish, Queue<Path> paths, Set<Tile> walkedOn) {
		Tile end = path.getFinish();
		for(Direction dir : Direction.values()) {
			Tile newEnd = Client.get().getWorld().getTile(end.getLocation().shift(dir));
			if(newEnd == null) continue;
			if(!newEnd.isTraversable()) continue;
			if(!walkedOn.contains(newEnd)) {
				Path newPath = path.extend(newEnd);
				if(newEnd.isAt(finish)) {
					newPath.getStart();
					return Optional.of(newPath.finalizePath());
				}
				if(newPath.getLength() >= maxLength)
					continue;
				paths.add(newPath);
				walkedOn.add(newEnd);
			}
		}
		return Optional.empty();
	}
	
	private static final Comparator<Path> constructPathComparator(final Tile finish) {
		return (a,b) -> {
			return a.getLength() - b.getLength();
		};
	}
	
	public StarPathFinder(int maxLength) {
		this.maxLength = maxLength;
	}
	
	public StarPathFinder() {
		this(DEFAULT_MAX_LENGTH);
	}
	
}
