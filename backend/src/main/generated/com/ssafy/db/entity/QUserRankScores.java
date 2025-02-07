package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserRankScores is a Querydsl query type for UserRankScores
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserRankScores extends EntityPathBase<UserRankScores> {

    private static final long serialVersionUID = 1671037402L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserRankScores userRankScores = new QUserRankScores("userRankScores");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath exerciseType = createString("exerciseType");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Short> rankScore = createNumber("rankScore", Short.class);

    public final QUser user;

    public QUserRankScores(String variable) {
        this(UserRankScores.class, forVariable(variable), INITS);
    }

    public QUserRankScores(Path<? extends UserRankScores> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserRankScores(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserRankScores(PathMetadata metadata, PathInits inits) {
        this(UserRankScores.class, metadata, inits);
    }

    public QUserRankScores(Class<? extends UserRankScores> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

