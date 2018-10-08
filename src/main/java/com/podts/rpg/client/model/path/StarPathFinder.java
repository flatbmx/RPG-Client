package com.podts.rpg.client.model.path;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

import com.podts.rpg.client.model.Location.Direction;
import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Tile;

public class StarPathFinder extends PathFinder {

	@Override
	protected Optional<ListPath> doFindPath(Tile start, Tile finish) {
		Queue<Path> paths = new PriorityQueue<>(constructPathComparator(finish));
		Optional<ListPath> result = expandPath(new ReferencePath(start), finish, paths);
		if(result.isPresent())
			return result;
		while(!paths.isEmpty()) {
			Path currentPath = paths.poll();
			result = expandPath(currentPath, finish, paths);
			if(result.isPresent())
				return result;
		}
		return result;
	}
	
	private static Optional<ListPath> expandPath(Path path, Tile finish, Queue<Path> paths) {
		Tile end = path.getFinish();
		for(Direction dir : Direction.values()) {
			Tile newEnd = Client.get().getWorld().getTile(end.getLocation().shift(dir));
			if(newEnd == null) continue;
			if(!newEnd.isTraversable()) continue;
			if(!path.contains(newEnd)) {
				Path newPath = path.extend(newEnd);
				if(newEnd.isAt(finish))
					return Optional.of(newPath.finalizePath());
				paths.add(newPath);
			}
		}
		return Optional.empty();
	}
	
	private static final Comparator<Path> constructPathComparator(final Tile finish) {
		return (a,b) -> {
			int aDistance = a.getFinish().getWalkingDistance(finish);
			int bDistance = b.getFinish().getWalkingDistance(finish);
			int diff = aDistance - bDistance;
			return diff*2 + (a.getLength() - b.getLength());
		};
	}
	
}
