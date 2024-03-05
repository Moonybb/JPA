package hellojpa;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUserName("member1");
            em.persist(member);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member.getId());
            System.out.println("refMember = " + refMember.getClass());

            // refMember.getUserName(); // 강제 초기화
            Hibernate.initialize(refMember);

            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));

            tx.commit();
        }catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        
        emf.close();
    }

    private static void printMember(Member member) {
        System.out.println("member = " + member.getUserName());
    }
    
    private static void printMemberAndTeam(Member member) {
        String userName = member.getUserName();
        System.out.println("userName = " + userName);
        
        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }
}
