package edu.htwm.vsp.phone.services.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.google.inject.persist.Transactional;

import edu.htwm.vsp.phone.entity.User;
import edu.htwm.vsp.phone.services.NotFoundException;
import edu.htwm.vsp.phone.services.PhoneService;

/**
 * An implementation of {@link PhoneService} that utilizes the JPA.
 * 
 * @author std
 * 
 */
public class PhoneServiceImpl implements PhoneService {

	private EntityManager entityManager;
	
	
	@Override
	@Transactional
	public User createUser(String name) {
		User u = new User(name);
		getEntityManager().persist(u);
		return u;
	}

	@Override
	@Transactional
	public User findUserByName(String name) throws NotFoundException {
		
//		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//		TypedQuery<User> q = getEntityManager().createQuery("SELECT u FROM User u WHERE u.name LIKE :name", User.class);
//		q.setParameter("name", name);

		return null;
	}

	@Override
	@Transactional
	public User findUserById(int userID) {
		return getEntityManager().find(User.class, userID);
	}
	
	public void deleteUser(int userID) {
		User userToDelete = findUserById(userID);
		getEntityManager().remove(userToDelete);
		getEntityManager().flush();
	}

	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<User> fetchAllUsers() {
		TypedQuery<User> q = getEntityManager().createQuery("SELECT u from User u", User.class);
		List<User> results = q.getResultList();
		 
		 return results;
	}

}
