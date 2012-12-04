package edu.htwm.vsp.phone.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phone.service.PhonebookService;

/**
 * An implementation of {@link PhonebookService} that utilizes the JPA.
 * 
 * @author std
 * 
 */
public class PhonebookServiceImpl implements PhonebookService {

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
	@Transactional
	public List<PhoneUser> fetchAllUsers() {
		TypedQuery<PhoneUser> q = getEntityManager().createQuery("SELECT u from PhoneUser u", PhoneUser.class);
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
