package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // 반환타입이 명확할 때
            TypedQuery<Member> q1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> q2 = em.createQuery("select m.username from Member m", String.class);
            
            // 반환타입이 명확하지 않을 때
            Query q3 = em.createQuery("select m.username from Member m");

            // resultList : 결과가 없으면 빈 리스트 반환
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            List<Member> resultList = query1.getResultList();
            for (Member m : resultList) {
                System.out.println("m = " + m);
            }

            // singleList : 결과가 정확히 하나, 단일객체 반환 (오류 주의)
            TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.age = 10", Member.class);
            Member singleResult1 = query2.getSingleResult();
            System.out.println("singleResult1 = " + singleResult1.getUsername());

            // 파라미터 바인딩
            Member singleResult2 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult2 = " + singleResult2.getUsername());


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
