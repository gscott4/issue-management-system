package org.jee8ng.users.boundary;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.Valid;

import org.jee8ng.users.entity.User;

@Stateless
public class UsersService {
	
	/*
	 * An entity manager is an instance off of the persistence context.
	 * A persistence context is a set of entity instances in which they are all unique instances (?)
	 * EntityManager is user to create and remove persistent entity instances
	 *     - To find entities by their primary keys and to query over entities
	 */
	@PersistenceContext
	private EntityManager em;
	
	public User isValid(String username, String password) {
		
		// em.createQuery creates an instance of the TypedQuery interface and allows us to execute Java Persistence queries
		// In this case we're seeing if the username and password are equal to the User instance (I believe)
		TypedQuery<User> query = em.createQuery("SELECT u from User u WHERE u.credential.username = :username and u.credential.password = :password",
				User.class);
		
		query.setParameter("username", username);
		query.setParameter("password", password);
		
		try {
			// Executes a select query that returns a single result
			return query.getSingleResult();
		} catch(NoResultException nre) {}
		
		// I believe this is a very bad practice, however, I'm following along
		return null;
	}
	
	public Set<User> getAll() {
		
		List<User> list = em.createQuery("FROM User u", User.class).getResultList();
		
		// Creates a hash set that is iterable and placed in set when it was added
		return new LinkedHashSet<User>(list);
	}
	
	public void add(@Valid User newUser) {
		
		// persist creates a new instance that is managed and persistent
		em.persist(newUser);
	}
	
	public boolean update(User updated) {
		
		User found = em.find(User.class, updated.getId());
		if (found != null) {
			em.merge(updated); // merge the state of the given entity into the current persistence context
			return true;
		} else {
			return false;
		}
	}
	
	public void remove(Long id) {
		
		// Why not a TypedQuery.. What is the difference? 
		Query query = em.createQuery("DELETE FROM User u WHERE u.id = :id");
		query.setParameter("id", id) // Binds an argument value to a named parameter
			.executeUpdate();
	}
	
	public Optional<User> get(Long id) {
		User found = em.find(User.class, id);
		return found != null ? Optional.of(found) : Optional.empty();
	}
	
	public List<String> getNames() {
		
		TypedQuery<User> query = em.createQuery("SELECT u from User u", User.class);
		
		List<String> names = query.getResultStream().map(User::getName)
				.collect(Collectors.toList());
		return names;
	}
}
