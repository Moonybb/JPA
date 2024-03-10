package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUserName("hello");
            member.setHomeAddress(new Address("home", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("=============START=============");
            Member findMember = em.find(Member.class, member.getId());

            // 아래처럼 수정하면 안됨
            // findMember.getHomeAdress().setCity("newCity");

            // 이렇게 값 타입 컬렉션 자체를 갈아끼워주어 수정해야함.
//            Address address = findMember.getHomeAddress();
//            findMember.setHomeAddress(new AddressEntity("newCity", address.getStreet(), address.getZipCode()));

            // 치킨 -> 한식
//            findMember.getFavoriteFoods().remove("치킨");
//            findMember.getFavoriteFoods().add("한식");

            // 주소 변경
//            findMember.getAdressHistory().remove(new Address("old1", "street", "10000"));
//            findMember.getAdressHistory().add(new Address("newCity", "street", "10000"));

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
