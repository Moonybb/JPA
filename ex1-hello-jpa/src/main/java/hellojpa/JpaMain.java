package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUserName("hello");
            member.setHomeAdress(new Adress("home", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAdressHistory().add(new Adress("old1", "street", "10000"));
            member.getAdressHistory().add(new Adress("old2", "street", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("=============START=============");
            Member findMember = em.find(Member.class, member.getId());

            // 값 타입 컬렉션도 지연 로딩 전략 사용
            List<Adress> adressHistory = findMember.getAdressHistory();
            for(Adress adress : adressHistory) {
                System.out.println("adress = " + adress.getCity());
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
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
