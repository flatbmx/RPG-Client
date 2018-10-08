package com.podts.rpg.client.model.path;

import java.util.Optional;

import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.model.Location.Direction;
import com.podts.rpg.client.model.Tile;

public class StraightPathFinder extends PathFinder {

	@Override
	protected Optional<ListPath> doFindPath(Tile start, Tile finish) {
		Optional<ListPath> result = tryYFirst(start, finish);
		if(result.isPresent())
			return result;
		return tryXFirst(start, finish);
	}
	
	private final Optional<ListPath> tryXFirst(Tile start, Tile finish) {
		Location sl = start.getLocation();
		Location fl = finish.getLocation();
		Path path = new ReferencePath(start);
		if(fl.getX() != sl.getX()) {
			Direction dir = Direction.LEFT;
			if(fl.getX() > sl.getX())
				dir = Direction.RIGHT;
			while(path.getFinish().getLocation().getX() != fl.getX()) {
				path = path.extend(dir);
				if(!path.getFinish().isTraversable())
					return Optional.empty();
			}
		}
		if(fl.getY() != sl.getY()) {
			Direction dir = Direction.UP;
			if(fl.getY() > sl.getY())
				dir = Direction.DOWN;
			while(path.getFinish().getLocation().getY() != fl.getY()) {
				path = path.extend(dir);
				if(!path.getFinish().isTraversable())
					return Optional.empty();
			}
		}
		return Optional.of(path.finalizePath());
	}
	
	private final Optional<ListPath> tryYFirst(Tile start, Tile finish) {
		Location sl = start.getLocation();
		Location fl = finish.getLocation();
		Path path = new ReferencePath(start);
		if(fl.getY() != sl.getY()) {
			Direction dir = Direction.UP;
			if(fl.getY() > sl.getY())
				dir = Direction.DOWN;
			while(path.getFinish().getLocation().getY() != fl.getY()) {
				path = path.extend(dir);
				if(!path.getFinish().isTraversable())
					return Optional.empty();
			}
		}
		if(fl.getX() != sl.getX()) {
			Direction dir = Direction.LEFT;
			if(fl.getX() > sl.getX())
				dir = Direction.RIGHT;
			while(path.getFinish().getLocation().getX() != fl.getX()) {
				path = path.extend(dir);
				if(!path.getFinish().isTraversable())
					return Optional.empty();
			}
		}
		return Optional.of(path.finalizePath());
	}

}
