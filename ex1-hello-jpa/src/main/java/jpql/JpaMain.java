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

            em.flush();
            em.clear();

            // 엔티티 프로젝션 select m from Member m
            // 이 때, 영속성 컨텍스트에서 관리가 될까? : ㅇㅇ 관리됨.
            List<Member> members = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            Member findMember = members.get(0);
            findMember.setAge(20);

            // 엔티티 프로젝션 select m.team from Member m / select t from Member m join m.team t
            List<Team> teams = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            // 임베디드 타입 프로젝션
            List<Address> addresses = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();

            // 스칼라 타입 프로젝션
            em.createQuery("select distinct m.username, m.age from Member m")
                            .getResultList();


            // 프로젝션 - 여러 값 조회

            // 방법 1
            List resultList1 = em.createQuery("select m.username, m.age from Member m")
                    .getResultList();

            Object o = resultList1.get(0);
            Object[] result = (Object[]) o;
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);

            // 방법 2
            List<MemberDTO> resultList2 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = resultList2.get(0);
            System.out.println("memberDTO.username = " + memberDTO.getUsername());
            System.out.println("memberDTO.age = " + memberDTO.getAge());

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
