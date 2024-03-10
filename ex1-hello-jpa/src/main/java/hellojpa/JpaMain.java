package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            // JPQL [객체지향 SQL]
            List<Member> resultList1 = em.createQuery(
                    "select m from Member m where m.userName like '%kim%'",
                    Member.class
            ).getResultList();

            List<Member> resultList2 = em.createNativeQuery(
                    "select memberId, city, street, zipCode, userName from MEMBER"
            ).getResultList();


            // Criteria (실무에서 사용하지 않음)
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);
            CriteriaQuery<Member> cq = query.select(m);

            String userName = "abcd";
            if (userName != null) {
                cq = cq.where(cb.equal(m.get("userName"), "kim"));
            }
            List<Member> resultList = em.createQuery(cq).getResultList();


            // QueryDSL



            tx.commit();

        }catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        
        emf.close();
    }

}
