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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("member");
            member1.setAge(10);
            member1.setType(MemberType.ADMIN);
            member1.changeTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername(null);
            member2.setAge(20);
            member2.setType(MemberType.ADMIN);
            member2.changeTeam(team);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("관리자");
            member3.setAge(30);
            member3.setType(MemberType.ADMIN);
            member3.changeTeam(team);
            em.persist(member3);

            em.flush();
            em.clear();

            // 명시적 조인 : join 키워드를 직접 사용
            // select m from Member m join m.team t

            // 묵시적 조인 : 경로 표현식에 의해 묵시적으로 SQL 조인 발생 (내부 조인만 가능)
            // select m.team from Member m

            // 묵시적 내부 조인 발생
            // 이렇게 쿼리를 짜는건 권장하지 않음
            String query1 = "select m.team from Member m";
            List<Team> result1 = em.createQuery(query1, Team.class)
                    .getResultList();

            for (Team s : result1) {
                System.out.println("s = " + s.getName());
            }

            // 컬렉션 값은 묵시적 내부 조인은 발생하지만 탐색할 수 없다.
            // 이것도 권장하지 않는 방법
            String query2 = "select t.members from Team t";
            List result2 = em.createQuery(query2, Collections.class)
                    .getResultList();

            for (Object o : result2) {
                System.out.println("o = " + o);
            }

            // 컬렉션 값 내부탐색을 하기 위해 아래처럼 명시적 조인을 사용해 탐색한다.
            // 명시적 조인을 통해 별칭을 얻어 접근
            String query3 = "select m.username from Team t join t.members m";
            List<String> result3 = em.createQuery(query3, String.class)
                    .getResultList();

            for (String s : result3) {
                System.out.println("s = " + s);
            }

            // 가급적 묵시적 조인 대신 명시적 조인을 사용!
            // 묵시적 조인은 조인이 일어나는 상황을 한 눈에 파악하기 어려움

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
