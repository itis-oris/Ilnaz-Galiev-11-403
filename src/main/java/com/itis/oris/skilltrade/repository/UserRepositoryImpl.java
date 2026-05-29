package com.itis.oris.skilltrade.repository;

import com.itis.oris.skilltrade.entity.Skill;
import com.itis.oris.skilltrade.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> searchUsers(Long skillId,
                                  String city,
                                  String usernameFragment,
                                  Boolean onlyBlocked) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (skillId != null) {
            Join<User, Skill> skillsJoin = user.join("skills", JoinType.INNER);
            predicates.add(cb.equal(skillsJoin.get("id"), skillId));
        }

        if (city != null && !city.isBlank()) {
            predicates.add(cb.equal(cb.lower(user.get("city")), city.toLowerCase()));
        }

        if (usernameFragment != null && !usernameFragment.isBlank()) {
            predicates.add(cb.like(
                    cb.lower(user.get("username")),
                    "%" + usernameFragment.toLowerCase() + "%"
            ));
        }

        if (onlyBlocked != null) {
            predicates.add(cb.equal(user.get("blocked"), onlyBlocked));
        }

        cq.select(user).distinct(true);
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        cq.orderBy(cb.asc(user.get("username")));

        return em.createQuery(cq).getResultList();
    }
}
