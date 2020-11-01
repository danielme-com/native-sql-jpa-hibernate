package com.danielme.blog.nativesql;

import com.danielme.blog.nativesql.dao.UserDaoTest;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DbUtils {

    public static void loadScript(EntityManager em, String scriptName) throws IOException, URISyntaxException {
        Path path = Paths.get(UserDaoTest.class.getClassLoader()
                .getResource(scriptName).toURI());
        try {
            em.getTransaction().begin();
            Files.lines(path).forEach(l->em.createNativeQuery(l).executeUpdate());
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        }
    }
}
