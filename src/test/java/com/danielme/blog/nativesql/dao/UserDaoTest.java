package com.danielme.blog.nativesql.dao;

import com.danielme.blog.nativesql.DbUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Some test cases.
 *
 * @author danielme.com
 */
public class UserDaoTest {

    private static UserDao userDao;

    @BeforeClass
    public static void setup() throws Exception {
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("sqlDemoPersistence");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        userDao = new UserDao(entityManagerFactory.createEntityManager());
        DbUtils.loadScript(entityManager, "users.sql");
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
        assertEquals(2, userDao.findAllDetail().size());
    }

    @Test
    public void testInsert() {
        userDao.insert(20113L, "name", "email");
    }

    @Test
    public void testFindAllAliasToBean() {
        assertEquals(2, userDao.findAllAliasToBean().size());
    }

    @Test
    public void testFindAllWithTuples() {
        assertEquals(2, userDao.findAllWithTuples().size());
    }

    @Test
    public void testFindNamedQuery() {
        assertEquals(1, userDao.findByIdNamedQuery(1L).getId().longValue());
    }

    @Test
    public void testFindAllDetailNamedQuery() {
        assertFalse(userDao.findAllDetailNamedQuery().isEmpty());
    }

    @Test
    public void testFindAllDetailTransformer() {
        assertEquals(2, userDao.findAllDetailTransformer().size());
    }

    @Test
    public void testCount() {
        assertEquals(2, userDao.count().intValue());
    }

}
