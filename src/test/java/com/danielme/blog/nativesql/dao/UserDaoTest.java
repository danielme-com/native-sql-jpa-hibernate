package com.danielme.blog.nativesql.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.danielme.blog.nativesql.ApplicationContext;
import com.danielme.blog.nativesql.dao.UserDao;

/**
 * Some test cases.
 * 
 * @author danielme.com
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContext.class })
//@Transactional
public class UserDaoTest {

	@Autowired
	private UserDao userDao;
	
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
