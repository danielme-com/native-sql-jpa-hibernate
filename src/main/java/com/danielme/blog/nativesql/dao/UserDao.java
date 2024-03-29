package com.danielme.blog.nativesql.dao;

import com.danielme.blog.nativesql.UserDetail;
import com.danielme.blog.nativesql.UserDetailRecord;
import com.danielme.blog.nativesql.entities.User;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {

    private final EntityManager em;

    public UserDao(EntityManager em) {
        this.em = em;
    }

    public List<User> findAll() {
        Query nativeQuery = em.createNativeQuery("SELECT id, name, email FROM user ORDER BY email DESC", User.class);

        return nativeQuery.getResultList();
		/*List<Object[]> results = nativeQuery.getResultList();
		
		return results
				.stream()
				.map(result -> new User(((BigInteger) result[0]).longValue(), (String) result[1], (String) result[2]))
				.collect(Collectors.toList());*/
    }

    public BigInteger count() {
        return (BigInteger) em.createNativeQuery("SELECT count(*) FROM user").getSingleResult();
    }

    public List<User> findAllWithTuples() {
        Query nativeQuery = em.createNativeQuery("SELECT id, name, email FROM user ORDER BY email DESC", Tuple.class);
        List<Tuple> tuples = nativeQuery.getResultList();
        return tuples.stream()
                .map(t -> new User(t.get("id", BigInteger.class).longValue(), t.get("name", String.class), t.get("email", String.class)))
                .collect(Collectors.toList());
    }

    public User findById(Long id) {
        Query nativeQuery = em.createNativeQuery("SELECT id, name, email FROM user WHERE id = :id", User.class);
        nativeQuery.setParameter("id", id);

        return (User) nativeQuery.getSingleResult();
		/*Object[] result = (Object[]) nativeQuery.getSingleResult();
		return new User(((BigInteger) result[0]).longValue(), (String) result[1], (String) result[2]);*/
    }

    public List<User> findByText(String text) {
        Query nativeQuery = em.createNativeQuery("SELECT * FROM user " +
                "WHERE name LIKE LOWER(:text) OR email LIKE LOWER(:text)", User.class);
        String textSearch = text == null ? "%" : "%" + text.toLowerCase() + "%";
        nativeQuery.setParameter("text", textSearch);
        return nativeQuery.getResultList();
    }

    public List<UserDetail> findAllDetail() {
        Query nativeQuery = em.createNativeQuery("SELECT id, CONCAT(name, '-', email) as concat From user", "DetailMapping");
        return nativeQuery.getResultList();
    }

    public void insert(String name, String email) {
        em.getTransaction().begin();
        Query query = em.createNativeQuery("INSERT INTO user (name, email) VALUES(:name,:email)");
        query.setParameter("name", name);
        query.setParameter("email", email);
        query.executeUpdate();
        em.getTransaction().commit();
    }

    public List<UserDetail> findAllDetailTransformer() {
        Query query = em.createNativeQuery("SELECT id, CONCAT(name, '-', email) AS concat FROM user");
        query.unwrap(org.hibernate.query.Query.class).setResultTransformer(new DetailTransformer());
        return query.getResultList();
    }

    public User findByIdNamedQuery(Long id) {
        TypedQuery<User> nativeQuery = em.createNamedQuery("selectFindById", User.class);
        nativeQuery.setParameter(1, id);
        return nativeQuery.getSingleResult();
    }

    public List<UserDetail> findAllDetailNamedQuery() {
        TypedQuery<UserDetail> nativeQuery = em.createNamedQuery("selectFindAllDetail", UserDetail.class);
        return nativeQuery.getResultList();
    }

    public List<UserDetail> findAllAliasToBean() {
        Query query = em.createNativeQuery("SELECT id as \"id\", CONCAT(name, '-', email) AS \"details\" FROM user");
        query.unwrap(org.hibernate.query.Query.class).setResultTransformer(new AliasToBeanResultTransformer(UserDetail.class));
        return query.getResultList();
    }

    public List<UserDetailRecord> findAllConstructorTransformer() {
        Query query = em.createNativeQuery("SELECT id as \"id\", CONCAT(name, '-', email) AS \"details\" FROM user");
        Constructor<UserDetailRecord> constructor;
        try {
            constructor = UserDetailRecord.class.getConstructor(BigInteger.class, String.class);
        } catch (NoSuchMethodException ee) {
            throw new RuntimeException("constructor for " + UserDetailRecord.class + "not found !!!");
        }
        query.unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
        return query.getResultList();
    }

    public List<User> findAll(int first, int max) {
        /*
         * Query nativeQuery = em.createNativeQuery("SELECT * FROM user ORDER BY id LIMIT :first OFFSET :max", User.class);
         * nativeQuery.setParameter("first", first);
         * nativeQuery.setParameter("max", max);
         */
        Query nativeQuery = em.createNativeQuery("SELECT * FROM user ORDER BY id", User.class);
        nativeQuery.setFirstResult(first);
        nativeQuery.setMaxResults(max);
        return nativeQuery.getResultList();
    }

    private static class DetailTransformer implements ResultTransformer {

        private static final long serialVersionUID = 1L;

        @Override
        public Object transformTuple(Object[] tuple, String[] aliases) {
            return new UserDetail(((BigInteger) tuple[0]), (String) tuple[1]);
        }

        @Override
        public List transformList(List collection) {
            return collection;
        }

    }


}
