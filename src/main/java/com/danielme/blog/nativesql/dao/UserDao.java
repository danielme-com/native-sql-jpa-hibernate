package com.danielme.blog.nativesql.dao;

import com.danielme.blog.nativesql.UserDetail;
import com.danielme.blog.nativesql.entities.User;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
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


    public List<UserDetail> findAllDetail() {
        Query nativeQuery = em.createNativeQuery("SELECT id, CONCAT(name, '-', email) as concat From user", "DetailMapping");
        return nativeQuery.getResultList();
    }

    public void insert(Long id, String name, String email) {
        em.getTransaction().begin();
        Query query = em.createNativeQuery("INSERT INTO user (id, name, email) VALUES(?,?,?)");
        query.setParameter(1, id);
        query.setParameter(2, name);
        query.setParameter(3, email);
        query.executeUpdate();
        em.getTransaction().commit();
    }

    public List<UserDetail> findAllDetailTransformer() {
        NativeQuery nativeQuery = ((Session) this.em.getDelegate()).createSQLQuery("SELECT id, CONCAT(name, '-', email) AS concat FROM user");
        nativeQuery.setResultTransformer(new DetailTransformer());
        return nativeQuery.list();
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
        NativeQuery nativeQuery = ((Session) this.em.getDelegate()).createSQLQuery("SELECT id as \"id\", CONCAT(name, '-', email) AS \"details\" FROM user");
        nativeQuery.setResultTransformer(new AliasToBeanResultTransformer(UserDetail.class));
        return nativeQuery.list();
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
