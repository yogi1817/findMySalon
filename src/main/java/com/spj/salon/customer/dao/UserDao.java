package com.spj.salon.customer.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.spj.salon.barber.entities.Authorities;
import com.spj.salon.customer.entities.User;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Repository
public class UserDao implements IUserDao{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
    public List<User> searchUserWithEmailAndAuthority(String email, String authority) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        
        Root<User> user = criteriaQuery.from(User.class);
        Join<User, Authorities> authorityJoin = user.join("authority");
 
        Predicate emailPredicate = criteriaBuilder.equal(user.get("email"), email);
        Predicate authorityPredicate = criteriaBuilder.equal(authorityJoin.get("authority"), authority);
        
        TypedQuery<User> userQuery = entityManager.createQuery(
        									criteriaQuery.select(user)
        									.where(emailPredicate, authorityPredicate)
        									.distinct(true)
        								);
        
        return userQuery.getResultList();
    }

	@Override
	public List<User> searchUserWithUserIdAndAuthority(Long userId, String authority) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        
        Root<User> user = criteriaQuery.from(User.class);
        Join<User, Authorities> authorityJoin = user.join("authorities");
 
        Predicate loginIdPredicate = criteriaBuilder.equal(user.get("userId"), userId);
        Predicate authorityPredicate = criteriaBuilder.equal(authorityJoin.get("authority"), authority);
        
        TypedQuery<User> userQuery = entityManager.createQuery(
        									criteriaQuery.select(user)
        									.where(loginIdPredicate, authorityPredicate)
        									.distinct(true)
        								);
        
        return userQuery.getResultList();
	}
}
