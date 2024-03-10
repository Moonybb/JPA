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
            member.setTeam(team);
            em.persist(member);


            em.flush();
            em.clear();

            // 내부 조인
            String innerJoinQuery = "select m from Member m inner join m.team t";
            List<Member> members1 = em.createQuery(innerJoinQuery, Member.class)
                    .getResultList();

            // 외부 조인
            String leftJoinQuery = "select m from Member m left join m.team t";
            List<Member> members2 = em.createQuery(leftJoinQuery, Member.class)
                    .getResultList();

            // 세타 조인
            String cetaJoinQuery = "select m from Member m, Team t where m.username = t.name";
            List<Member> members3 = em.createQuery(cetaJoinQuery, Member.class)
                    .getResultList();

            // 조인 대상 필터링
            // ex) 회원과 팀을 조인하면서 회원과 팀의 이름이 같은 대상 외부 조인
            // JPQL : select m, t from Member m left join Team t on m.username = t.name
            // SQL : select m.*, t.* from Member m left join Team t on m.username = t.name
            String query = "select m from Member m left join m.team t on m.username = t.name";
            List<Member> members4 = em.createQuery(query, Member.class)
                    .getResultList();

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
