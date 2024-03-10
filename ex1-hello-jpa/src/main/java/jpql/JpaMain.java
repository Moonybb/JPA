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

//            // fetch join
//            // SQL 조인 종류가 아님
//            // JPQL에서 성능 최적화를 위해 제공
//            // 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
//            // join fetch 명령어 사용
//            // 페치 조인 ::= [LEFT [OUTER] | INNER] JOIN FETCH 조인 경로
//
//            // 엔티티 페치 조인
//            // JPQL : select m from Member m join fetch m.team
//            // SQL : SELECT M.*, T.* FROM MEMBER M INNER JOIN TEAM T ON M.TEAM_ID = T.ID
//
//            // 페치조인 미사용 (지연로딩, 프록시로 들고와서 get 할 때마다 조회함.. N+1 발생)
//            String query1 = "select m From Member m";
//
//            List<Member> result1 = em.createQuery(query1, Member.class)
//                    .getResultList();
//
//            for (Member member : result1) {
//                System.out.println("member = " + member + "team = " + member.getTeam().getName());
//
//                // 회원 100명 -> N + 1
//            }
//
//            // 페치조인 사용(즉시로딩, 처음부터 한방쿼리로 들고옴)
//            // 지연로딩(LAZY)로 세팅되어있어도 fetch join이 우선으로 작동함
//            String query2 = "select m From Member m join fetch m.team";
//
//            List<Member> result2 = em.createQuery(query2, Member.class)
//                    .getResultList();
//
//            for (Member member : result2) {
//                System.out.println("member = " + member + "team = " + member.getTeam().getName());
//            }
//
//            // 페치조인
//            String query3 = "select t From Team t join fetch t.members";
//
//            List<Team> result3 = em.createQuery(query3, Team.class)
//                    .getResultList();
//
//            System.out.println("result3.size() = " + result3.size());
//
//            for (Team team : result3) {
//                System.out.println("team = " + team.getName() + " | members = " + team.getMembers().size());
//                System.out.println("===> " + team.getMembers());
//            }
//
//            // distinct
//            // 같은 식별자를 가진 Team 엔티티를 제거한다.
//            String query4 = "select distinct t From Team t join fetch t.members";
//
//            List<Team> result4 = em.createQuery(query4, Team.class)
//                    .getResultList();
//
//            System.out.println("result4.size() = " + result4.size());
//
//            for (Team team : result4) {
//                System.out.println("team = " + team.getName() + " | members = " + team.getMembers().size());
//                System.out.println("===> " + team.getMembers());
//            }

            // 페치 조인 대상에는 별칭을 줄 수 없다.
            // 둘 이상의 컬렉션은 페치 조인 할 수 없다.
            // 컬렉션을 페치 조인하면 페이징 API를 사용할 수 없다.
            // 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
            // 하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)

            String query5 = "select t From Team t";

            List<Team> result5 = em.createQuery(query5, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            for (Team team : result5) {
                System.out.println("team = " + team.getName() + " | members = " + team.getMembers().size());
                System.out.println("===> " + team.getMembers());
            }
            
            // 페치조인의 특징과 한계
            // 연관된 엔티티들을 SQL 한 번으로 조회 - 성능 최적화
            // 엔티티에 직접 적용하는 글로벌 로딩전략보다 우선함
            // 실무에서 글로벌 로딩 전략은 모두 지연 로딩
            // 최적화가 필요한 곳은 페치 조인 적용

            // 모든 것을 페치 조인으로 해결할 수는 없음
            // 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
            // 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전햐 다른 결과를 내야하면,
            // 페치 조인보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적이다

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
