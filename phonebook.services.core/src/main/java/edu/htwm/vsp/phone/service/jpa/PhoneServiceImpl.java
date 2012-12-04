package edu.htwm.vsp.phone.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import edu.htwm.vsp.phone.service.NotFoundException;
import edu.htwm.vsp.phone.service.PhoneService;
import edu.htwm.vsp.phone.service.PhoneUser;

/**
 * An implementation of {@link PhoneService} that utilizes the JPA.
 * 
 * @author std
 * 
 */
public class PhoneServiceImpl implements PhoneService {

	@Inject
	private EntityManager entityManager;
	
	
	@Override
	@Transactional
	public PhoneUser createUser(String name) {
		
		PhoneUser u = new PhoneUser(name);
		getEntityManager().persist(u);
		
		return u;
	}

	@Override
	@Transactional
	public PhoneUser findUserByName(String name) throws NotFoundException {
		
//		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//		TypedQuery<User> q = getEntityManager().createQuery("SELECT u FROM User u WHERE u.name LIKE :name", User.class);
//		q.setParameter("name", name);

		return null;
	}

	@Override
	@Transactional
	public PhoneUser findUserById(int userID) {
		return getEntityManager().find(PhoneUser.class, userID);
	}
	
	@Override
	@Transactional
	public void deleteUser(int userID) {
		PhoneUser userToDelete = findUserById(userID);
		
		if(userToDelete == null)
			return;
		
		getEntityManager().remove(userToDelete);
		getEntityManager().flush();
	}


	@Override
	public List<PhoneUser> fetchAllUsers() {
		TypedQuery<PhoneUser> q = getEntityManager().createQuery("SELECT u from User u", PhoneUser.class);
		List<PhoneUser> results = q.getResultList();
		 
		 return results;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
