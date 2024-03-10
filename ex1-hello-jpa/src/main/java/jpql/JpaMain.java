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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);


            em.flush();
            em.clear();

            // JPA는 where, having절에서만 서브 쿼리 사용 가능
            // select 절도 가능 (하이버네이트에서 지원)
            // from 절의 서브 쿼리는 현재 JPQL에서 불가능 : 조인으로 풀 수 있으면 풀어서 해결
            String query = "select (select avg(m1.age) from Member m1) from Member m";


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
