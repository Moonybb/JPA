package jpql;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            // fetch join
            // SQL 조인 종류가 아님
            // JPQL에서 성능 최적화를 위해 제공
            // 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
            // join fetch 명령어 사용
            // 페치 조인 ::= [LEFT [OUTER] | INNER] JOIN FETCH 조인 경로

            // 엔티티 페치 조인
            // JPQL : select m from Member m join fetch m.team
            // SQL : SELECT M.*, T.* FROM MEMBER M INNER JOIN TEAM T ON M.TEAM_ID = T.ID

            // 페치조인 미사용 (지연로딩, 프록시로 들고와서 get 할 때마다 조회함.. N+1 발생)
            String query1 = "select m From Member m";

            List<Member> result1 = em.createQuery(query1, Member.class)
                    .getResultList();

            for (Member member : result1) {
                System.out.println("member = " + member + "team = " + member.getTeam().getName());

                // 회원 100명 -> N + 1
            }

            // 페치조인 사용(즉시로딩, 처음부터 한방쿼리로 들고옴)
            // 지연로딩(LAZY)로 세팅되어있어도 fetch join이 우선으로 작동함
            String query2 = "select m From Member m join fetch m.team";

            List<Member> result2 = em.createQuery(query2, Member.class)
                    .getResultList();

            for (Member member : result2) {
                System.out.println("member = " + member + "team = " + member.getTeam().getName());
            }

            // 페치조인
            String query3 = "select t From Team t join fetch t.members";

            List<Team> result3 = em.createQuery(query3, Team.class)
                    .getResultList();

            System.out.println("result3.size() = " + result3.size());

            for (Team team : result3) {
                System.out.println("team = " + team.getName() + " | members = " + team.getMembers().size());
                System.out.println("===> " + team.getMembers());
            }

            // distinct
            // 같은 식별자를 가진 Team 엔티티를 제거한다.
            String query4 = "select distinct t From Team t join fetch t.members";

            List<Team> result4 = em.createQuery(query4, Team.class)
                    .getResultList();

            System.out.println("result4.size() = " + result4.size());

            for (Team team : result4) {
                System.out.println("team = " + team.getName() + " | members = " + team.getMembers().size());
                System.out.println("===> " + team.getMembers());
            }

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
