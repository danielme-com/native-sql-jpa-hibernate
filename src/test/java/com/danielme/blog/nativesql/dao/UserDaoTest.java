package com.danielme.blog.nativesql.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Some test cases.
 * 
 * @author danielme.com
 * 
 */
public class UserDaoTest {

	private static UserDao userDao;

	@BeforeClass
	public static void setup() {
		EntityManagerFactory entityManagerFactory = Persistence
				.createEntityManagerFactory("sqlDemoPersistence");
		userDao = new UserDao(entityManagerFactory.createEntityManager());
	}
	
	@Test
	public void testFindAll() {
		assertFalse(userDao.findAll().isEmpty());
	}

	@Test
	public void testFind() {
		assertEquals(1, userDao.findById(1L).getId().longValue());
	}
	
	@Test
	public void testFindAllDetail() {
		assertFalse(userDao.findAllDetail().isEmpty());
	}
	
	/*@Test
	public void testInsert() {
		userDao.insert(20113L, "name", "email");
	}*/
	
	@Test
	public void testFindAllAliasToBean() {
		assertFalse(userDao.findAllAliasToBean().isEmpty());
	}
	
	@Test
	public void testFindNamedQuery() {
		assertEquals(1, userDao.findByIdNamedQuery(1L).getId().longValue());
	}
	
	@Test
	public void testFindAllDetailNamedQuery() {
		assertFalse(userDao.findAllDetailNamedQuery().isEmpty());
	}

}
