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

            String query1 =
                    "select " +
                      "case when m.age <= 10 then '학생요금' " +
                           "when m.age >= 60 then '경로요금' " +
                           "else '일반요금' " +
                           "end " +
                      "from Member m";

            List<String> result1 = em.createQuery(query1, String.class)
                    .getResultList();
            for (String s : result1) {
                System.out.println("s1 = " + s);
            }


            // coalesce
            String query2 = "select coalesce(m.username, '이름 없는 회원') from Member m";
            List<String> result2 = em.createQuery(query2, String.class).getResultList();
            for (String s : result2) {
                System.out.println("s2 = " + s);
            }

            // nullif
            String query3 = "select nullif(m.username, '관리자') as username from Member m";
            List<String> result3 = em.createQuery(query3, String.class).getResultList();
            for (String s : result3) {
                System.out.println("s3 = " + s);
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
