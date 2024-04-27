package sample.thecommerce.domain.user;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import sample.thecommerce.dto.user.response.QUserResponse;
import sample.thecommerce.dto.user.response.UserResponse;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static sample.thecommerce.domain.user.QUser.user;

@Repository
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    public UserQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<UserResponse> searchPageUsers(Pageable pageable) {

        List<OrderSpecifier> orderSpecifiers = getOrderSpecifiers(pageable.getSort());

        List<UserResponse> content = queryFactory
                .select(new QUserResponse(
                        user.id.as("userId"),
                        user.username,
                        user.nickname,
                        user.name,
                        user.tel,
                        user.email,
                        user.createDateTime
                ))
                .from(user)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<User> countQuery = queryFactory
                .selectFrom(user);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        PathBuilder<User> entityPath = new PathBuilder<>(User.class, "user");
        return sort.stream()
                .filter(order -> order.getProperty().equals("name") || order.getProperty().equals("createDateTime"))
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    return new OrderSpecifier(direction, entityPath.get(order.getProperty()));
                }).collect(Collectors.toList());
    }

}
