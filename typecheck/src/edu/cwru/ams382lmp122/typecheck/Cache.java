package edu.cwru.ams382lmp122.typecheck;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

final class Cache<T, V> {
	
	private Map<T, V> cache;
	
	Cache() {
		cache = new HashMap<>();
	}
	
	public V get(T key, Function<? super T, ? extends V> constructor) throws NullPointerException {
		Objects.requireNonNull(key, "Key cannot be null");
		Objects.requireNonNull(constructor, "Constructor cannot be null");
		
		// If the key does not exist in the cache, compute the appropriate value using the constructor
		// and store it in the cache
		return cache.computeIfAbsent(key, constructor);
	}
	
}
